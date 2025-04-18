package org.example.charityproject1.controller;

import jakarta.validation.Valid;
import org.example.charityproject1.projection.CampagneResume;
import org.example.charityproject1.service.CampagneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campagnes")
public class CampagneController {

    @Autowired
    private CampagneService campagneService;
    
    /**
     * Récupère toutes les campagnes actives
     * @return Liste des campagnes actives sous forme de projection CampagneResume
     */
    @GetMapping("/actives")
    public ResponseEntity<List<CampagneResume>> getActiveCampagnes() {
        List<CampagneResume> activeCampagnes = campagneService.getActiveCampagnes();
        return ResponseEntity.ok(activeCampagnes);
    }
}
