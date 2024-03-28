package org.otus.dzmitry.kapachou.highload.payload.request;

import lombok.Data;

@Data
public class PersonSingInRequest {

    private String username;
    private String password;
}
