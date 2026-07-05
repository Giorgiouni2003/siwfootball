package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminTorneoController {

    // dipendenza da TorneoService via costruttore

    private TorneoService torneoService;

    public AdminTorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    // METODI PER CREARE UN NUOVO TORNEO
    @GetMapping("/admin/tornei/new")
    public String newTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());

        return "admin/tornei/new";
    }


    @PostMapping("/admin/tornei/new")
    public String newTorneo(@Valid @ModelAttribute Torneo torneo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/tornei/new";
        }

        torneoService.save(torneo);
        return "redirect:/tornei/" + torneo.getId();
    }


    //METODI PER MODIFICARE UN TORNEO

    @GetMapping("/admin/tornei/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        Torneo torneo = torneoService.findById(id);

        model.addAttribute("torneo", torneo);

        return "admin/tornei/edit";
    }

    @PostMapping("/admin/tornei/{id}/edit")
    public String editTorneo(@PathVariable Long id, @Valid @ModelAttribute Torneo torneo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/tornei/edit";
        }

        Torneo oldTorneo = torneoService.findById(id);

        oldTorneo.setNome(torneo.getNome());
        oldTorneo.setDescrizione(torneo.getDescrizione());
        oldTorneo.setAnno(torneo.getAnno());

        torneoService.save(oldTorneo);

        return "redirect:/tornei/" + oldTorneo.getId();
    }





}
