package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Commento;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Utente;
import it.uniroma3.siw.siwfootball.service.CommentoService;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentoController {

    private CommentoService commentoService;
    private PartitaService partitaService;
    private UtenteService utenteService;

    public CommentoController(CommentoService commentoService,
                               PartitaService partitaService,
                               UtenteService utenteService) {
        this.commentoService = commentoService;
        this.partitaService = partitaService;
        this.utenteService = utenteService;
    }

    @PostMapping("/partite/{id}/commenti")
    public String saveCommento(@PathVariable Long id, @Valid @ModelAttribute Commento commento,
                                BindingResult bindingResult, Authentication authentication, Model model) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        if (isAdmin) {
            return "redirect:/partite/" + id;
        }

        Partita partita = partitaService.findById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("partita", partita);
            model.addAttribute("commenti", commentoService.findByPartitaId(id));
            return "partite/show";
        }

        Utente utente = utenteService.findByUsername(authentication.getName());

        commento.setPartita(partita);
        commento.setUtente(utente);

        commentoService.save(commento);
        return "redirect:/partite/" + id;
    }


    @GetMapping("/commenti/{id}/edit")
    public String edit(@PathVariable Long id, Model model, Authentication authentication) {
        Commento commento = commentoService.findById(id);

        //prendo l'utente loggato
        Utente utente = utenteService.findByUsername(authentication.getName());


        //verifico che l'utente loggsto sia lo stesso che ha scritto il commento
        if(commento.getUtente().getUsername().equals(utente.getUsername())){
            model.addAttribute("commento", commento);
            return "commenti/edit";
        }

        return "redirect:/partite/" + commento.getPartita().getId();


    }


    @PostMapping("/commenti/{id}/edit")
    public String editCommento(@PathVariable Long id,
                               @Valid @ModelAttribute Commento commento,
                               BindingResult bindingResult,
                               Authentication authentication) {

        // recupero il commento esistente dal db: quello bindato dal form non ha utente/partita popolati
        Commento esistente = commentoService.findById(id);
        Utente utente = utenteService.findByUsername(authentication.getName());

        //verifico che l'utente loggato sia lo stesso che ha scritto il commento
        if(!esistente.getUtente().getUsername().equals(utente.getUsername())){
            return "redirect:/partite/" + esistente.getPartita().getId();
        }

        if (bindingResult.hasErrors()) {
            commento.setPartita(esistente.getPartita());
            return "commenti/edit";
        }

        esistente.setTesto(commento.getTesto());
        commentoService.save(esistente);

        return "redirect:/partite/" + esistente.getPartita().getId();

    }
}
