package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.repository.TorneoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TorneoService {

    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public Torneo findById(Long id) {
        return this.torneoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Torneo non trovato: " + id));
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

}