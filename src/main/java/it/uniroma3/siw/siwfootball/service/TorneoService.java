package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.TorneoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public Torneo findById(Long id) {
        return this.torneoRepository.findById(id).orElse(null);
    }

    public List<Torneo> findAll() {
        return (List<Torneo>) this.torneoRepository.findAll();
    }

    public Torneo findByNomeAndAnno(String nome, String anno) {
        return this.torneoRepository.findByNomeAndAnno(nome, anno);
    }

    public Torneo save(Torneo torneo) {
        return this.torneoRepository.save(torneo);
    }

}