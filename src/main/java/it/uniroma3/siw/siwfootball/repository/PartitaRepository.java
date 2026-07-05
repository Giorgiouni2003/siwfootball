package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    List<Partita> findByTorneo(Torneo torneo);

    List<Partita> findBySquadraDiCasaOrSquadraDiTrasferta(Squadra squadraDiCasa, Squadra squadraDiTrasferta);

    List<Partita> findByArbitro(Arbitro arbitro);



    @Query("SELECT p FROM Partita p " +
            "JOIN FETCH p.squadraDiCasa " +
            "JOIN FETCH p.squadraDiTrasferta " +
            "WHERE p.torneo = :torneo AND p.stato = :stato")
    List<Partita> findPartiteGiocateByTorneo(@Param("torneo") Torneo torneo, @Param("stato") StatoPartita stato);

}