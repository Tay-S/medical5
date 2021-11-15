package fr.m2i.medical.repositories;

import fr.m2i.medical.entities.PatientEntity;
import fr.m2i.medical.entities.VilleEntity;
import org.springframework.data.repository.CrudRepository;

public interface VilleRepository extends CrudRepository<VilleEntity, Integer> {
    Iterable<VilleEntity> findByNom(String nom ); // select * from patient where nom = :nom
}