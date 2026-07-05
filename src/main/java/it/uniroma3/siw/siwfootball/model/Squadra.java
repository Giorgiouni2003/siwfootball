package it.uniroma3.siw.siwfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String annoDiFondazione;

    @NotBlank
    @Column(nullable = false)
    private String citta;

    @OneToMany(mappedBy = "squadra", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Giocatore> giocatori = new ArrayList<>();

    @ManyToMany(mappedBy = "squadre")
    private List<Torneo> tornei = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAnnoDiFondazione() {
        return annoDiFondazione;
    }

    public void setAnnoDiFondazione(String annoDiFondazione) {
        this.annoDiFondazione = annoDiFondazione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    public List<Torneo> getTornei() {
        return tornei;
    }

    public void setTornei(List<Torneo> tornei) {
        this.tornei = tornei;
    }

    public void addGiocatore(Giocatore giocatore) {
        this.giocatori.add(giocatore);
        giocatore.setSquadra(this);
    }

    public void removeGiocatore(Giocatore giocatore) {
        this.giocatori.remove(giocatore);
        giocatore.setSquadra(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Squadra squadra = (Squadra) o;
        return id != null && id.equals(squadra.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}