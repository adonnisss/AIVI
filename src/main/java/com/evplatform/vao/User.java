package com.evplatform.vao;

import java.util.Objects;

/**
 * Value Access Object (VAO) representing a user of electric charging stations.
 * Contains user data without business logic.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private double balance;
    private CarType carType;

    /**
     * Enum representing different car types for compatibility checking
     */
    public enum CarType {
        SEDAN,        // Regular passenger car
        SUV,          // Sport utility vehicle
        COMPACT,      // Compact car
        SPORTS,       // Sports car
        TRUCK,        // Light truck
        VAN,          // Van or minivan
        LUXURY        // Luxury car
    }

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Parameterized constructor
     *
     * @param id User's unique identifier
     * @param name User's name
     * @param email User's email address
     * @param balance User's account balance
     * @param carType Type of car the user owns
     */
    public User(int id, String name, String email, double balance, CarType carType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.carType = carType;
    }

    /**
     * Get user ID
     * @return The unique identifier of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Set user ID
     * @param id The unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get user name
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Set user name
     * @param name The name to set
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.name = name;
    }

    /**
     * Get user email
     * @return The email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email
     * @param email The email to set
     * @throws IllegalArgumentException if email is null, empty, or invalid
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    /**
     * Get user balance
     * @return The current balance of the user
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set user balance
     * @param balance The balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Add funds to user balance
     * @param amount The amount to add
     * @throws IllegalArgumentException if amount is negative
     */
    public void addFunds(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount");
        }
        this.balance += amount;
    }

    /**
     * Deduct funds from user balance
     * @param amount The amount to deduct
     * @return true if deduction was successful, false if insufficient funds
     * @throws IllegalArgumentException if amount is negative
     */
    public boolean deductFunds(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deduct negative amount");
        }

        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Get user car type
     * @return The type of car the user owns
     */
    public CarType getCarType() {
        return carType;
    }

    /**
     * Set user car type
     * @param carType The car type to set
     */
    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                ", carType=" + carType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}