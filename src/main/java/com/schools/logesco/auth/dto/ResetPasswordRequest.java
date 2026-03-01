package com.schools.logesco.auth.dto;

public record ResetPasswordRequest(String token, String newPassword) {}
