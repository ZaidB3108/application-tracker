package com.basri.applicationtracker.profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ProfileRequest(@NotBlank String fullName, @Email @NotBlank String email, @NotBlank @Size(min = 8) String password) { }
