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
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String cognome;

    //codice identificativo dell'arbitro (richiesto dalla specifica)
    @NotBlank
    @Column(nullable = false)
    private String codiceArbitrale;

    //le partite dirette da questo arbitro
    @OneToMany(mappedBy = "arbitro", fetch = FetchType.LAZY)
    private List<Partita> partite = new ArrayList<>();
}
