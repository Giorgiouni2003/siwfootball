package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Torneo;
import org.springframework.data.repository.CrudRepository;



public interface TorneoRepository extends CrudRepository<Torneo, Long> {


    Torneo findByNomeAndAnno(String nome, String anno);


}
