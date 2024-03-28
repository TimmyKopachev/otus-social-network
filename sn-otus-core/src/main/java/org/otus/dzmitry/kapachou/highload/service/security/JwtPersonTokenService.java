package org.otus.dzmitry.kapachou.highload.service.security;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.config.security.JwtTokenProcessor;
import org.otus.dzmitry.kapachou.highload.exception.TokenRefreshException;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.authentication.JwtPersonToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import static org.otus.dzmitry.kapachou.highload.config.security.JwtTokenProcessor.REFRESH_TOKEN_EXPIRATION_IN_DAYS;

@Service
@AllArgsConstructor
public class JwtPersonTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProcessor jwtTokenProcessor;

    public Optional<JwtPersonToken> findByPersonId(Long userId) {
        return refreshTokenRepository.findByPersonId(userId);
    }

    public Optional<JwtPersonToken> findByToken(String token) {
        return refreshTokenRepository.findByAccessToken(token);
    }

    @Transactional
    public JwtPersonToken createRefreshToken(Person person) {
        var generatedToken = generateRefreshToken(person);
        refreshTokenRepository.deleteByPersonId(person.getId());
        return refreshTokenRepository.save(generatedToken);
    }

    private JwtPersonToken generateRefreshToken(Person person) {
        var refreshTokenEntity = new JwtPersonToken();
        Instant issuedDate = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Instant expiredDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_IN_DAYS).toInstant(ZoneOffset.UTC);
        refreshTokenEntity.setIssuedDate(issuedDate);
        refreshTokenEntity.setExpirationDate(expiredDate);
        refreshTokenEntity.setPerson(person);
        refreshTokenEntity.setAccessToken(jwtTokenProcessor.generateToken(person));
        refreshTokenEntity.setRefreshToken(
                jwtTokenProcessor.generateRefreshToken(
                        Date.from(issuedDate), Date.from(expiredDate), person.getId().toString()));

        return refreshTokenEntity;
    }

    public boolean isTokenExpired(JwtPersonToken token) {
        if (token.getExpirationDate().isBefore(Instant.now())) {
            throw new TokenRefreshException(
                    token.getRefreshToken(), TokenRefreshException.MESSAGE_REFRESH_TOKEN_EXPIRED);
        }
        return false;
    }

}
