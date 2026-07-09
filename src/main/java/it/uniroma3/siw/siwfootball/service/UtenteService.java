package it.uniroma3.siw.siwfootball.service;


import it.uniroma3.siw.siwfootball.model.Utente;
import it.uniroma3.siw.siwfootball.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public Utente save(Utente utente) {

        // cripta la password prima di salvare
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));

        // imposta il ruolo di default
        utente.setRuolo("USER");
        return this.utenteRepository.save(utente);
    }

    public Utente findByUsername(String username) {
        return this.utenteRepository.findByUsername(username);
    }



}
