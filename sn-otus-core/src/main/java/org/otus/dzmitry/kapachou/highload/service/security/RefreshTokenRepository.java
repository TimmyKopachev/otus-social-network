package org.otus.dzmitry.kapachou.highload.service.security;


import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.authentication.JwtPersonToken;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends BaseRepository<JwtPersonToken> {

    Optional<JwtPersonToken> findByPersonId(Long id);

    Optional<JwtPersonToken> findByAccessToken(String id);

    @Modifying
    void deleteByPersonId(Long personId);

}
