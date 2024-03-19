package org.otus.dzmitry.kapachou.highload.cache.jpa;

import org.otus.dzmitry.kapachou.highload.cache.model.BasicId;

import java.util.Collection;

public interface CrudService <T extends BasicId> {

    T save(T t);

    Iterable<T> saveAll(Collection<T> data);

    T update(T t);

    T get(Long id);

    void delete(Long id);

    Collection<T> findAll();

}
