package com.example.orderorder.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {



    private String userId;
    private String displayName;
    private String firstName;
    private String lastName;
    private String user;

    public LoggedInUser(String userId, String user, String firstName, String lastName) {
        this.userId = userId;
        this.displayName = firstName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user = user;

    }



    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public interface getUid {
        String uid();

    }
}