package com.basri.applicationtracker.company;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/companies") @CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {
    private final CompanyRepository repository;
    public CompanyController(CompanyRepository repository) { this.repository = repository; }
    @GetMapping public List<Company> all() { return repository.findAll(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Company create(@Valid @RequestBody CompanyRequest request) {
        return repository.findByNameIgnoreCase(request.name()).orElseGet(() -> repository.save(new Company(request.name().trim(), request.website(), request.location())));
    }
}
