package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.repository.ArbitroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArbitroService {

    @Autowired
    private ArbitroRepository arbitroRepository;

    public Arbitro findById(Long id) {
        Optional<Arbitro> arbitro = this.arbitroRepository.findById(id);
        if (!arbitro.isPresent()) {
            throw new EntityNotFoundException("Arbitro non trovato: " + id);
        }
        return arbitro.get();
    }

    public List<Arbitro> findAll() {
        return (List<Arbitro>) this.arbitroRepository.findAll();
    }

    @Transactional
    public Arbitro save(Arbitro arbitro) {
        return this.arbitroRepository.save(arbitro);
    }

}