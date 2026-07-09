package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Squadra;
import lombok.Getter;
import lombok.Setter;

// rappresenta una riga della classifica di un torneo: non e' salvata sul db,
// viene calcolata dal service a partire dai risultati delle partite giocate
@Getter
@Setter
public class RigaClassifica {

    private Squadra squadra;
    private int puntiTotali;
    private int vittorie;
    private int pareggi;
    private int sconfitte;
    private int golFatti;
    private int golSubiti;

    public RigaClassifica(Squadra squadra) {
        this.squadra = squadra;
    }
}
