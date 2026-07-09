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
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String anno;

    @NotBlank
    @Column(nullable = false)
    private String descrizione;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Squadra> squadre = new ArrayList<>();

    public void addSquadra(Squadra squadra) {
        this.squadre.add(squadra);
        squadra.getTornei().add(this);
    }

    public void removeSquadra(Squadra squadra) {
        this.squadre.remove(squadra);
        squadra.getTornei().remove(this);
    }
}
