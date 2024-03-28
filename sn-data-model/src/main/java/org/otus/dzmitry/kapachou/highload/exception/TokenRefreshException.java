package org.otus.dzmitry.kapachou.highload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    public static final String MESSAGE_REFRESH_TOKEN_EXPIRED =
            "Refresh token was expired. Please make a new signin request";

    public TokenRefreshException(String token) {
        super(String.format("Failed for generate & save token<%s>.", token));
    }

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for token<%s>: %s", token, message));
    }
}
