package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.SquadraRepository;
import it.uniroma3.siw.siwfootball.repository.TorneoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final SquadraRepository squadraRepository;

    public TorneoService(TorneoRepository torneoRepository, SquadraRepository squadraRepository) {
        this.torneoRepository = torneoRepository;
        this.squadraRepository = squadraRepository;
    }

    public Torneo findById(Long id) {
        Optional<Torneo> torneo = this.torneoRepository.findById(id);
        if (!torneo.isPresent()) {
            throw new EntityNotFoundException("Torneo non trovato: " + id);
        }
        return torneo.get();
    }

    public List<Torneo> findAll() {
        return (List<Torneo>) this.torneoRepository.findAll();
    }

    public Torneo findByNomeAndAnno(String nome, String anno) {
        return this.torneoRepository.findByNomeAndAnno(nome, anno);
    }

    @Transactional
    public Torneo save(Torneo torneo) {
        return this.torneoRepository.save(torneo);
    }

    //iscrive una squadra al torneo (se non e' gia' iscritta)
    @Transactional
    public void iscriviSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = this.findById(torneoId);

        Optional<Squadra> squadra = this.squadraRepository.findById(squadraId);
        if (!squadra.isPresent()) {
            throw new EntityNotFoundException("Squadra non trovata: " + squadraId);
        }

        if (!torneo.getSquadre().contains(squadra.get())) {
            torneo.addSquadra(squadra.get());
            this.torneoRepository.save(torneo);
        }
    }

    //rimuove una squadra dal torneo
    @Transactional
    public void rimuoviSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = this.findById(torneoId);

        Optional<Squadra> squadra = this.squadraRepository.findById(squadraId);
        if (!squadra.isPresent()) {
            throw new EntityNotFoundException("Squadra non trovata: " + squadraId);
        }

        torneo.removeSquadra(squadra.get());
        this.torneoRepository.save(torneo);
    }




}
