package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.service.GiocatoreService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminGiocatoreController {

    private GiocatoreService giocatoreService;
    private SquadraService squadraService;

    public AdminGiocatoreController(GiocatoreService giocatoreService,
                                    SquadraService squadraService) {
        this.giocatoreService = giocatoreService;
        this.squadraService = squadraService;
    }

    //METODI PER INSERIRE UN NUOVO GIOCATORE

    @GetMapping("/admin/giocatori/new")
    public String newGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", squadraService.findAll());
        return "admin/giocatori/new";
    }

    @PostMapping("/admin/giocatori/new")
    public String saveGiocatore(@ModelAttribute Giocatore giocatore,
                                 @RequestParam("squadra.id") Long squadraId) {
        Squadra squadra = squadraService.findById(squadraId);
        giocatore.setSquadra(squadra);
        giocatoreService.save(giocatore);
        return "redirect:/squadre";
    }
}