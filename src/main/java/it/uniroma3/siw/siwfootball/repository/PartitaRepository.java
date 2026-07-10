package it.uniroma3.siw.siwfootball.repository;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.model.StatoPartita;
import it.uniroma3.siw.siwfootball.model.Torneo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartitaRepository extends CrudRepository<Partita, Long> {



    List<Partita> findBySquadraDiCasaOrSquadraDiTrasferta(Squadra squadraDiCasa, Squadra squadraDiTrasferta);

    List<Partita> findByArbitro(Arbitro arbitro);





    //METODO USATO ANCHE PER IL TEST CASO LAZY/NAIVE
    List<Partita> findByTorneo(Torneo torneo);


    //METODO USATO SIA PER IL CASO D'USO DELLA CLASSIFICA DI UN TORNEO E ANCHE PER IL TEST CASO JOIN FETCH
    @Query("SELECT p FROM Partita p " +
            "JOIN FETCH p.squadraDiCasa " +
            "JOIN FETCH p.squadraDiTrasferta " +
            "WHERE p.torneo = :torneo AND p.stato = :stato")
    List<Partita> findPartiteGiocateByTorneo(@Param("torneo") Torneo torneo, @Param("stato") StatoPartita stato);


    //METODO USATO PER IL TEST CASO ENTITYGRAPH: stessa query derivata di findByTorneo+stato,
    //ma l'EntityGraph dice a Hibernate di caricare subito anche le due squadre (con una LEFT JOIN)
    @EntityGraph(attributePaths = {"squadraDiCasa", "squadraDiTrasferta"})
    List<Partita> findByTorneoAndStato(Torneo torneo, StatoPartita stato);




}