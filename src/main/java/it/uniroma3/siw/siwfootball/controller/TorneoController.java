package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TorneoController {

    private TorneoService torneoService;
    private PartitaService partitaService;

    public TorneoController(TorneoService torneoService, PartitaService partitaService) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
    }

    @GetMapping("/tornei")
    public String list(Model model) {
        List<Torneo> tornei = this.torneoService.findAll();
        model.addAttribute("tornei", tornei);
        return "tornei/list";
    }

    @GetMapping("/tornei/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("torneo", this.torneoService.findById(id));
        model.addAttribute("partite", this.partitaService.findByTorneo(torneoService.findById(id)));
        model.addAttribute("classifica", this.partitaService.getClassifica(torneoService.findById(id)));

        return "tornei/show";

    }






}