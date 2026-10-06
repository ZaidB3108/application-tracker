package com.basri.applicationtracker.auth;
public record AuthResponse(String token, Long userId, String fullName, String email) { }
