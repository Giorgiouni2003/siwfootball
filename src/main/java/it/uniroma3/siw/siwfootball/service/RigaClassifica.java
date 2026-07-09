package it.uniroma3.siw.siwfootball.service;

import it.uniroma3.siw.siwfootball.model.Squadra;

// rappresenta una riga della classifica di un torneo: non e' salvata sul db,
// viene calcolata dal service a partire dai risultati delle partite giocate
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


    
    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public int getPuntiTotali() {
        return puntiTotali;
    }

    public void setPuntiTotali(int puntiTotali) {
        this.puntiTotali = puntiTotali;
    }

    public int getVittorie() {
        return vittorie;
    }

    public void setVittorie(int vittorie) {
        this.vittorie = vittorie;
    }

    public int getPareggi() {
        return pareggi;
    }

    public void setPareggi(int pareggi) {
        this.pareggi = pareggi;
    }

    public int getSconfitte() {
        return sconfitte;
    }

    public void setSconfitte(int sconfitte) {
        this.sconfitte = sconfitte;
    }

    public int getGolFatti() {
        return golFatti;
    }

    public void setGolFatti(int golFatti) {
        this.golFatti = golFatti;
    }

    public int getGolSubiti() {
        return golSubiti;
    }

    public void setGolSubiti(int golSubiti) {
        this.golSubiti = golSubiti;
    }
}