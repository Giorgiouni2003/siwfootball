package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.PartitaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PartitaService {

    private final PartitaRepository partitaRepository;

    public PartitaService(PartitaRepository partitaRepository) {
        this.partitaRepository = partitaRepository;
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

    public Partita save(Partita partita) {
        return this.partitaRepository.save(partita);
    }



    

    public List<RigaClassifica> getClassifica(Torneo torneo) {
        List<Partita> partiteGiocate = this.partitaRepository.findPartiteGiocateByTorneo(torneo);

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
