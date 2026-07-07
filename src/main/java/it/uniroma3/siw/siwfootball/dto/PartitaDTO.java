package it.uniroma3.siw.siwfootball.dto;

import it.uniroma3.siw.siwfootball.model.Partita;

// DTO = versione "leggera" della Partita, solo i campi che servono al front-end React
public class PartitaDTO {

    private Long id;
    private String data;
    private String ora;
    private String luogo;
    private Integer goalsHome;
    private Integer goalsAway;
    private String stato;
    private String etichettaStato;
    private Long torneoId;
    private String torneoNome;
    private Long squadraDiCasaId;
    private String squadraDiCasaNome;
    private Long squadraDiTrasfertaId;
    private String squadraDiTrasfertaNome;

    // costruisce un DTO a partire dall'entità Partita
    public static PartitaDTO from(Partita p) {
        PartitaDTO dto = new PartitaDTO();
        dto.id = p.getId();
        dto.data = p.getData();
        dto.ora = p.getOra();
        dto.luogo = p.getLuogo();
        dto.goalsHome = p.getGoalsHome();
        dto.goalsAway = p.getGoalsAway();
        dto.stato = p.getStato().name();
        dto.etichettaStato = p.getStato().getEtichetta();
        dto.torneoId = p.getTorneo().getId();
        dto.torneoNome = p.getTorneo().getNome();
        dto.squadraDiCasaId = p.getSquadraDiCasa().getId();
        dto.squadraDiCasaNome = p.getSquadraDiCasa().getNome();
        dto.squadraDiTrasfertaId = p.getSquadraDiTrasferta().getId();
        dto.squadraDiTrasfertaNome = p.getSquadraDiTrasferta().getNome();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public String getStato() {
        return stato;
    }

    public String getEtichettaStato() {
        return etichettaStato;
    }

    public Long getTorneoId() {
        return torneoId;
    }

    public String getTorneoNome() {
        return torneoNome;
    }

    public Long getSquadraDiCasaId() {
        return squadraDiCasaId;
    }

    public String getSquadraDiCasaNome() {
        return squadraDiCasaNome;
    }

    public Long getSquadraDiTrasfertaId() {
        return squadraDiTrasfertaId;
    }

    public String getSquadraDiTrasfertaNome() {
        return squadraDiTrasfertaNome;
    }
}
