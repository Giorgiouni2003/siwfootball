package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.repository.ArbitroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public Arbitro findById(Long id) {
        return this.arbitroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Arbitro non trovato: " + id));
    }

    public List<Arbitro> findAll() {
        return (List<Arbitro>) this.arbitroRepository.findAll();
    }

}