package org.example.charityproject1.service;

import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.repository.OrganisationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminService {

    @Autowired
    private OrganisationsRepository organisationsRepository;

    public void validateOrganisation(String id) {
        System.out.println("Finding organization with ID: " + id);

        // Get all orgs for debugging
        List<Organisations> allOrgs = organisationsRepository.findAll();
        System.out.println("Total orgs in DB: " + allOrgs.size());
        allOrgs.forEach(org -> System.out.println("DB org ID: " + org.getNumeroIdentif()));

        // Try multiple ways to find the organization
        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(id);

        if (!orgOptional.isPresent()) {
            // If not found by numeroIdentif, try by MongoDB _id
            try {
                orgOptional = organisationsRepository.findById(id);
            } catch (Exception e) {
                System.out.println("Error finding by _id: " + e.getMessage());
            }
        }

        if (orgOptional.isPresent()) {
            Organisations org = orgOptional.get();
            System.out.println("Found organization: " + org.getNom());
            org.setValideParAdmin(true);
            organisationsRepository.save(org);
            System.out.println("Organization validated successfully");
        } else {
            throw new RuntimeException("Organisation not found with ID: " + id);
        }
    }

    public void deleteOrganisation(String id) {
        System.out.println("Attempting to delete organisation with ID: " + id);

        try {
            // First try to find by numeroIdentif
            Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(id);

            if (orgOptional.isPresent()) {
                Organisations org = orgOptional.get();
                System.out.println("Found organization by numeroIdentif: " + org.getNom());

                // Delete by the MongoDB String ID
                organisationsRepository.deleteById(org.getId()); // Use the `id` field instead of `getIdOrganisation()`
                System.out.println("Organization deleted by ID: " + org.getId());
            } else {
                // If not found by numeroIdentif, try by MongoDB _id directly
                try {
                    organisationsRepository.deleteById(id);
                    System.out.println("Organization deleted directly by ID: " + id);
                } catch (Exception e) {
                    System.err.println("Failed to delete by ID: " + e.getMessage());
                    throw new RuntimeException("Organisation not found with ID: " + id);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in deleteOrganisation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete organisation: " + e.getMessage());
        }

        // Verify deletion
        try {
            Optional<Organisations> checkDeleted = organisationsRepository.findById(id);
            if (checkDeleted.isPresent()) {
                System.err.println("WARNING: Organization still exists after deletion attempt!");
            } else {
                System.out.println("Verified: Organization no longer exists in database");
            }
        } catch (Exception e) {
            // Ignore verification errors
        }
    }

    // Get all organisations
    public List<Organisations> getAllOrganisations() {
        return organisationsRepository.findAll();
    }
}