package it.uniroma3.siw.siwfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String data;

    @NotBlank
    @Column(nullable = false)
    private String ora;

    @NotBlank
    @Column(nullable = false)
    private String luogo;

    //Gol segnati in casa (null finche' la partita non e' stata giocata)
    @Min(0)
    private Integer goalsHome;

    //Gol segnati in trasferta (null finche' la partita non e' stata giocata)
    @Min(0)
    private Integer goalsAway;

    //in che stato si trova la partita? (SCHEDULED o PLAYED, vedi enum StatoPartita)
    //viene impostato dal controller, non dal form
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPartita stato;

    //LAZY: le associazioni vengono caricate solo quando servono davvero;
    //dove servono tutte in blocco (es. classifica) si usa JOIN FETCH o EntityGraph nel repository
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Squadra squadraDiCasa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Squadra squadraDiTrasferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Arbitro arbitro;


    @OneToMany(mappedBy = "partita", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Commento> commenti = new ArrayList<>();
}
