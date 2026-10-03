package com.basri.applicationtracker.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record InterviewRequest(
    @NotBlank String interviewType,
    @NotNull LocalDateTime interviewDate,
    String interviewerName,
    String result,
    String notes
) { }
