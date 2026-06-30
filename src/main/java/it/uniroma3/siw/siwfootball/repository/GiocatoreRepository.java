package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {

    List<Giocatore> findBySquadra(Squadra squadra);

}
