package org.otus.dzmitry.kapachou.highload.jpa;

import org.otus.dzmitry.kapachou.highload.model.BasicId;

import java.util.Collection;

public interface CrudService <T extends BasicId> {

    T save(T t);

    T update(T t);

    T get(Long id);

    void delete(Long id);

    Collection<T> findAll();

}
