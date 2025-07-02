package com.test_back_end.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {
    private final OrRequestMatcher skipMatcher;
    private final OrRequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<RequestMatcher> pathsToSkip, List<RequestMatcher> pathsToProcess) {
        this.skipMatcher = new OrRequestMatcher(pathsToSkip);
        this.processingMatcher = new OrRequestMatcher(pathsToProcess);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (skipMatcher.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request);
    }
}

