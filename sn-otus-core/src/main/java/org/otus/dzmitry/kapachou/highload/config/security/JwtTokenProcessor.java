package org.otus.dzmitry.kapachou.highload.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.authentication.AuthenticatedPersonDetails;
import org.otus.dzmitry.kapachou.highload.model.authentication.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtTokenProcessor {

    private final String secret;

    public JwtTokenProcessor(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public static final ZoneId zoneId = ZoneId.systemDefault();
    public static final String SUBJECT_AUTH_REFRESH_TOKEN = "refresh_token";
    public static final Long REFRESH_TOKEN_EXPIRATION_IN_DAYS = 1L;
    public static final Long TOKEN_EXPIRATION_IN_MINUTES = 1L;

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isValidToken(String token) {
        return !isTokenExpired(token);
    }

    public Boolean isValidToken(String token, AuthenticatedPersonDetails personDetails) {
        String username = getUsernameFromToken(token);
        return Objects.equals(username, personDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(AuthenticatedPersonDetails personDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", personDetails.getRoleList());
        var person = personDetails.getPerson();
        return doGenerateToken(claims, personDetails.getUsername(), person.getId().toString());
    }

    public String generateToken(Person person) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", person.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return doGenerateToken(claims, person.getUsername(), person.getId().toString());
    }

    public String generateRefreshToken(Date issuedDate, Date expiredDate, String personId) {
        return Jwts.builder()
                .setSubject(SUBJECT_AUTH_REFRESH_TOKEN)
                .setAudience(personId)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, String userId) {
        Date issuedDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        Date expiredDate =
                Date.from(
                        LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_IN_MINUTES).toInstant(ZoneOffset.UTC));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(userId)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        Date date = getExpirationDateFromToken(token);
        Date now = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        return date == null || date.before(now);
    }
}
