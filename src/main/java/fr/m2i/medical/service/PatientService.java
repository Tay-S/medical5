package fr.m2i.medical.service;

import fr.m2i.medical.api.PatientAPIController;
import fr.m2i.medical.entities.PatientEntity;
import fr.m2i.medical.entities.VilleEntity;
import fr.m2i.medical.repositories.PatientRepository;
import fr.m2i.medical.repositories.VilleRepository;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;

@Service
public class PatientService {

    private PatientRepository pr;
    private VilleRepository vr;
    public PatientService( PatientRepository pr ){
        this.pr = pr;
    }

    public Iterable<PatientEntity> findAll() {
        return pr.findAll();
    }

    public PatientEntity findPatient(int id) {
        return pr.findById(id).get();
    }

    public void delete(int id) {
        pr.deleteById(id);
    }

    private void checkPatient( PatientEntity p ) throws InvalidObjectException {

        if( p.getNom().length() <= 2  ){
            throw new InvalidObjectException("Nom invalide");
        }

        if( p.getPrenom().length() <= 2  ){
            throw new InvalidObjectException("Prénom invalide");
        }

        if( p.getAdresse().length() <= 10  ){
            throw new InvalidObjectException("Adresse invalide");
        }
        Integer id = p.getVille().getId()  ;

        if(vr.findById(id).isEmpty()){
            throw new InvalidObjectException("Ville invalide");
        }

    }

    public void addPatient( PatientEntity p ) throws InvalidObjectException {
        checkPatient(p);
        pr.save(p);
    }

    public void editPatient( int id , PatientEntity p) throws InvalidObjectException {
        checkPatient(p);
        PatientEntity pExistante = pr.findById(id).get();

        pExistante.setDatenaissance( p.getDatenaissance() );
        pExistante.setNom( p.getNom() );
        pExistante.setPrenom(p.getPrenom());
        pExistante.setAdresse( p.getAdresse() );

        pr.save( pExistante );

    }
}