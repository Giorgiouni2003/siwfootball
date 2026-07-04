package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.Torneo;
import org.springframework.data.repository.CrudRepository;



public interface SquadraRepository extends CrudRepository<Squadra, Long> {


    Squadra findByNome(String nome);



}
