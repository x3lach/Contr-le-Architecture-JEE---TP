package org.example.charityproject1.controller;

import jakarta.validation.Valid;
import org.example.charityproject1.dto.DonDTO;
import org.example.charityproject1.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campagnes")
public class DonationController {

    @Autowired
    private DonationService donationService;
    
    /**
     * Enregistre un don pour une campagne
     * @param id ID de la campagne
     * @param donDTO DTO contenant les informations du don
     * @return Le don enregistré
     */
    @PostMapping("/{id}/dons")
    public ResponseEntity<DonDTO> registerDonation(
            @PathVariable("id") Long id,
            @Valid @RequestBody DonDTO donDTO) {
        
        DonDTO savedDonation = donationService.registerDonation(id, donDTO);
        return new ResponseEntity<>(savedDonation, HttpStatus.CREATED);
    }
}
