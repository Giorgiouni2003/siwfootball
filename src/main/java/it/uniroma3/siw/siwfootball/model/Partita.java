package it.uniroma3.siw.siwfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Squadra squadraDiCasa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Squadra squadraDiTrasferta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Arbitro arbitro;


    @OneToMany(mappedBy = "partita", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Commento> commenti = new ArrayList<>();






    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public StatoPartita getStato() {
        return stato;
    }

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Squadra getSquadraDiCasa() {
        return squadraDiCasa;
    }

    public void setSquadraDiCasa(Squadra squadraDiCasa) {
        this.squadraDiCasa = squadraDiCasa;
    }

    public Squadra getSquadraDiTrasferta() {
        return squadraDiTrasferta;
    }

    public void setSquadraDiTrasferta(Squadra squadraDiTrasferta) {
        this.squadraDiTrasferta = squadraDiTrasferta;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partita partita = (Partita) o;
        return id != null && id.equals(partita.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}