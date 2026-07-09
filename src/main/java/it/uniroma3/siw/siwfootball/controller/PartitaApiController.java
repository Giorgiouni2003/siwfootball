package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.dto.OpzioneDTO;
import it.uniroma3.siw.siwfootball.dto.PartitaDTO;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// API REST usata dal componente React "elenco partite con filtri"

//questo non è un controller normale (in cui ritorna i nomi di pagine) perché restituisce i dati in formato JSON
@RestController
public class PartitaApiController {

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;



    // restituisce l'elenco delle partite in formato JSON, applicando i filtri ricevuti.
    // torneoId, squadraId e stato sono tutti opzionali: se uno manca, quel filtro semplicemente non viene usato.
    @GetMapping("/api/partite")
    public List<PartitaDTO> partite(
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) Long squadraId,
            @RequestParam(required = false) StatoPartita stato) {

        // il service fa il vero filtro sul database; qui trasformiamo ogni Partita
        // (l'entita' JPA, con tutte le relazioni) in un PartitaDTO piu' semplice da mandare al frontend
        List<PartitaDTO> risultato = new ArrayList<>();
        for (Partita partita : this.partitaService.filtra(torneoId, squadraId, stato)) {
            risultato.add(PartitaDTO.from(partita));
        }
        return risultato;
    }


    // restituisce le liste di tornei e squadre esistenti, usate da React per riempire le due select dei filtri.
    // viene chiamato una volta sola quando la pagina si carica
    @GetMapping("/api/partite/opzioni-filtro")
    public Map<String, List<OpzioneDTO>> opzioniFiltro() {

        // per ogni torneo prendiamo solo id e nome (OpzioneDTO): a una select serve solo questo
        List<OpzioneDTO> tornei = new ArrayList<>();
        for (Torneo torneo : this.torneoService.findAll()) {
            tornei.add(new OpzioneDTO(torneo.getId(), torneo.getNome()));
        }

        // stesso discorso per le squadre
        List<OpzioneDTO> squadre = new ArrayList<>();
        for (Squadra squadra : this.squadraService.findAll()) {
            squadre.add(new OpzioneDTO(squadra.getId(), squadra.getNome()));
        }

        // le due liste vengono messe in un'unica mappa cosi' il frontend riceve i tornei e le squadre con una sola chiamata
        Map<String, List<OpzioneDTO>> opzioni = new HashMap<>();
        opzioni.put("tornei", tornei);
        opzioni.put("squadre", squadre);
        return opzioni;
    }
}
