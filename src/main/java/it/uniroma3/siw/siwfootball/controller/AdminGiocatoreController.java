package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Giocatore;
import it.uniroma3.siw.siwfootball.model.Squadra;
import it.uniroma3.siw.siwfootball.service.GiocatoreService;
import it.uniroma3.siw.siwfootball.service.SquadraService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminGiocatoreController {

    private GiocatoreService giocatoreService;
    private SquadraService squadraService;

    public AdminGiocatoreController(GiocatoreService giocatoreService,
                                    SquadraService squadraService) {
        this.giocatoreService = giocatoreService;
        this.squadraService = squadraService;
    }

    //METODI PER INSERIRE UN NUOVO GIOCATORE

    @GetMapping("/admin/giocatori/new")
    public String newGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", squadraService.findAll());
        return "admin/giocatori/new";
    }

    @PostMapping("/admin/giocatori/new")
    public String saveGiocatore(@Valid @ModelAttribute Giocatore giocatore,
                                 BindingResult bindingResult,
                                 @RequestParam("squadra.id") Long squadraId,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", squadraService.findAll());
            return "admin/giocatori/new";
        }

        Squadra squadra = squadraService.findById(squadraId);
        giocatore.setSquadra(squadra);
        giocatoreService.save(giocatore);
        return "redirect:/squadre";
    }


    //METODI PER MODIFICARE UN GIOCATORE

    @GetMapping("/admin/giocatori/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        Giocatore giocatore = giocatoreService.findById(id);

        model.addAttribute("giocatore", giocatore);

        return "admin/giocatori/edit";
    }

    @PostMapping("/admin/giocatori/{id}/edit")
    public String editGiocatore(@PathVariable Long id, @Valid @ModelAttribute Giocatore giocatore,
                                 BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", squadraService.findAll());
            return "admin/giocatori/edit";
        }

        Giocatore oldGiocatore = giocatoreService.findById(id);

        oldGiocatore.setSquadra(giocatore.getSquadra());
        oldGiocatore.setAltezza(giocatore.getAltezza());
        oldGiocatore.setCognome(giocatore.getCognome());
        oldGiocatore.setNome(giocatore.getNome());
        oldGiocatore.setDataDiNascita(giocatore.getDataDiNascita());
        oldGiocatore.setRuolo(giocatore.getRuolo());

        giocatoreService.save(oldGiocatore);

        return "redirect:/squadre/" + oldGiocatore.getSquadra().getId();
    }
}