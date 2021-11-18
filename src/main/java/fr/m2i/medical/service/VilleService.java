package fr.m2i.medical.service;


import fr.m2i.medical.entities.VilleEntity;
import fr.m2i.medical.repositories.VilleRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class VilleService {

    private VilleRepository vr;

    public VilleService( VilleRepository vr ){
        this.vr = vr;
    }

    public Iterable<VilleEntity> findAll() {
        return vr.findAll();
    }

    public Page<VilleEntity> listAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return vr.findAll(pageable);
    }

    public Iterable<VilleEntity> findAll(String search) {
        if( search != null && search.length() > 0 ){
            return vr.findByNomContains(search);
        }
        return vr.findAll();
    }

    private void checkVille( VilleEntity v ) throws InvalidObjectException {

        if( v.getNom().length() <= 2  ){
            throw new InvalidObjectException("Nom de ville invalide");
        }

        if( v.getPays().length() <= 3  ){
            throw new InvalidObjectException("Nom du pays invalide");
        }

    }

    public VilleEntity findVille(int id) {
        return (VilleEntity) vr.findById(id).get();
    }

    public void addVille( VilleEntity v ) throws InvalidObjectException {
        checkVille(v);
        vr.save(v);
    }

    public void delete(int id) throws ConstraintViolationException {
        vr.deleteById(id);
    }

    public void editVille( int id , VilleEntity v) throws InvalidObjectException , NoSuchElementException {
        checkVille(v);
        try{
            VilleEntity vExistante = (VilleEntity) vr.findById(id).get();

            vExistante.setCodePostal( v.getCodePostal() );
            vExistante.setNom( v.getNom() );
            vExistante.setPays( v.getPays() );
            vr.save( vExistante );

        }catch ( NoSuchElementException e ){
            throw e;
        }

    }
}