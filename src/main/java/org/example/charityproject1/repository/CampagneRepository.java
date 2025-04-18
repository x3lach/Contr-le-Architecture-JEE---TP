package org.example.charityproject1.repository;

import org.example.charityproject1.model.Campagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    
    @Query("SELECT c FROM Campagne c WHERE c.dateDebut <= CURRENT_DATE AND c.dateFin >= CURRENT_DATE")
    List<Campagne> findActiveCampagnes();
}
