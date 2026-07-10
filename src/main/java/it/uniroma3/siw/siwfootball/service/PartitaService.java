package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.PartitaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;


    public Partita findById(Long id) {
        Optional<Partita> partita = this.partitaRepository.findById(id);
        if (!partita.isPresent()) {
            throw new EntityNotFoundException("Partita non trovata: " + id);
        }
        return partita.get();
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
        for (Partita partita : this.partitaRepository.findAll()) {
            partite.add(partita);
        }
        return partite;
    }





    // filtra le partite in memoria in base ai parametri passati (tutti opzionali, null = "nessun filtro")
    public List<Partita> filtra(Long torneoId, Long squadraId, StatoPartita stato) {
        List<Partita> risultato = new ArrayList<>();

        for (Partita partita : this.findAll()) {

            // filtro per torneo, se richiesto
            if (torneoId != null && !torneoId.equals(partita.getTorneo().getId())) {
                continue;
            }

            // filtro per squadra (casa o trasferta), se richiesto
            if (squadraId != null
                    && !squadraId.equals(partita.getSquadraDiCasa().getId())
                    && !squadraId.equals(partita.getSquadraDiTrasferta().getId())) {
                continue;
            }

            // filtro per stato (SCHEDULED/PLAYED), se richiesto
            if (stato != null && stato != partita.getStato()) {
                continue;
            }

            risultato.add(partita);
        }

        return risultato;
    }

    @Transactional
    public Partita save(Partita partita) {
        return this.partitaRepository.save(partita);
    }


    @Transactional
    public void delete(Partita p) {
        this.partitaRepository.delete(p);
    }






    //Per il caso d'uso della classifica del torneo
    public List<RigaClassifica> getClassifica(Torneo torneo) {


        List<Partita> partiteGiocate = this.partitaRepository.findPartiteGiocateByTorneo(torneo, StatoPartita.PLAYED);

        Map<Squadra, RigaClassifica> mappaClassifica = new HashMap<>();

        // ogni squadra iscritta al torneo parte con una riga a zero punti,
        // cosi' compare in classifica anche se non ha ancora giocato
        for (Squadra squadra : torneo.getSquadre()) {
            mappaClassifica.put(squadra, new RigaClassifica(squadra));
        }

        for (Partita partita : partiteGiocate) {

            // una partita segnata come giocata ma senza risultato (dati inconsistenti,
            // es. inseriti a mano nel db) viene ignorata invece di far fallire la classifica
            if (partita.getGoalsHome() == null || partita.getGoalsAway() == null) {
                continue;
            }

            // 1. recupera (o crea se non esiste) la RigaClassifica per squadraDiCasa e squadraDiTrasferta nella mappa

            RigaClassifica rigaCasa = mappaClassifica.get(partita.getSquadraDiCasa());
            if (rigaCasa == null) {
                rigaCasa = new RigaClassifica(partita.getSquadraDiCasa());
                mappaClassifica.put(partita.getSquadraDiCasa(), rigaCasa);
            }

            RigaClassifica rigaTrasferta = mappaClassifica.get(partita.getSquadraDiTrasferta());
            if (rigaTrasferta == null) {
                rigaTrasferta = new RigaClassifica(partita.getSquadraDiTrasferta());
                mappaClassifica.put(partita.getSquadraDiTrasferta(), rigaTrasferta);
            }

            // 2. aggiorna golFatti/golSubiti per entrambe in base a goalsHome/goalsAway

            rigaCasa.setGolFatti(rigaCasa.getGolFatti() + partita.getGoalsHome());
            rigaCasa.setGolSubiti(rigaCasa.getGolSubiti() + partita.getGoalsAway());

            rigaTrasferta.setGolFatti(rigaTrasferta.getGolFatti() + partita.getGoalsAway());
            rigaTrasferta.setGolSubiti(rigaTrasferta.getGolSubiti() + partita.getGoalsHome());

            // 3. confronta goalsHome e goalsAway per stabilire vittoria/pareggio/sconfitta
            //    e assegna i punti: vittoria 3, pareggio 1, sconfitta 0

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

        }

        List<RigaClassifica> classifica = new ArrayList<>(mappaClassifica.values());

        // ordina per punti; a parita' di punti conta la differenza reti, poi i gol fatti
        Collections.sort(classifica, new Comparator<RigaClassifica>() {
            @Override
            public int compare(RigaClassifica r1, RigaClassifica r2) {
                if (r1.getPuntiTotali() != r2.getPuntiTotali()) {
                    return r2.getPuntiTotali() - r1.getPuntiTotali();
                }
                int differenzaReti1 = r1.getGolFatti() - r1.getGolSubiti();
                int differenzaReti2 = r2.getGolFatti() - r2.getGolSubiti();
                if (differenzaReti1 != differenzaReti2) {
                    return differenzaReti2 - differenzaReti1;
                }
                return r2.getGolFatti() - r1.getGolFatti();
            }
        });

        return classifica;
    }

}
