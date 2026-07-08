package it.uniroma3.siw.siwfootball;

import it.uniroma3.siw.siwfootball.model.*;
import it.uniroma3.siw.siwfootball.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Esperimento di performance richiesto dalla specifica del progetto:
 * confronta il caricamento "naive" (senza JOIN FETCH, quindi soggetto a
 * problema N+1 sulle associazioni @ManyToOne) con il caricamento tramite
 * JOIN FETCH gia' usato in PartitaRepository.findPartiteGiocateByTorneo,
 * per il calcolo della classifica di un torneo (PartitaService.getClassifica).
 *
 * @Rollback: i dati di test vengono creati e poi annullati a fine test,
 * cosi' il database reale non viene alterato in modo permanente.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.generate_statistics=true",
        "logging.level.org.hibernate.SQL=warn",
        "logging.level.org.hibernate.stat=warn"
})
@Transactional
@Rollback
public class PerformanceExperimentTest {

    private static final int NUM_SQUADRE = 20;
    private static final int NUM_PARTITE = 300;

    @Autowired
    private TorneoRepository torneoRepository;
    @Autowired
    private SquadraRepository squadraRepository;
    @Autowired
    private ArbitroRepository arbitroRepository;
    @Autowired
    private PartitaRepository partitaRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void confrontoLazyVsJoinFetchPerClassifica() {

        Statistics stats = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        stats.setStatisticsEnabled(true);

        // ---- 1. Prepara dati sintetici -------------------------------------------------
        Torneo torneo = new Torneo();
        torneo.setNome("Torneo Benchmark");
        torneo.setAnno("2026");
        torneo.setDescrizione("Torneo generato per l'esperimento di performance");

        Arbitro arbitro = new Arbitro();
        arbitro.setNome("Mario");
        arbitro.setCognome("Rossi");
        arbitro.setCodiceArbitrale("ARB-BENCH-001");
        arbitro = arbitroRepository.save(arbitro);

        List<Squadra> squadre = new ArrayList<>();
        for (int i = 0; i < NUM_SQUADRE; i++) {
            Squadra squadra = new Squadra();
            squadra.setNome("Squadra Benchmark " + i);
            squadra.setAnnoDiFondazione("2000");
            squadra.setCitta("Città " + i);
            squadra = squadraRepository.save(squadra);
            squadre.add(squadra);
            torneo.addSquadra(squadra);
        }
        torneo = torneoRepository.save(torneo);

        for (int i = 0; i < NUM_PARTITE; i++) {
            Squadra casa = squadre.get(i % NUM_SQUADRE);
            Squadra trasferta = squadre.get((i + 1 + (i / NUM_SQUADRE)) % NUM_SQUADRE);
            if (casa.equals(trasferta)) {
                trasferta = squadre.get((i + 2) % NUM_SQUADRE);
            }

            Partita partita = new Partita();
            partita.setData("2026-0" + ((i % 9) + 1) + "-01");
            partita.setOra("15:00");
            partita.setLuogo("Stadio Benchmark " + i);
            partita.setGoalsHome(i % 4);
            partita.setGoalsAway((i + 1) % 3);
            partita.setStato(StatoPartita.PLAYED);
            partita.setTorneo(torneo);
            partita.setSquadraDiCasa(casa);
            partita.setSquadraDiTrasferta(trasferta);
            partita.setArbitro(arbitro);
            partitaRepository.save(partita);
        }

        entityManager.flush();
        entityManager.clear(); // svuota la persistence context: nessuna cache di primo livello a falsare i risultati

        // ---- 2. Strategia LAZY/naive: findByTorneo senza JOIN FETCH --------------------
        stats.clear();
        long startLazy = System.nanoTime();

        List<Partita> partiteLazy = partitaRepository.findByTorneo(torneo);
        long puntiTotaliLazy = 0;
        for (Partita p : partiteLazy) {
            if (p.getStato() == StatoPartita.PLAYED) {
                // accesso alle associazioni ManyToOne: con l'accesso "naive" (nessun
                // join fetch) Hibernate esegue una SELECT aggiuntiva per ciascuna
                // associazione non ancora presente nel persistence context
                puntiTotaliLazy += p.getSquadraDiCasa().getId() + p.getSquadraDiTrasferta().getId();
            }
        }

        long durataLazyNs = System.nanoTime() - startLazy;
        long queryCountLazy = stats.getQueryExecutionCount();
        long prepStatCountLazy = stats.getPrepareStatementCount();

        entityManager.clear();

        // ---- 3. Strategia JOIN FETCH: findPartiteGiocateByTorneo -----------------------
        stats.clear();
        long startJoinFetch = System.nanoTime();

        List<Partita> partiteJoinFetch = partitaRepository.findPartiteGiocateByTorneo(torneo, StatoPartita.PLAYED);
        long puntiTotaliJoinFetch = 0;
        for (Partita p : partiteJoinFetch) {
            puntiTotaliJoinFetch += p.getSquadraDiCasa().getId() + p.getSquadraDiTrasferta().getId();
        }

        long durataJoinFetchNs = System.nanoTime() - startJoinFetch;
        long queryCountJoinFetch = stats.getQueryExecutionCount();
        long prepStatCountJoinFetch = stats.getPrepareStatementCount();

        // ---- 4. Report ------------------------------------------------------------------
        System.out.println("\n================ ESPERIMENTO DI PERFORMANCE ================");
        System.out.println("Caso d'uso: caricamento partite giocate di un torneo per il calcolo della classifica");
        System.out.println("Dati di test: " + NUM_SQUADRE + " squadre, " + NUM_PARTITE + " partite giocate\n");

        System.out.printf("Strategia LAZY / naive (findByTorneo, nessun JOIN FETCH):%n");
        System.out.printf("  - partite elaborate:            %d%n", partiteLazy.size());
        System.out.printf("  - query JPQL di primo livello:  %d%n", queryCountLazy);
        System.out.printf("  - SELECT SQL fisiche eseguite:  %d (1 principale + N+1 per le squadre non ancora in cache)%n", prepStatCountLazy);
        System.out.printf("  - tempo di esecuzione:          %.2f ms%n%n", durataLazyNs / 1_000_000.0);

        System.out.printf("Strategia JOIN FETCH (findPartiteGiocateByTorneo):%n");
        System.out.printf("  - partite elaborate:            %d%n", partiteJoinFetch.size());
        System.out.printf("  - query JPQL di primo livello:  %d%n", queryCountJoinFetch);
        System.out.printf("  - SELECT SQL fisiche eseguite:  %d%n", prepStatCountJoinFetch);
        System.out.printf("  - tempo di esecuzione:          %.2f ms%n%n", durataJoinFetchNs / 1_000_000.0);

        System.out.printf("Riduzione delle SELECT fisiche: da %d a %d (-%.1f%%)%n",
                prepStatCountLazy, prepStatCountJoinFetch,
                100.0 * (prepStatCountLazy - prepStatCountJoinFetch) / prepStatCountLazy);
        System.out.printf("Speedup: %.2fx%n", (double) durataLazyNs / durataJoinFetchNs);
        System.out.println("==============================================================\n");
    }
}
