package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.service.ArbitroService;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminPartitaController {

    private PartitaService partitaService;
    private TorneoService torneoService;
    private SquadraService squadraService;
    private ArbitroService arbitroService;

    public AdminPartitaController(PartitaService partitaService,
                                  TorneoService torneoService,
                                  SquadraService squadraService,
                                  ArbitroService arbitroService) {
        this.partitaService = partitaService;
        this.torneoService = torneoService;
        this.squadraService = squadraService;
        this.arbitroService = arbitroService;
    }

    @GetMapping("/admin/partite/new")
    public String newPartita(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("arbitri", arbitroService.findAll());
        return "admin/partite/new";
    }

    @PostMapping("/admin/partite/new")
    public String savePartita(@ModelAttribute Partita partita,
                              @RequestParam("torneo.id") Long torneoId,
                              @RequestParam("squadraDiCasa.id") Long squadraCasaId,
                              @RequestParam("squadraDiTrasferta.id") Long squadraTrasfertaId,
                              @RequestParam("arbitro.id") Long arbitroId) {
        partita.setTorneo(torneoService.findById(torneoId));
        partita.setSquadraDiCasa(squadraService.findById(squadraCasaId));
        partita.setSquadraDiTrasferta(squadraService.findById(squadraTrasfertaId));
        partita.setArbitro(arbitroService.findById(arbitroId));
        partitaService.save(partita);
        return "redirect:/tornei/" + torneoId;
    }
}