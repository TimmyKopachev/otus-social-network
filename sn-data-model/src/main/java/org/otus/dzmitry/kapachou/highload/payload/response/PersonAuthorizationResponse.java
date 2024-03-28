package org.otus.dzmitry.kapachou.highload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonAuthorizationResponse {

    private String username;
    private String accessToken;
    private String refreshToken;

}
