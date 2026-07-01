package it.uniroma3.siw.siwfootball.controller;

import org.springframework.ui.Model;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SquadraController {
    private SquadraService squadraService;


    public SquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }


    @GetMapping("/squadre")
    public String squadre(Model model) {
        List<Squadra> lista = this.squadraService.findAll();
        model.addAttribute("squadre",lista);

        return "squadre/list";
    }

    @GetMapping("/squadre/{id}")
    public String squadra(Model model, @PathVariable Long id) {
        model.addAttribute("squadra", this.squadraService.findById(id));

        return "squadre/show";
    }
}
