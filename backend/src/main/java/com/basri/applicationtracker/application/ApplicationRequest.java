package com.basri.applicationtracker.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** Request payload mirrors the pre-existing applications table. */
public record ApplicationRequest(
    @NotNull Long userId,
    @NotNull Long companyId,
    @NotBlank String roleTitle,
    String jobType,
    String workMode,
    Integer salaryMin,
    Integer salaryMax,
    String jobPostingUrl,
    String source,
    LocalDate appliedDate,
    @NotBlank String status,
    String notes
) { }
