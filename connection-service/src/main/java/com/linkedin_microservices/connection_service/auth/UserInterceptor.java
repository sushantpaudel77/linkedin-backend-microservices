package com.linkedin_microservices.connection_service.auth;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request,
                                @Nonnull HttpServletResponse response,
                                @Nonnull Object handler, Exception ex) throws Exception {

        UserContextHolder.clear();
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {

        String userId = request.getHeader("X-User-Id");
        if (userId != null) {
            UserContextHolder.setCurrentUserId(Long.valueOf(userId));
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
