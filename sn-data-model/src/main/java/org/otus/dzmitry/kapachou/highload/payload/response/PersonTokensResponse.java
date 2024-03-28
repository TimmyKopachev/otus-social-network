package org.otus.dzmitry.kapachou.highload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonTokensResponse {
    private String accessToken;
    private String refreshToken;
}