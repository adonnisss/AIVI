package com.evplatform.web;

import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import com.evplatform.vao.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named
@SessionScoped
public class CrudBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(CrudBean.class.getName());

    @EJB
    private UserServiceInterface userService;

    @EJB
    private ProviderServiceInterface providerService;

    @EJB
    private ChargingStationServiceInterface stationService;

    // Current objects for editing/viewing
    private User currentUser = new User();
    private Provider currentProvider = new Provider();
    private ChargingStation currentStation = new ChargingStation();

    // Selected car type for user
    private String selectedCarType;

    // Selected provider for station
    private int selectedProviderId;

    // Selected status for station
    private String selectedStationStatus;

    // Flag to indicate if we're in edit mode
    private boolean editMode = false;

    // Confirmation flags
    private boolean showDeleteConfirmation = false;
    private Object itemToDelete;

    // Getters and setters for all fields
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Provider getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(Provider currentProvider) {
        this.currentProvider = currentProvider;
    }

    public ChargingStation getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(ChargingStation currentStation) {
        this.currentStation = currentStation;
    }

    public String getSelectedCarType() {
        return selectedCarType;
    }

    public void setSelectedCarType(String selectedCarType) {
        this.selectedCarType = selectedCarType;
    }

    public int getSelectedProviderId() {
        return selectedProviderId;
    }

    public void setSelectedProviderId(int selectedProviderId) {
        this.selectedProviderId = selectedProviderId;
    }

    public String getSelectedStationStatus() {
        return selectedStationStatus;
    }

    public void setSelectedStationStatus(String selectedStationStatus) {
        this.selectedStationStatus = selectedStationStatus;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isShowDeleteConfirmation() {
        return showDeleteConfirmation;
    }

    public void setShowDeleteConfirmation(boolean showDeleteConfirmation) {
        this.showDeleteConfirmation = showDeleteConfirmation;
    }

    public Object getItemToDelete() {
        return itemToDelete;
    }

    public void setItemToDelete(Object itemToDelete) {
        this.itemToDelete = itemToDelete;
    }

    // Helper methods for dropdown options
    public User.CarType[] getCarTypes() {
        return User.CarType.values();
    }

    public List<Provider> getAllProviders() {
        return providerService.getAllProviders();
    }

    public ChargingStation.ChargingStationStatus[] getStationStatuses() {
        return ChargingStation.ChargingStationStatus.values();
    }

    // Data retrieval methods
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<ChargingStation> getAllStations() {
        return stationService.getAllChargingStations();
    }

    // User CRUD operations
    public String prepareNewUser() {
        currentUser = new User();
        selectedCarType = null;
        editMode = false;
        return "/views/users/edit.xhtml?faces-redirect=true";
    }

    public String prepareEditUser(User user) {
        currentUser = user;
        selectedCarType = user.getCarType() != null ? user.getCarType().name() : null;
        editMode = true;
        return "/views/users/edit.xhtml?faces-redirect=true";
    }

    public String saveUser() {
        try {
            if (selectedCarType != null && !selectedCarType.isEmpty()) {
                currentUser.setCarType(User.CarType.valueOf(selectedCarType));
            }

            if (editMode) {
                userService.updateUser(currentUser);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated successfully");
            } else {
                userService.addUser(currentUser);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "User created successfully");
            }

            currentUser = new User();
            selectedCarType = null;
            editMode = false;

            return "/views/users/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error saving user: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            return null;
        }
    }

    public void confirmDeleteUser(User user) {
        itemToDelete = user;
        showDeleteConfirmation = true;
    }

    public String deleteUser() {
        try {
            User user = (User) itemToDelete;
            userService.deleteUser(user.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Success", "User deleted successfully");
            showDeleteConfirmation = false;
            return "/views/users/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error deleting user: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            showDeleteConfirmation = false;
            return null;
        }
    }

    public String cancelDelete() {
        showDeleteConfirmation = false;
        itemToDelete = null;
        return null;
    }

    // Provider CRUD operations
    public String prepareNewProvider() {
        currentProvider = new Provider();
        editMode = false;
        return "/views/providers/edit.xhtml?faces-redirect=true";
    }

    public String prepareEditProvider(Provider provider) {
        currentProvider = provider;
        editMode = true;
        return "/views/providers/edit.xhtml?faces-redirect=true";
    }

    public String saveProvider() {
        try {
            if (editMode) {
                providerService.updateProvider(currentProvider);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Provider updated successfully");
            } else {
                providerService.addProvider(currentProvider);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Provider created successfully");
            }

            currentProvider = new Provider();
            editMode = false;

            return "/views/providers/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error saving provider: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            return null;
        }
    }

    public void confirmDeleteProvider(Provider provider) {
        itemToDelete = provider;
        showDeleteConfirmation = true;
    }

    public String deleteProvider() {
        try {
            Provider provider = (Provider) itemToDelete;
            providerService.deleteProvider(provider.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Success", "Provider deleted successfully");
            showDeleteConfirmation = false;
            return "/views/providers/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error deleting provider: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            showDeleteConfirmation = false;
            return null;
        }
    }

    // ChargingStation CRUD operations
    public String prepareNewStation() {
        currentStation = new ChargingStation();
        selectedProviderId = 0;
        selectedStationStatus = null;
        editMode = false;
        return "/views/stations/edit.xhtml?faces-redirect=true";
    }

    public String prepareEditStation(ChargingStation station) {
        currentStation = station;
        selectedProviderId = station.getProvider() != null ? station.getProvider().getId() : 0;
        selectedStationStatus = station.getStatus() != null ? station.getStatus().name() : null;
        editMode = true;
        return "/views/stations/edit.xhtml?faces-redirect=true";
    }

    public String saveStation() {
        try {
            if (selectedProviderId > 0) {
                Provider provider = providerService.getProviderById(selectedProviderId);
                currentStation.setProvider(provider);
            }

            if (selectedStationStatus != null && !selectedStationStatus.isEmpty()) {
                currentStation.setStatus(ChargingStation.ChargingStationStatus.valueOf(selectedStationStatus));
            }

            if (editMode) {
                stationService.updateChargingStation(currentStation);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Charging station updated successfully");
            } else {
                stationService.addChargingStation(currentStation);
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Charging station created successfully");
            }

            currentStation = new ChargingStation();
            selectedProviderId = 0;
            selectedStationStatus = null;
            editMode = false;

            return "/views/stations/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error saving charging station: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            return null;
        }
    }

    public void confirmDeleteStation(ChargingStation station) {
        itemToDelete = station;
        showDeleteConfirmation = true;
    }

    public String deleteStation() {
        try {
            ChargingStation station = (ChargingStation) itemToDelete;
            stationService.deleteChargingStation(station.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Success", "Charging station deleted successfully");
            showDeleteConfirmation = false;
            return "/views/stations/list.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Error deleting charging station: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            showDeleteConfirmation = false;
            return null;
        }
    }

    // Helper method for adding faces messages
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
}