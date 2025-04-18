package org.example.charityproject1.service;

import org.example.charityproject1.model.Campagne;
import org.example.charityproject1.projection.CampagneResume;
import org.example.charityproject1.repository.CampagneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampagneService {

    @Autowired
    private CampagneRepository campagneRepository;
    
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
    
    /**
     * Récupère toutes les campagnes actives
     * @return Liste des campagnes actives sous forme de projection CampagneResume
     */
    public List<CampagneResume> getActiveCampagnes() {
        List<Campagne> activeCampagnes = campagneRepository.findActiveCampagnes();
        return activeCampagnes.stream()
                .map(campagne -> projectionFactory.createProjection(CampagneResume.class, campagne))
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère une campagne par son ID
     * @param id ID de la campagne
     * @return La campagne si elle existe, null sinon
     */
    public Campagne getCampagneById(Long id) {
        return campagneRepository.findById(id).orElse(null);
    }
}
