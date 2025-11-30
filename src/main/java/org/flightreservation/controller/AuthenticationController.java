package org.flightreservation.controller;

import org.flightreservation.data.UserDAO;
import org.flightreservation.entity.User;

public class AuthenticationController {
    private UserDAO userDAO;

    public AuthenticationController() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean registerUser(User user) {
        // Check if user already exists
        User existingUser = userDAO.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }
        // Only allow CUSTOMER role for registration
        if (!"CUSTOMER".equals(user.getRole())) {
            return false;
        }
        return userDAO.save(user);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}