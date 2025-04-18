package org.example.charityproject1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DonDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom de la campagne est obligatoire")
    private String nomCampagne;
    
    @NotBlank(message = "Le nom du donateur est obligatoire")
    private String nomDonateur;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;
    
    @NotNull(message = "La date est obligatoire")
    private LocalDate date;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNomCampagne() {
        return nomCampagne;
    }
    
    public void setNomCampagne(String nomCampagne) {
        this.nomCampagne = nomCampagne;
    }
    
    public String getNomDonateur() {
        return nomDonateur;
    }
    
    public void setNomDonateur(String nomDonateur) {
        this.nomDonateur = nomDonateur;
    }
    
    public BigDecimal getMontant() {
        return montant;
    }
    
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
