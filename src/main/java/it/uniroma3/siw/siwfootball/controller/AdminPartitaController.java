package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.ArbitroService;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String savePartita(@Valid @ModelAttribute Partita partita,
                              BindingResult bindingResult,
                              @RequestParam("torneo.id") Long torneoId,
                              @RequestParam("squadraDiCasa.id") Long squadraCasaId,
                              @RequestParam("squadraDiTrasferta.id") Long squadraTrasfertaId,
                              @RequestParam("arbitro.id") Long arbitroId,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            return "admin/partite/new";
        }

        Torneo torneo = torneoService.findById(torneoId);
        Squadra squadraDiCasa = squadraService.findById(squadraCasaId);
        Squadra squadraDiTrasferta = squadraService.findById(squadraTrasfertaId);
        Arbitro arbitro = arbitroService.findById(arbitroId);

        partita.setTorneo(torneo);
        partita.setSquadraDiCasa(squadraDiCasa);
        partita.setSquadraDiTrasferta(squadraDiTrasferta);
        partita.setArbitro(arbitro);
        partitaService.save(partita);
        return "redirect:/tornei/" + torneoId;
    }



    //METODI PER VISUALIZZARE E MODIFICARE LO STATO DI UNA PARTITA
    @GetMapping("/admin/partite/{id}/risultato")
    public String risultato(@PathVariable Long id, Model model) {

        Partita partita = this.partitaService.findById(id);

        model.addAttribute("partita", partita);

        return "admin/partite/risultato";
    }

    @PostMapping("/admin/partite/{id}/edit")
    public String risultatoPartita(@PathVariable Long id, @ModelAttribute Partita partita, Model model) {

        Partita oldPartita = this.partitaService.findById(id);

        if (partita.getStato() == null
                || partita.getGoalsHome() == null || partita.getGoalsAway() == null) {
            model.addAttribute("partita", oldPartita);
            model.addAttribute("errore", "Stato e risultato sono obbligatori");
            return "admin/partite/risultato";
        }

        oldPartita.setGoalsAway(partita.getGoalsAway());
        oldPartita.setGoalsHome(partita.getGoalsHome());
        oldPartita.setStato(partita.getStato());

        partitaService.save(oldPartita);

        return "redirect:/partite/" + oldPartita.getId();
    }

    //METODO PER ELIMINARE UNA PARTITA
    @PostMapping("/admin/partite/{id}/delete")
    public String deletePartita(@PathVariable Long id) {


        Partita partita = this.partitaService.findById(id);

        partitaService.delete(partita);

        return "redirect:/partite";
    }
}