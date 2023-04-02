package ksl.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ksl.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenService {


    public void handleResponseHeader(HttpServletRequest request, HttpServletResponse response, UserPrincipal user) {
        response.addHeader(Constants.Headers.X_KM_AUTH_TOKEN, generateAccessToken(request, user));
        response.addHeader(Constants.Headers.X_KM_REFRESH_TOKEN, generateRefreshToken(request, user));
    }

    public String generateAccessToken(HttpServletRequest request, UserPrincipal user) {
        log.info("Generating Access Token for: {}", user);

        log.info("Generating Claims for Access Token.......");
        Map<String, Object> claims = new HashMap<>();
        generateClaims(request, user, claims);
        claims.put(Constants.Claims.TYPE, Constants.TokenType.ACCESS_TOKEN);
        log.info("Claim generated: {}", claims);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(generateExpiryDate(Constants.TokenType.ACCESS_TOKEN)))
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET.getBytes())
                .compact();
        log.info("Access token generated");
        return accessToken;
    }


    public String generateRefreshToken(HttpServletRequest request, UserPrincipal user) {
        log.info("Generating Refresh Token for: {}", user);

        log.info("Generating Claims for Refresh Token.......");
        Map<String, Object> claims = new HashMap<>();
        generateClaims(request, user, claims);
        claims.put(Constants.Claims.TYPE, Constants.TokenType.REFRESH_TOKEN);
        log.info("Claim generated: {}", claims);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(generateExpiryDate(Constants.TokenType.REFRESH_TOKEN)))
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET.getBytes())
                .compact();
        log.info("Refresh token generated");
        return refreshToken;
    }

    private void generateClaims(HttpServletRequest request, UserPrincipal user, Map<String, Object> claims) {
        claims.put(Constants.Claims.SUB, user.getUserName());
        claims.put(Constants.Claims.USERNAME, user.getUserName());
        claims.put(Constants.Claims.CREATED, Calendar.getInstance().getTimeInMillis());
        claims.put(Constants.Claims.RANDOM_VALUE, String.valueOf(System.currentTimeMillis()));
        claims.put(Constants.Claims.USER_AGENT, request.getHeader(Constants.Headers.USER_AGENT));
    }

    private Long generateExpiryDate(String type) {
        long expiry = type == Constants.TokenType.ACCESS_TOKEN ? 1800000L : 86400000L;
        return Calendar.getInstance().getTimeInMillis() + expiry;
    }


}
