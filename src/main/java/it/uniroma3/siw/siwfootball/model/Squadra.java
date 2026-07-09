package it.uniroma3.siw.siwfootball.model;

import jakarta.persistence.*;
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
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
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

    @OneToMany(mappedBy = "squadra", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Giocatore> giocatori = new ArrayList<>();

    @ManyToMany(mappedBy = "squadre", fetch = FetchType.LAZY)
    private List<Torneo> tornei = new ArrayList<>();

    public void addGiocatore(Giocatore giocatore) {
        this.giocatori.add(giocatore);
        giocatore.setSquadra(this);
    }

    public void removeGiocatore(Giocatore giocatore) {
        this.giocatori.remove(giocatore);
        giocatore.setSquadra(null);
    }
}
