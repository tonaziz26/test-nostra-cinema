package com.test_back_end.util;

import org.springframework.data.domain.Sort;

public class PaginationUtil {

    public static Sort.Direction getSortDirection(String direction) {
        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else {
            return Sort.Direction.DESC;
        }
    }
}
