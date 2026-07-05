package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Commento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentoRepository  extends CrudRepository<Commento, Long> {

    List<Commento> findByPartitaId(Long id);
}
