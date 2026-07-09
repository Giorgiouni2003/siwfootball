package it.uniroma3.siw.siwfootball.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Giocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;


    @NotBlank
    @Column(nullable = false)
    private String cognome;

    @NotBlank
    @Column(nullable = false)
    private String dataDiNascita;

    @NotBlank
    @Column(nullable = false)
    private String ruolo;

    @NotNull
    @Column(nullable = false)
    private Integer altezza;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Squadra squadra;
}
