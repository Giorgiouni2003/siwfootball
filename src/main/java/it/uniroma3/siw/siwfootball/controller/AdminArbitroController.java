package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Arbitro;
import it.uniroma3.siw.siwfootball.service.ArbitroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminArbitroController {

    private ArbitroService arbitroService;

    public AdminArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }





    //METODI PER CREARE UN NUOVO ARBITRO
    //senza arbitri non si possono registrare partite, quindi serve una pagina per inserirli

    @GetMapping("/admin/arbitri/new")
    public String newArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        model.addAttribute("arbitri", arbitroService.findAll());
        return "admin/arbitri/new";
    }

    @PostMapping("/admin/arbitri/new")
    public String saveArbitro(@Valid @ModelAttribute Arbitro arbitro,
                              BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("arbitri", arbitroService.findAll());
            return "admin/arbitri/new";
        }

        arbitroService.save(arbitro);
        // dopo il salvataggio, nuova richiesta GET allo stesso form (per inserirne subito un altro)
        return "redirect:/admin/arbitri/new";
    }



}
