package fr.m2i.medical.repositories;

import fr.m2i.medical.entities.VilleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface VilleRepository<T> extends CrudRepository<VilleEntity, Integer> {
    Iterable<VilleEntity> findByNomContains(String search);

    Page<T> findAll(Pageable pageable);
}