package com.test_back_end.dto;

import java.util.List;

public record PageResultDTO<T>(
        List<T> results,
        Integer page,
        Long elements) { }
