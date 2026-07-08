package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.PartitaRepository;
import it.uniroma3.siw.siwfootball.repository.SquadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SquadraService {

    private final SquadraRepository squadraRepository;
    private final PartitaRepository partitaRepository;

    public SquadraService(SquadraRepository squadraRepository, PartitaRepository partitaRepository) {
        this.squadraRepository = squadraRepository;
        this.partitaRepository = partitaRepository;
    }

    //serve per il caso d'uso "dettaglio squadra"
    public Squadra findById(Long id) {
        Optional<Squadra> squadra = this.squadraRepository.findById(id);
        if (!squadra.isPresent()) {
            throw new EntityNotFoundException("Squadra non trovata: " + id);
        }
        return squadra.get();
    }


    public Squadra findByNome (String nome)
    {
        return this.squadraRepository.findByNome(nome);
    }

    public List<Squadra> findAll(){
        return (List<Squadra>) this.squadraRepository.findAll();
    }

    @Transactional
    public Squadra save(Squadra squadra) {
        return this.squadraRepository.save(squadra);
    }

    /*
     * Cancella una squadra insieme a tutto cio' che la riferisce, in un'unica transazione:
     * 1. le partite in cui e' coinvolta (con i relativi commenti, via cascade)
     * 2. le iscrizioni ai tornei (righe della tabella di join)
     * 3. la squadra stessa (i giocatori vengono cancellati via cascade)
     * Senza questi passaggi la cancellazione violerebbe i vincoli di chiave esterna.
     */
    @Transactional
    public void deleteById(Long id) {
        Squadra squadra = this.findById(id);

        // 1. elimina le partite giocate in casa o in trasferta da questa squadra
        List<Partita> partite = this.partitaRepository.findBySquadraDiCasaOrSquadraDiTrasferta(squadra, squadra);
        for (Partita partita : partite) {
            this.partitaRepository.delete(partita);
        }

        // 2. toglie la squadra dai tornei a cui e' iscritta
        //    (copia della lista per non modificarla mentre la si scorre)
        List<Torneo> tornei = new ArrayList<>(squadra.getTornei());
        for (Torneo torneo : tornei) {
            torneo.removeSquadra(squadra);
        }

        // 3. elimina la squadra
        this.squadraRepository.delete(squadra);
    }





}
