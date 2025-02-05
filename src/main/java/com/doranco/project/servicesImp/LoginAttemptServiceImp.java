package com.doranco.project.servicesImp;

import com.doranco.project.services.LoginAttemptService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImp implements LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(10);
    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockTimeCache = new HashMap<>();

    public void loginFailed(String email) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int attempts = attemptsCache.getOrDefault(email, 0) + 1;
        attemptsCache.put(email, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(email, System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String email) {
        if (!lockTimeCache.containsKey(email)) {
            return false;
        }
        long lockTime = lockTimeCache.get(email);
        if (System.currentTimeMillis() - lockTime > LOCK_TIME_DURATION) {
            lockTimeCache.remove(email);
            attemptsCache.remove(email);
            return false;
        }
        return true;
    }

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
        lockTimeCache.remove(email);
    }
}
