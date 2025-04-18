package org.example.charityproject1.service;

import org.example.charityproject1.dto.DonDTO;
import org.example.charityproject1.model.Campagne;
import org.example.charityproject1.model.Donation;
import org.example.charityproject1.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private CampagneService campagneService;
    
    /**
     * Enregistre un don pour une campagne
     * @param campagneId ID de la campagne
     * @param donDTO DTO contenant les informations du don
     * @return Le don enregistré transformé en DTO
     * @throws IllegalArgumentException si la campagne n'existe pas ou n'est pas active
     */
    public DonDTO registerDonation(Long campagneId, DonDTO donDTO) {
        // Vérifier que la campagne existe
        Campagne campagne = campagneService.getCampagneById(campagneId);
        if (campagne == null) {
            throw new IllegalArgumentException("La campagne avec l'ID " + campagneId + " n'existe pas");
        }
        
        // Vérifier que la campagne est active
        if (!campagne.isActive()) {
            throw new IllegalArgumentException("La campagne avec l'ID " + campagneId + " n'est pas active");
        }
        
        // Créer et sauvegarder le don
        Donation donation = new Donation();
        donation.setCampagne(campagne);
        donation.setNomDonateur(donDTO.getNomDonateur());
        donation.setMontant(donDTO.getMontant());
        donation.setDate(LocalDate.now());
        
        Donation savedDonation = donationRepository.save(donation);
        
        // Transformer en DTO et retourner
        return entityToDTO(savedDonation);
    }
    
    /**
     * Transforme une entité Donation en DTO
     * @param donation L'entité à transformer
     * @return Le DTO correspondant
     */
    public DonDTO entityToDTO(Donation donation) {
        DonDTO donDTO = new DonDTO();
        donDTO.setId(donation.getId());
        donDTO.setNomCampagne(donation.getCampagne().getNom());
        donDTO.setNomDonateur(donation.getNomDonateur());
        donDTO.setMontant(donation.getMontant());
        donDTO.setDate(donation.getDate());
        return donDTO;
    }
}
