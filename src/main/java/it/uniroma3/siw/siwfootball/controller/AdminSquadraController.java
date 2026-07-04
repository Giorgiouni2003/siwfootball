package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String saveSquadra(@ModelAttribute Squadra squadra) {
        squadraService.save(squadra);
        return "redirect:/squadre";
    }
}