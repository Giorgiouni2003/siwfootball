package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Utente;
import it.uniroma3.siw.siwfootball.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UtenteService utenteService;

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
    public String register(@Valid @ModelAttribute("utente") Utente utente, BindingResult bindingResult) {

        // lo username non deve essere gia' usato da un altro utente
        if (this.utenteService.findByUsername(utente.getUsername()) != null) {
            bindingResult.rejectValue("username", "duplicato", "Username già in uso");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        this.utenteService.save(utente);
        // dopo la registrazione, nuova richiesta GET alla pagina di login
        return "redirect:/login";
    }

    // dopo il login, redirect in base al ruolo
    @GetMapping("/success")
    public String success() {
        // nuova richiesta GET alla home dopo il login
        return "redirect:/";
    }
}