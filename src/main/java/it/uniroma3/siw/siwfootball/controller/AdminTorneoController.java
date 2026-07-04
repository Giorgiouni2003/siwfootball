package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminTorneoController {

    // dipendenza da TorneoService via costruttore

    private TorneoService torneoService;

    public AdminTorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    // METODI PER CREARE UN NUOVO TORNEO
    @GetMapping("/admin/tornei/new")
    public String newTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());

        return "admin/tornei/new";
    }


    @PostMapping("/admin/tornei/new")
    public String newTorneo(@ModelAttribute Torneo torneo) {
        torneoService.save(torneo);
        return "redirect:/tornei/" + torneo.getId();
    }

}
