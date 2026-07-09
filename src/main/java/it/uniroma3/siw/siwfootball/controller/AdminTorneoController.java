package it.uniroma3.siw.siwfootball.controller;

import it.uniroma3.siw.siwfootball.model.Torneo;
import it.uniroma3.siw.siwfootball.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminTorneoController {

    @Autowired
    private TorneoService torneoService;

    // METODI PER CREARE UN NUOVO TORNEO
    @GetMapping("/admin/tornei/new")
    public String newTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());

        return "admin/tornei/new";
    }


    @PostMapping("/admin/tornei/new")
    public String newTorneo(@Valid @ModelAttribute Torneo torneo, BindingResult bindingResult) {

        // il torneo non deve essere gia' presente (stesso nome nello stesso anno)
        if (this.torneoService.findByNomeAndAnno(torneo.getNome(), torneo.getAnno()) != null) {
            bindingResult.rejectValue("nome", "duplicato", "Torneo già esistente per questo anno");
        }

        if (bindingResult.hasErrors()) {
            return "admin/tornei/new";
        }

        torneoService.save(torneo);
        // dopo il salvataggio, nuova richiesta GET alla pagina del torneo appena creato
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

        // dopo la modifica, nuova richiesta GET alla pagina del torneo aggiornato
        return "redirect:/tornei/" + oldTorneo.getId();
    }


    //METODI PER GESTIRE LE SQUADRE PARTECIPANTI AL TORNEO

    @PostMapping("/admin/tornei/{id}/squadre/add")
    public String iscriviSquadra(@PathVariable Long id, @RequestParam("squadraId") Long squadraId) {

        torneoService.iscriviSquadra(id, squadraId);

        // dopo l'iscrizione, nuova richiesta GET alla pagina del torneo (con la squadra aggiunta)
        return "redirect:/tornei/" + id;
    }

    @PostMapping("/admin/tornei/{id}/squadre/{squadraId}/remove")
    public String rimuoviSquadra(@PathVariable Long id, @PathVariable Long squadraId) {

        torneoService.rimuoviSquadra(id, squadraId);

        // dopo la rimozione, nuova richiesta GET alla pagina del torneo (senza quella squadra)
        return "redirect:/tornei/" + id;
    }


}
