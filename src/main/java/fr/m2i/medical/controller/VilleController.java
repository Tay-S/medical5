package fr.m2i.medical.controller;


import fr.m2i.medical.entities.VilleEntity;
import fr.m2i.medical.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.InvalidObjectException;

@Controller
@RequestMapping("/ville")
public class VilleController {

    @Autowired
    private VilleService vs;

    // http://localhost:8080/ville
    @GetMapping(value = "")
    public String list( Model model ){
        model.addAttribute("villes" , vs.findAll() );
        return "ville/list_ville";
    }



    @PostMapping(value = "/add")
    public String addPost( HttpServletRequest request){
        // Récupération des paramètres envoyés en POST
        String nom = request.getParameter("nom");
        String pays = request.getParameter("pays");
        String codePostal = request.getParameter("codePostal");

        // Préparation de l'entité à sauvegarder
        VilleEntity v = new VilleEntity( 0 , nom , codePostal, pays );

        // Enregistrement en utilisant la couche service qui gère déjà nos contraintes
        try{
            vs.addVille( v );
        }catch( Exception e ){
            System.out.println( e.getMessage() );
        }
        return "redirect:/ville";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit( Model model , @PathVariable int id ){
        //... récupérer le ville à modifier et le passer à la vue
        model.addAttribute("ville" , vs.findVille(id) );
        return "ville/addEdit_ville";
    }

    @PostMapping(value = "/edit/{id}")
    public String editPost( VilleEntity v, HttpServletRequest request, @PathVariable int id) throws InvalidObjectException {
        //... récupérer la ville envoyé depuis le form et enregistrer en bd
        v.setNom(request.getParameter("nom"));
        v.setPays(request.getParameter("pays"));
        v.setCodePostal(request.getParameter("codePostal"));

        vs.editVille(id, v);
        return "redirect:/ville";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable int id) {
        vs.delete(id);
        return "redirect:/ville";
    }

    public VilleService getVs() {
        return vs;
    }

    public void setVs(VilleService vs) {
        this.vs = vs;
    }

}