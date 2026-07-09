package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.ArbitroService;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminPartitaController {

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private ArbitroService arbitroService;






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

        // la squadra di casa e quella in trasferta devono essere diverse
        if (squadraCasaId.equals(squadraTrasfertaId)) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            model.addAttribute("errore", "La squadra di casa e quella in trasferta devono essere diverse");
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

        // una partita appena registrata e' sempre "da giocare" e senza risultato
        partita.setStato(StatoPartita.SCHEDULED);
        partita.setGoalsHome(null);
        partita.setGoalsAway(null);

        partitaService.save(partita);
        // dopo il salvataggio, nuova richiesta GET alla pagina del torneo (con la nuova partita in elenco)
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

        if (partita.getStato() == null) {
            model.addAttribute("partita", oldPartita);
            model.addAttribute("errore", "Lo stato e' obbligatorio");
            return "admin/partite/risultato";
        }

        if (partita.getStato() == StatoPartita.PLAYED) {

            // per una partita giocata servono entrambi i gol, non negativi
            if (partita.getGoalsHome() == null || partita.getGoalsAway() == null
                    || partita.getGoalsHome() < 0 || partita.getGoalsAway() < 0) {
                model.addAttribute("partita", oldPartita);
                model.addAttribute("errore", "Per una partita giocata servono i gol di entrambe le squadre (non negativi)");
                return "admin/partite/risultato";
            }

            oldPartita.setGoalsHome(partita.getGoalsHome());
            oldPartita.setGoalsAway(partita.getGoalsAway());

        } else {

            // partita riportata a "da giocare": il risultato viene azzerato
            oldPartita.setGoalsHome(null);
            oldPartita.setGoalsAway(null);
        }

        oldPartita.setStato(partita.getStato());

        partitaService.save(oldPartita);

        // dopo l'aggiornamento del risultato, nuova richiesta GET alla pagina della partita
        return "redirect:/partite/" + oldPartita.getId();
    }




    //METODO PER ELIMINARE UNA PARTITA
    @PostMapping("/admin/partite/{id}/delete")
    public String deletePartita(@PathVariable Long id) {


        Partita partita = this.partitaService.findById(id);

        partitaService.delete(partita);

        // dopo l'eliminazione, nuova richiesta GET all'elenco partite
        return "redirect:/partite";
    }


}