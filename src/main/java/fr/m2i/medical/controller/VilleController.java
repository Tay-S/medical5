package fr.m2i.medical.controller;


import fr.m2i.medical.entities.VilleEntity;
import fr.m2i.medical.repositories.VilleRepository;
import fr.m2i.medical.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/ville")
public class VilleController {

    @Autowired
    private VilleService vs;

    private VilleRepository vr;

    // http://localhost:8080/ville
    @GetMapping(value = "")
    public String list(Model model, HttpServletRequest request){
        String search = request.getParameter("search");
        Iterable<VilleEntity> villas = vs.findAll(search);
        model.addAttribute( "search" , search );

        model.addAttribute( "error" , request.getParameter("error") );
        model.addAttribute( "success" , request.getParameter("success") );

        return listByPage(model, 1);
    }


    @GetMapping("/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<VilleEntity> page = vs.listAll(currentPage);
        int totalItems = page.getNumberOfElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        List<VilleEntity> listVilles = page.getContent();
        model.addAttribute("listVilles", listVilles);

        return "ville/list_ville";
    }



    @PostMapping(value = "/add")
    public String addPost( Model model, HttpServletRequest request){
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
            model.addAttribute("ville" , v );
            model.addAttribute("error" , e.getMessage() );
            return "ville/addEdit_ville";
        }
        return "redirect:/ville?success=true";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit( Model model , @PathVariable int id ){
        //... récupérer le ville à modifier et le passer à la vue
        model.addAttribute("ville" , vs.findVille(id) );
        return "ville/addEdit_ville";
    }

    @PostMapping(value = "/edit/{id}")
    public String editPost( Model model, HttpServletRequest request, @PathVariable int id) throws InvalidObjectException {
        //... récupérer la ville envoyé depuis le form et enregistrer en bd
        if( request.getMethod().equals("POST") ){
            String nom = request.getParameter("nom");
            String pays = request.getParameter("pays");
            String codePostal = request.getParameter("codePostal");

            // Préparation de l'entité à sauvegarder
            VilleEntity v = new VilleEntity( nom , codePostal , pays );

            // Enregistrement en utilisant la couche service qui gère déjà nos contraintes
            try{
                vs.editVille( id , v );
            }catch( Exception e ){
                v.setId(  -1 ); // hack
                System.out.println( e.getMessage() );
                model.addAttribute("ville" , v );
                model.addAttribute("error" , e.getMessage() );
                return "ville/addEdit_ville";
            }
            return "redirect:/ville?success=true";
        }else{
            try{
                model.addAttribute("ville" , vs.findVille( id ) );
            }catch ( NoSuchElementException e ){
                return "redirect:/ville?error=Ville%20introuvalble";
            }

            return "ville/addEdit_ville";
        }
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable int id) {
        String message = "?success=true";
        try{
            vs.delete(id);
        }catch ( Exception e ){
            message = "?error=Ville%20introuvalble";
        }
        return "redirect:/ville"+message;
    }

    public VilleService getVs() {
        return vs;
    }

    public void setVs(VilleService vs) {
        this.vs = vs;
    }

}