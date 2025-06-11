package com.test_back_end.dto;

public record PresignedUrlResponseDTO(
    String url,
    String filename
) {
}
