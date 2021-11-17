package fr.m2i.medical.controller;


import fr.m2i.medical.entities.PatientEntity;
import fr.m2i.medical.entities.VilleEntity;
import fr.m2i.medical.service.PatientService;
import fr.m2i.medical.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InvalidObjectException;
import java.sql.Date;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService ps;

    @Autowired
    private VilleService vs;

    // http://localhost:8080/patient
    @GetMapping(value = "")
    public String list( Model model ){
        model.addAttribute("patients" , ps.findAll() );
        return "list_patient";
    }

    // http://localhost:8080/patient/add
    @GetMapping(value = "/add")
    public String add( Model model ){
        model.addAttribute("villes" , vs.findAll() );
        return "add_edit";
    }

    @PostMapping(value = "/add")
    public String addPost( HttpServletRequest request){
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String naissance = request.getParameter("naissance");
        String adresse = request.getParameter("adresse");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        int ville = Integer.parseInt(request.getParameter("ville"));

        VilleEntity v = new VilleEntity();
        v.setId(ville);
        PatientEntity p = new PatientEntity( 0 , nom , prenom , Date.valueOf( naissance ) , email , telephone , adresse , v );

        try{
            ps.addPatient( p );
        }catch( Exception e ){
            System.out.println( e.getMessage() );
        }
        return "redirect:/patient";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit( Model model , @PathVariable int id ){
        model.addAttribute("villes" , vs.findAll() );
        model.addAttribute("patient" , ps.findPatient(id) );
        return "add_edit";
    }

    @PostMapping(value = "/edit/{id}")
    public String editPost( PatientEntity p, HttpServletRequest request, @PathVariable int id) throws InvalidObjectException {
        p.setNom(request.getParameter("nom"));
        p.setPrenom(request.getParameter("prenom"));
        p.setEmail(request.getParameter("email"));
        p.setAdresse(request.getParameter("adresse"));
        p.setTelephone(request.getParameter("telephone"));
        p.setDateNaissance(Date.valueOf(request.getParameter("naissance")));

        VilleEntity v = vs.findVille(Integer.parseInt(request.getParameter("ville")));
        p.setVille(v);
        ps.editPatient(id, p);
        return "redirect:/patient";
    }

    public PatientService getPs() {
        return ps;
    }

    public void setPs(PatientService ps) {
        this.ps = ps;
    }

}