package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.service.ArbitroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminArbitroController {

    private ArbitroService arbitroService;

    public AdminArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    //METODI PER INSERIRE UN NUOVO ARBITRO
    @GetMapping("/admin/arbitri/new")
    public String newArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "admin/arbitri/new";
    }

    @PostMapping("/admin/arbitri/new")
    public String saveArbitro(@ModelAttribute Arbitro arbitro) {
        arbitroService.save(arbitro);
        return "redirect:/";
    }
}