package com.linkedin_microservices.api_gateway.filters;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            log.info("Login request: {}", exchange.getRequest().getURI());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("Authorization token header not found or invalid!!");
                return exchange.getResponse().setComplete();
            }

            // Extract token after "Bearer "
            final String token = tokenHeader.substring("Bearer ".length()).trim();

            String userIdFromToken;
            try {
                userIdFromToken = jwtService.getUserIdFromToken(token);
            } catch (JwtException exception) {
                log.error("Invalid JWT token: {}", exception.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(req -> req.header("X-User-Id", userIdFromToken))
                    .build();

            return chain.filter(modifiedExchange);
        };
    }

    public static class Config {

    }
}
