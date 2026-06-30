package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.repository.GiocatoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiocatoreService {

    private final GiocatoreRepository giocatoreRepository;

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    public Giocatore findById(Long id) {
        return this.giocatoreRepository.findById(id).orElse(null);
    }

    public List<Giocatore> findAll() {
        return (List<Giocatore>) this.giocatoreRepository.findAll();
    }

    public List<Giocatore> findBySquadra(Squadra squadra) {
        return this.giocatoreRepository.findBySquadra(squadra);
    }

    public Giocatore save(Giocatore giocatore) {
        return this.giocatoreRepository.save(giocatore);
    }

}