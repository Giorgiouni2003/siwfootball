package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TorneoController {

    private TorneoService torneoService;
    private PartitaService partitaService;
    private SquadraService squadraService;



    public TorneoController(TorneoService torneoService, PartitaService partitaService,
                            SquadraService squadraService) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.squadraService = squadraService;
    }

    // UC1 [PUBLIC] - Visualizza elenco tornei con conteggio totale squadre nel sistema
    @GetMapping("/tornei")
    public String list(Model model) {
        List<Torneo> tornei = this.torneoService.findAll();
        model.addAttribute("tornei", tornei);
        return "tornei/list";
    }

    // UC2 [PUBLIC] - Visualizza dettaglio torneo: squadre partecipanti (+ conteggio),
    // calendario partite, classifica (MULTI-ENTITÀ: Torneo+Squadra+Partita)
    @GetMapping("/tornei/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);

        model.addAttribute("torneo", torneo);
        model.addAttribute("partite", this.partitaService.findByTorneo(torneo));
        model.addAttribute("classifica", this.partitaService.getClassifica(torneo));

        // squadre non ancora iscritte al torneo: servono all'admin per la tendina "iscrivi squadra"
        List<Squadra> squadreDisponibili = new ArrayList<>();
        for (Squadra squadra : this.squadraService.findAll()) {
            if (!torneo.getSquadre().contains(squadra)) {
                squadreDisponibili.add(squadra);
            }
        }
        model.addAttribute("squadreDisponibili", squadreDisponibili);

        return "tornei/show";

    }




}