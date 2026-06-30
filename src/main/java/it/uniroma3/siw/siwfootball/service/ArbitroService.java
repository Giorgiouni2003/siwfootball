package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.repository.ArbitroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public Arbitro findById(Long id) {
        return this.arbitroRepository.findById(id).orElse(null);
    }

    public List<Arbitro> findAll() {
        return (List<Arbitro>) this.arbitroRepository.findAll();
    }

    public Arbitro save(Arbitro arbitro) {
        return this.arbitroRepository.save(arbitro);
    }

}