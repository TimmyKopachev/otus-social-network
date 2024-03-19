package org.otus.dzmitry.kapachou.highload.cache.jpa;

import org.otus.dzmitry.kapachou.highload.cache.exception.NonExistingEntityException;
import org.otus.dzmitry.kapachou.highload.cache.model.BasicId;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractCrudService<T extends BasicId> implements CrudService<T> {

    @Override
    public T save(T t) {
        return getRepository().save(t);
    }

    @Override
    public Iterable<T> saveAll(Collection<T> data) {
        return getRepository().saveAll(data);
    }

    @Override
    public T update(T t) {
        return getRepository().save(t);
    }

    @Override
    public T get(Long id) {
        Optional<T> optional = getRepository().findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new NonExistingEntityException(String.format("Can not find %s with id %o", this.getClass().getCanonicalName(), id));
    }

    @Override
    public void delete(Long id) {
        getRepository().deleteById(id);
    }

    @Override
    public Collection<T> findAll() {
        Iterable<T> entities = getRepository().findAll();
        List<T> list = new ArrayList<>();
        entities.forEach(list::add);
        return list;
    }


    protected abstract BaseRepository<T> getRepository();

}
