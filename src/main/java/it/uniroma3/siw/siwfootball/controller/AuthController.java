package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Utente;
import it.uniroma3.siw.siwfootball.service.UtenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // mostra la pagina di login
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // mostra il form di registrazione
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("utente", new Utente());
        return "auth/register";
    }

    // gestisce la registrazione
    @PostMapping("/register")
    public String register(@ModelAttribute("utente") Utente utente) {
        this.utenteService.save(utente);
        return "redirect:/login";
    }

    // dopo il login, redirect in base al ruolo
    @GetMapping("/success")
    public String success() {
        return "redirect:/";
    }
}