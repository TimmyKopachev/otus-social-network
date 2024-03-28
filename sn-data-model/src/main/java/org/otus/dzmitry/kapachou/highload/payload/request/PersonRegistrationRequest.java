package org.otus.dzmitry.kapachou.highload.payload.request;

import lombok.Data;
import org.otus.dzmitry.kapachou.highload.model.Gender;

@Data
public class PersonRegistrationRequest {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Integer age;
    private String interests;
    private String role;
    private String city;
    private Gender gender;

}
