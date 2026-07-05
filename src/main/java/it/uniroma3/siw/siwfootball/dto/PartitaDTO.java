package it.uniroma3.siw.siwfootball.dto;

import it.uniroma3.siw.siwfootball.model.Partita;

// DTO = versione "leggera" della Partita, solo i campi che servono al front-end React
public record PartitaDTO(
        Long id,
        String data,
        String ora,
        String luogo,
        Integer goalsHome,
        Integer goalsAway,
        String stato,
        String etichettaStato,
        Long torneoId,
        String torneoNome,
        Long squadraDiCasaId,
        String squadraDiCasaNome,
        Long squadraDiTrasfertaId,
        String squadraDiTrasfertaNome
) {

    // costruisce un DTO a partire dall'entità Partita
    public static PartitaDTO from(Partita p) {
        return new PartitaDTO(
                p.getId(),
                p.getData(),
                p.getOra(),
                p.getLuogo(),
                p.getGoalsHome(),
                p.getGoalsAway(),
                p.getStato().name(),
                p.getStato().getEtichetta(),
                p.getTorneo().getId(),
                p.getTorneo().getNome(),
                p.getSquadraDiCasa().getId(),
                p.getSquadraDiCasa().getNome(),
                p.getSquadraDiTrasferta().getId(),
                p.getSquadraDiTrasferta().getNome()
        );
    }
}
