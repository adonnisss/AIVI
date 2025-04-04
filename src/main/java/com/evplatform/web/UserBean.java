package com.evplatform.web;

import com.evplatform.service.UserService;
import com.evplatform.vao.User;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UserBean.class.getName());

    private User newUser = new User();
    private String selectedCarType;

    // Getters and setters
    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getSelectedCarType() {
        return selectedCarType;
    }

    public void setSelectedCarType(String selectedCarType) {
        this.selectedCarType = selectedCarType;
    }

    // Helper method to get all car types for dropdown
    public User.CarType[] getCarTypes() {
        return User.CarType.values();
    }

    // Method to register a new user
    public String registerUser() {
        try {
            // Convert the selected car type string to enum
            if (selectedCarType != null && !selectedCarType.isEmpty()) {
                newUser.setCarType(User.CarType.valueOf(selectedCarType));
            }

            // Set default balance if not provided
            if (newUser.getBalance() <= 0) {
                newUser.setBalance(50.0);
            }

            // Save the user
            int userId = UserService.getInstance().addUser(newUser);

            // Log success
            logger.info("User registered successfully with ID: " + userId);

            // Add success message for the UI
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User registered successfully"));

            // Reset form for next entry
            newUser = new User();
            selectedCarType = null;

            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            // Log error
            logger.severe("Error registering user: " + e.getMessage());

            // Add error message for the UI
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

            return null;
        }
    }

    // Method to print all users to console
    public void printUsersToConsole() {
        logger.info("===== ALL USERS =====");

        UserService.getInstance().getAllUsers().forEach(user -> {
            logger.info("User ID: " + user.getId() +
                    ", Name: " + user.getName() +
                    ", Email: " + user.getEmail() +
                    ", Car Type: " + user.getCarType() +
                    ", Balance: " + user.getBalance());
        });

        logger.info("=====================");
    }
}