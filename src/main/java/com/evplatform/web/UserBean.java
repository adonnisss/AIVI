package com.evplatform.web;

import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UserBean.class.getName());

    @EJB
    private UserServiceInterface userService;

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
            if (selectedCarType != null && !selectedCarType.isEmpty()) {
                newUser.setCarType(User.CarType.valueOf(selectedCarType));
            }

            if (newUser.getBalance() <= 0) {
                newUser.setBalance(50.0);
            }

            int userId = userService.addUser(newUser); // Use injected service

            logger.info("User registered successfully with ID: " + userId);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User registered successfully"));

            newUser = new User();
            selectedCarType = null;

            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error registering user: " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

            return null;
        }
    }

    // Method to print all users to console
    public void printUsersToConsole() {
        logger.info("===== ALL USERS =====");

        userService.getAllUsers().forEach(user -> { // Use injected service
            logger.info("User ID: " + user.getId() +
                    ", Name: " + user.getName() +
                    ", Email: " + user.getEmail() +
                    ", Car Type: " + user.getCarType() +
                    ", Balance: " + user.getBalance());
        });

        logger.info("=====================");
    }
}