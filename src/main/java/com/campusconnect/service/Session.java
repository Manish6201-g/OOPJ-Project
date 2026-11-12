package com.campusconnect.service;
import com.campusconnect.model.User;
public final class Session {
    private static User currentUser;
    private Session() { }
    public static void setCurrentUser(User user) { currentUser = user; }
    public static User getCurrentUser() { return currentUser; }
    public static void clearSession() { currentUser = null; }
}
