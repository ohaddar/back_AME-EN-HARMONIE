package com.doranco.project.services;

public interface LoginAttemptService {
    public void loginFailed(String email);
    public boolean isBlocked(String email);
    public void loginSucceeded(String email);
}
