package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.SquadraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquadraService {

    private final SquadraRepository squadraRepository;

    public SquadraService(SquadraRepository squadraRepository) {
        this.squadraRepository = squadraRepository;
    }

    //serve per il caso d'uso "dettaglio squadra"
    public Squadra findById(Long id) {
        return this.squadraRepository.findById(id).orElse(null);
    }


    public Squadra findByNome (String nome)
    {
        return this.squadraRepository.findByNome(nome);
    }

    public List<Squadra> findAll(){
        return (List<Squadra>) this.squadraRepository.findAll();
    }

    public Squadra save(Squadra squadra) {
        return this.squadraRepository.save(squadra);
    }

}
