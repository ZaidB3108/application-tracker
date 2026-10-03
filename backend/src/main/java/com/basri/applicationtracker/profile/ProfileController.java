package com.basri.applicationtracker.profile;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {
    private final UserAccountRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ProfileController(UserAccountRepository repository) { this.repository = repository; }
    @GetMapping public List<UserAccount> all() { return repository.findAll(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public UserAccount create(@Valid @RequestBody ProfileRequest request) {
        repository.findByEmailIgnoreCase(request.email()).ifPresent(user -> { throw new IllegalArgumentException("An account with that email already exists"); });
        return repository.save(new UserAccount(request.fullName().trim(), request.email().trim().toLowerCase(), passwordEncoder.encode(request.password())));
    }
}
