package com.basri.applicationtracker.application;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(@NotBlank String status) { }
