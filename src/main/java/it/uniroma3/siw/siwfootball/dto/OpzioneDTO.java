package it.uniroma3.siw.siwfootball.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// DTO generico "id + nome", usato per popolare le select dei filtri (tornei e squadre)
@Getter
@RequiredArgsConstructor
public class OpzioneDTO {

    private final Long id;
    private final String nome;
}
