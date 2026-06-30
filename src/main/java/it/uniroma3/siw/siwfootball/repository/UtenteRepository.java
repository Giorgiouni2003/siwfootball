package it.uniroma3.siw.siwfootball.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.siwfootball.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

    //serve per la parte di Spring Security
    Utente findByUsername(String username);

}