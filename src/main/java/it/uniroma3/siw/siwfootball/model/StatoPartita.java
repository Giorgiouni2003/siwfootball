package it.uniroma3.siw.siwfootball.model;

/**
 * Stato in cui si puo' trovare una partita.
 * Enum a due soli valori, cosi' l'utente puo' solo scegliere tra le opzioni
 * consentite (tramite una select nel form) e non scrivere testo libero.
 */
public enum StatoPartita {

    SCHEDULED("Scheduled"),
    PLAYED("Played");

    private final String etichetta;

    StatoPartita(String etichetta) {
        this.etichetta = etichetta;
    }

    public String getEtichetta() {
        return etichetta;
    }
}
