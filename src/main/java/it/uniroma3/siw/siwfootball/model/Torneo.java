package it.uniroma3.siw.siwfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Squadra> getSquadre() {
        return squadre;
    }

    public void setSquadre(List<Squadra> squadre) {
        this.squadre = squadre;
    }

    public void addSquadra(Squadra squadra) {
        this.squadre.add(squadra);
        squadra.getTornei().add(this);
    }

    public void removeSquadra(Squadra squadra) {
        this.squadre.remove(squadra);
        squadra.getTornei().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Torneo torneo = (Torneo) o;
        return id != null && id.equals(torneo.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}