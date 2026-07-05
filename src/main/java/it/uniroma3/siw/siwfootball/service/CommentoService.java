package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Commento;
import it.uniroma3.siw.siwfootball.repository.CommentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentoService {

    private final CommentoRepository commentoRepository;

    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }


    public List<Commento> findAll() {
        return (List<Commento>) this.commentoRepository.findAll();
    }

    public Commento findById(Long id) {
        return this.commentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commento non trovato: " + id));
    }

    @Transactional
    public Commento save(Commento commento) {
        return this.commentoRepository.save(commento);
    }

    public List<Commento> findByPartitaId(Long partitaId) {
        return this.commentoRepository.findByPartitaId(partitaId);
    }
}
