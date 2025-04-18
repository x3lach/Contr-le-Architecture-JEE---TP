package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("user_email");
        if (userEmail == null) {
            return "redirect:/auth/login";
        }

        Optional<Utilisateurs> userOptional = utilisateursRepository.findByEmail(userEmail);
        if (!userOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        model.addAttribute("user", userOptional.get());
        return "user/dashboard";
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("telephone") String telephone,
            @RequestParam("localisation") String localisation,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Récupérer l'utilisateur connecté
        String userEmail = (String) session.getAttribute("user_email");
        if (userEmail == null) {
            return "redirect:/auth/login";
        }

        Optional<Utilisateurs> userOptional = utilisateursRepository.findByEmail(userEmail);
        if (!userOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        // Validation des données
        if (nom == null || nom.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "name-invalid");
            return "redirect:/user/dashboard";
        }

        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttributes.addFlashAttribute("profileError", "email-invalid");
            return "redirect:/user/dashboard";
        }

        if (telephone != null && !telephone.isEmpty() && !telephone.matches("^[0-9]{10}$")) {
            redirectAttributes.addFlashAttribute("profileError", "phone-invalid");
            return "redirect:/user/dashboard";
        }

        if (localisation == null || localisation.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "localisation-invalid");
            return "redirect:/user/dashboard";
        }

        Utilisateurs user = userOptional.get();

        // Vérifier si l'email a changé
        if (!email.equals(userEmail)) {
            // Vérifier si l'email est déjà utilisé par un autre utilisateur
            Optional<Utilisateurs> existingUser = utilisateursRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(user.getUserId())) {
                redirectAttributes.addFlashAttribute("profileError", "email-exists");
                return "redirect:/user/dashboard";
            }

            // Si l'email est modifié, l'utilisateur devra se reconnecter
            user.setEmail(email);
            utilisateursRepository.save(user);
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "Votre email a été modifié. Veuillez vous reconnecter.");
            return "redirect:/auth/login";
        }

        // Mettre à jour les informations utilisateur
        user.setNom(nom);
        user.setTelephone(telephone);
        user.setLocalisation(localisation);
        
        // Traiter l'image si elle est fournie
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // Vérifier la taille de l'image (max 2MB)
                if (profileImage.getSize() > 2 * 1024 * 1024) {
                    redirectAttributes.addFlashAttribute("profileError", "photo-invalid");
                    return "redirect:/user/dashboard";
                }
                
                // Vérifier le type de fichier
                String contentType = profileImage.getContentType();
                if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                    redirectAttributes.addFlashAttribute("profileError", "photo-invalid");
                    return "redirect:/user/dashboard";
                }
                
                // Convertir l'image en Base64
                byte[] bytes = profileImage.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                user.setLogoPath(base64Image);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("profileError", "photo-invalid");
                return "redirect:/user/dashboard";
            }
        }
        
        // Sauvegarder les modifications
        utilisateursRepository.save(user);
        
        // Message de succès
        redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");
        return "redirect:/user/dashboard";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        String userEmail = (String) session.getAttribute("user_email");
        if (userEmail == null) {
            return "redirect:/auth/login";
        }

        Optional<Utilisateurs> userOptional = utilisateursRepository.findByEmail(userEmail);
        if (!userOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        Utilisateurs utilisateur = userOptional.get();

        // Check if current password is correct
        if (!passwordEncoder.matches(currentPassword, utilisateur.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "password-incorrect");
            redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe actuel est incorrect.");
            return "redirect:/user/dashboard?passwordError=true";
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "password-mismatch");
            redirectAttributes.addFlashAttribute("errorMessage", "Les nouveaux mots de passe ne correspondent pas.");
            return "redirect:/user/dashboard?passwordError=true";
        }

        // Check if new password is different from current
        if (passwordEncoder.matches(newPassword, utilisateur.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "password-same");
            redirectAttributes.addFlashAttribute("errorMessage", "Le nouveau mot de passe doit être différent de l'ancien.");
            return "redirect:/user/dashboard?passwordError=true";
        }

        // Password strength validation
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("passwordError", "password-weak");
            redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe doit contenir au moins 8 caractères.");
            return "redirect:/user/dashboard?passwordError=true";
        }

        // Update password
        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        utilisateursRepository.save(utilisateur);

        // Log out the user
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Votre mot de passe a été modifié avec succès. Veuillez vous reconnecter.");
        return "redirect:/auth/login";
    }
}