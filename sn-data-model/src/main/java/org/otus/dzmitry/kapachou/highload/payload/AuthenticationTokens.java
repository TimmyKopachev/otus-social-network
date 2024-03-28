package org.otus.dzmitry.kapachou.highload.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthenticationTokens implements Serializable {

    private String accessToken;
    private String refreshToken;
}
