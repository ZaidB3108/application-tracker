package com.basri.applicationtracker.company;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "companies")
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
    private String website;
    private String location;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;
    protected Company() { }
    Company(String name, String website, String location) { this.name = name; this.website = website; this.location = location; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getWebsite() { return website; }
    public String getLocation() { return location; }
}
