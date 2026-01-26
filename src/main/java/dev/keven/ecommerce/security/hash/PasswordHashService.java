package dev.keven.ecommerce.security.hash;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashService {

    private final PasswordEncoder passwordEncoder;

    public PasswordHashService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
