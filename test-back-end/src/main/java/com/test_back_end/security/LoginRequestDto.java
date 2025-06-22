package com.test_back_end.security;

public record LoginRequestDto(String sessionId, String otp) { }
