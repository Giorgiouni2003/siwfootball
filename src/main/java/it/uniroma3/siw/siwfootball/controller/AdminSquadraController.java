package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminSquadraController {

    private SquadraService squadraService;

    public AdminSquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }





    //METODI PER CREARE UNA NUOVA SQUADRA
    @GetMapping("/admin/squadre/new")
    public String newSquadra(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/squadre/new";
    }

    @PostMapping("/admin/squadre/new")
    public String saveSquadra(@Valid @ModelAttribute Squadra squadra, BindingResult bindingResult) {

        // la squadra non deve essere gia' presente
        if (this.squadraService.findByNome(squadra.getNome()) != null) {
            bindingResult.rejectValue("nome", "duplicato", "Squadra già esistente");
        }

        if (bindingResult.hasErrors()) {
            return "admin/squadre/new";
        }

        squadraService.save(squadra);
        // dopo il salvataggio, nuova richiesta GET all'elenco squadre
        return "redirect:/squadre";
    }





    //METODI PER MODIFICARE UNA SQUADRA

    @GetMapping("/admin/squadre/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        Squadra squadra = squadraService.findById(id);

        model.addAttribute("squadra", squadra);

        return "admin/squadre/edit";
    }

    @PostMapping("/admin/squadre/{id}/edit")
    public String editSquadra(@PathVariable Long id, @Valid @ModelAttribute Squadra squadra, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/squadre/edit";
        }

        Squadra oldSquadra = squadraService.findById(id);

        oldSquadra.setNome(squadra.getNome());
        oldSquadra.setCitta(squadra.getCitta());
        oldSquadra.setAnnoDiFondazione(squadra.getAnnoDiFondazione());

        squadraService.save(oldSquadra);

        // dopo la modifica, nuova richiesta GET alla pagina della squadra aggiornata
        return "redirect:/squadre/" + oldSquadra.getId();
    }




    //METODO PER ELIMINARE UNA SQUADRA
    //il service si occupa anche di eliminare partite e iscrizioni ai tornei collegate
    @PostMapping("/admin/squadre/{id}/delete")
    public String deleteSquadra(@PathVariable Long id) {

        squadraService.deleteById(id);

        // dopo l'eliminazione, nuova richiesta GET all'elenco squadre
        return "redirect:/squadre";
    }



}