package org.otus.dzmitry.kapachou.highload.service.security;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.authentication.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByName(String name);

}
