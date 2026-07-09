package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.repository.GiocatoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    public Giocatore findById(Long id) {
        Optional<Giocatore> giocatore = this.giocatoreRepository.findById(id);
        if (!giocatore.isPresent()) {
            throw new EntityNotFoundException("Giocatore non trovato: " + id);
        }
        return giocatore.get();
    }

    public List<Giocatore> findAll() {
        return (List<Giocatore>) this.giocatoreRepository.findAll();
    }

    public List<Giocatore> findBySquadra(Squadra squadra) {
        return this.giocatoreRepository.findBySquadra(squadra);
    }

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return this.giocatoreRepository.save(giocatore);
    }

}