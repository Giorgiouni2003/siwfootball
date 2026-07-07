package it.uniroma3.siw.siwfootball.dto;

// DTO generico "id + nome", usato per popolare le select dei filtri (tornei e squadre)
public class OpzioneDTO {

    private Long id;
    private String nome;

    public OpzioneDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
