package com.basri.applicationtracker.company;
import jakarta.validation.constraints.NotBlank;
public record CompanyRequest(@NotBlank String name, String website, String location) { }
