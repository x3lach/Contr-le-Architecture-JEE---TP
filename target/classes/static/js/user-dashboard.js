document.addEventListener('DOMContentLoaded', function() {
    console.log("User dashboard script loaded");

    // Dropdown functionality
    const dropdownToggle = document.getElementById('dropdownToggle');
    const dropdownContent = document.querySelector('.dropdown-content');

    // Check if dropdown elements exist
    if (dropdownToggle && dropdownContent) {
        // Toggle dropdown when clicking the parameter icon
        dropdownToggle.addEventListener('click', function(e) {
            e.preventDefault();
            console.log("Dropdown toggle clicked");
            dropdownContent.classList.toggle('show');
        });

        // Close dropdown when clicking outside
        window.addEventListener('click', function(e) {
            if (!e.target.matches('.param-icon') && !e.target.matches('.fa-cog')) {
                if (dropdownContent.classList.contains('show')) {
                    dropdownContent.classList.remove('show');
                }
            }
        });
    }

    // Modal functionality
    const profileModal = document.getElementById('profileModal');
    const showProfileModal = document.getElementById('showProfileModal');
    const closeProfileModal = document.querySelector('.modal .close');
    
    // Show modal when clicking "Modifier profil"
    if (showProfileModal && profileModal) {
        showProfileModal.addEventListener('click', function(e) {
            e.preventDefault();
            console.log("Opening profile modal");
            profileModal.style.display = 'flex';
            document.body.style.overflow = 'hidden'; // Prevent scrolling
        });
    }

    // Close modal with X button
    if (closeProfileModal) {
        closeProfileModal.addEventListener('click', function() {
            profileModal.style.display = 'none';
            document.body.style.overflow = ''; // Restore scrolling
        });
    }

    // Close modal when clicking outside
    if (profileModal) {
        window.addEventListener('click', function(e) {
            if (e.target === profileModal) {
                profileModal.style.display = 'none';
                document.body.style.overflow = '';
            }
        });
    }

    // Profile Modal Functionality - DÉCLARATION UNIQUE DES VARIABLES
    const cancelProfileEdit = document.getElementById('cancelProfileEdit');
    const profileImageContainer = document.querySelector('.profile-image-container');
    const profileImageInput = document.getElementById('profileImageInput');
    const profileImagePreview = document.getElementById('profileImagePreview');

    // Close modal functionality
    function closeModal() {
        if (profileModal) {
            profileModal.style.display = 'none';
            document.body.style.overflow = ''; // Restore scrolling
            
            // Optional: reset form
            const profileForm = document.getElementById('profileForm');
            if (profileForm) profileForm.reset();
        }
    }

    // Close modal with Cancel button
    if (cancelProfileEdit) {
        cancelProfileEdit.addEventListener('click', closeModal);
    }

    // Close modal when clicking outside
    if (profileModal) {
        profileModal.addEventListener('click', function(e) {
            if (e.target === profileModal) {
                closeModal();
            }
        });
    }

    // Close modal with Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && profileModal && profileModal.style.display === 'flex') {
            closeModal();
        }
    });

    // Profile image upload
    if (profileImageContainer && profileImageInput && profileImagePreview) {
        profileImageContainer.addEventListener('click', function() {
            profileImageInput.click();
        });

        profileImageInput.addEventListener('change', function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    profileImagePreview.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // Active tab management
    const navLinks = document.querySelectorAll('.main-nav a');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Remove active class from all links
            navLinks.forEach(item => item.classList.remove('active'));
            // Add active class to clicked link
            this.classList.add('active');
        });
    });

    // Success message animation (if present)
    const successMessage = document.querySelector('.alert-success');
    if (successMessage) {
        setTimeout(() => {
            successMessage.classList.add('fade-out');
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 500);
        }, 5000);
    }
});