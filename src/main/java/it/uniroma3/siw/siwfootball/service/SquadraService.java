package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.SquadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SquadraService {

    private final SquadraRepository squadraRepository;

    public SquadraService(SquadraRepository squadraRepository) {
        this.squadraRepository = squadraRepository;
    }

    //serve per il caso d'uso "dettaglio squadra"
    public Squadra findById(Long id) {
        return this.squadraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Squadra non trovata: " + id));
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

    @Transactional
    public void delete(Squadra squadra) {
        this.squadraRepository.delete(squadra);
    }
}
