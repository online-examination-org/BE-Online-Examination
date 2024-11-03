package com.team2.online_examination.aspects;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(authorize)")
    public void checkAuthorization(JoinPoint joinPoint, Authorize authorize) {
        String BearerToken = request.getHeader("Authorization");
        if (BearerToken == null || !BearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        }

        String token = BearerToken.substring(7);  // Remove "Bearer " prefix

        try {
            JwtPayload payload = jwtUtil.verifyToken(token);
            String userRole = (String) payload.getClaims().get("role");

            List<String> allowedRoles = Arrays.asList(authorize.roles());
            if (!allowedRoles.contains(userRole)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
            }
            // Set context
            UserContext.setJwtPayload(payload);

        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", e);
        }
    }
}
