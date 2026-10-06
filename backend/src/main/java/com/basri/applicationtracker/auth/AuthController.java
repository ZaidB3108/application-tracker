package com.basri.applicationtracker.auth;

import com.basri.applicationtracker.profile.UserAccount;
import com.basri.applicationtracker.profile.UserAccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final UserAccountRepository users;
    private final JwtService jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public AuthController(UserAccountRepository users, JwtService jwt) { this.users = users; this.jwt = jwt; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.findByEmailIgnoreCase(request.email()).isPresent()) throw new IllegalArgumentException("Email already registered");
        UserAccount user = users.save(new UserAccount(request.fullName().trim(), request.email().trim().toLowerCase(), encoder.encode(request.password())));
        return response(user);
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        UserAccount user = users.findByEmailIgnoreCase(request.email()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!encoder.matches(request.password(), user.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password");
        return response(user);
    }
    private AuthResponse response(UserAccount user) { return new AuthResponse(jwt.createToken(user.getId(), user.getEmail()), user.getId(), user.getFullName(), user.getEmail()); }
}
