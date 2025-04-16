package com.doranco.project.services;

public interface LoginAttemptService {
     void loginFailed(String email);
     boolean isBlocked(String email);
     void loginSucceeded(String email);
}
