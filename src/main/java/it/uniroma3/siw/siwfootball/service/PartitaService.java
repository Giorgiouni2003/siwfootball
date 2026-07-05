package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.PartitaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PartitaService {

    private final PartitaRepository partitaRepository;

    public PartitaService(PartitaRepository partitaRepository) {

        this.partitaRepository = partitaRepository;
    }


    public Partita findById(Long id) {
        return this.partitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partita non trovata: " + id));
    }


    public List<Partita> findByTorneo(Torneo torneo) {
        return partitaRepository.findByTorneo(torneo);
    }

    public List<Partita> findBySquadraDiCasaOrSquadraDiTrasferta(Squadra squadraDiCasa, Squadra squadraDiTrasferta)
    {
        return this.partitaRepository.findBySquadraDiCasaOrSquadraDiTrasferta(squadraDiCasa, squadraDiTrasferta);
    }

    public List<Partita> findByArbitro(Arbitro arbitro){
        return this.partitaRepository.findByArbitro(arbitro);
    }

    // tutte le partite, usata come base per i filtri dell'elenco React
    public List<Partita> findAll() {
        List<Partita> partite = new ArrayList<>();
        this.partitaRepository.findAll().forEach(partite::add);
        return partite;
    }

    // filtra le partite in memoria in base ai parametri passati (tutti opzionali, null = "nessun filtro")
    public List<Partita> filtra(Long torneoId, Long squadraId, StatoPartita stato) {
        return this.findAll().stream()
                // filtro per torneo, se richiesto
                .filter(p -> torneoId == null || Objects.equals(p.getTorneo().getId(), torneoId))
                // filtro per squadra (casa o trasferta), se richiesto
                .filter(p -> squadraId == null
                        || Objects.equals(p.getSquadraDiCasa().getId(), squadraId)
                        || Objects.equals(p.getSquadraDiTrasferta().getId(), squadraId))
                // filtro per stato (SCHEDULED/PLAYED), se richiesto
                .filter(p -> stato == null || p.getStato() == stato)
                .collect(Collectors.toList());
    }

    @Transactional
    public Partita save(Partita partita) {
        return this.partitaRepository.save(partita);
    }


    @Transactional
    public void delete(Partita p) {
        this.partitaRepository.delete(p);
    }
    

    public List<RigaClassifica> getClassifica(Torneo torneo) {
        List<Partita> partiteGiocate = this.partitaRepository.findPartiteGiocateByTorneo(torneo, StatoPartita.PLAYED);

        Map<Squadra, RigaClassifica> mappaClassifica = new HashMap<>();

        for (Partita partita : partiteGiocate) {
            // 1. recupera (o crea se non esiste) la RigaClassifica per squadraDiCasa e squadraDiTrasferta nella mappa

            RigaClassifica rigaCasa = mappaClassifica.computeIfAbsent(
                    partita.getSquadraDiCasa(),
                    squadra -> new RigaClassifica(squadra));

            RigaClassifica rigaTrasferta = mappaClassifica.computeIfAbsent(
                    partita.getSquadraDiTrasferta(),
                    squadra -> new RigaClassifica(squadra));

            // 2. aggiorna golFatti/golSubiti per entrambe in base a goalsHome/goalsAway

            rigaCasa.setGolFatti(rigaCasa.getGolFatti() + partita.getGoalsHome());
            rigaCasa.setGolSubiti(rigaCasa.getGolSubiti() + partita.getGoalsAway());

            rigaTrasferta.setGolFatti(rigaTrasferta.getGolFatti() + partita.getGoalsAway());
            rigaTrasferta.setGolSubiti(rigaTrasferta.getGolSubiti() + partita.getGoalsHome());

            // 3. confronta goalsHome e goalsAway per stabilire vittoria/pareggio/sconfitta

            if (partita.getGoalsHome() > partita.getGoalsAway()) {

                // vince la squadra di casa
                rigaCasa.setVittorie(rigaCasa.getVittorie() + 1);
                rigaCasa.setPuntiTotali(rigaCasa.getPuntiTotali() + 3);
                rigaTrasferta.setSconfitte(rigaTrasferta.getSconfitte() + 1);

            } else if (partita.getGoalsHome() < partita.getGoalsAway()) {

                // vince la squadra in trasferta
                rigaTrasferta.setVittorie(rigaTrasferta.getVittorie() + 1);
                rigaTrasferta.setPuntiTotali(rigaTrasferta.getPuntiTotali() + 3);
                rigaCasa.setSconfitte(rigaCasa.getSconfitte() + 1);

            } else {

                // pareggio
                rigaCasa.setPareggi(rigaCasa.getPareggi() + 1);
                rigaCasa.setPuntiTotali(rigaCasa.getPuntiTotali() + 1);
                rigaTrasferta.setPareggi(rigaTrasferta.getPareggi() + 1);
                rigaTrasferta.setPuntiTotali(rigaTrasferta.getPuntiTotali() + 1);

            }

            //    e assegna i punti: vittoria 3, pareggio 1, sconfitta 0


        }

        List<RigaClassifica> classifica = new ArrayList<>(mappaClassifica.values());
        // ordina la lista per puntiTotali decrescente (Collections.sort con un Comparator, o classifica.sort(...))

        classifica.sort((r1, r2) -> r2.getPuntiTotali() - r1.getPuntiTotali());


        return classifica;
    }

}
