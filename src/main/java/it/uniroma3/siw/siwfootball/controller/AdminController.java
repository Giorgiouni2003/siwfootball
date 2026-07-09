package it.uniroma3.siw.siwfootball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // pagina indice dell'area admin, con i link alle sezioni di gestione
    @GetMapping("/admin")
    public String index() {
        return "admin/index";
    }
}
