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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// API REST usata dal componente React "elenco partite con filtri"
@RestController
public class PartitaApiController {

    private final PartitaService partitaService;
    private final TorneoService torneoService;
    private final SquadraService squadraService;

    public PartitaApiController(PartitaService partitaService, TorneoService torneoService, SquadraService squadraService) {
        this.partitaService = partitaService;
        this.torneoService = torneoService;
        this.squadraService = squadraService;
    }

    // GET /api/partite?torneoId=1&squadraId=2&stato=PLAYED (tutti i parametri sono opzionali)
    @GetMapping("/api/partite")
    public List<PartitaDTO> partite(
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) Long squadraId,
            @RequestParam(required = false) StatoPartita stato) {

        List<PartitaDTO> risultato = new ArrayList<>();
        for (Partita partita : this.partitaService.filtra(torneoId, squadraId, stato)) {
            risultato.add(PartitaDTO.from(partita));
        }
        return risultato;
    }

    // GET /api/partite/opzioni-filtro: dati per riempire le select di torneo e squadra
    @GetMapping("/api/partite/opzioni-filtro")
    public Map<String, List<OpzioneDTO>> opzioniFiltro() {
        List<OpzioneDTO> tornei = new ArrayList<>();
        for (Torneo torneo : this.torneoService.findAll()) {
            tornei.add(new OpzioneDTO(torneo.getId(), torneo.getNome()));
        }

        List<OpzioneDTO> squadre = new ArrayList<>();
        for (Squadra squadra : this.squadraService.findAll()) {
            squadre.add(new OpzioneDTO(squadra.getId(), squadra.getNome()));
        }

        Map<String, List<OpzioneDTO>> opzioni = new HashMap<>();
        opzioni.put("tornei", tornei);
        opzioni.put("squadre", squadre);
        return opzioni;
    }
}
