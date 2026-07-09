package it.uniroma3.siw.siwfootball.controller;


import it.uniroma3.siw.siwfootball.model.Commento;
import it.uniroma3.siw.siwfootball.model.Partita;
import it.uniroma3.siw.siwfootball.service.CommentoService;
import it.uniroma3.siw.siwfootball.service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private CommentoService commentoService;



    // pagina "shell": il contenuto vero e proprio (elenco + filtri) è renderizzato da React
    @GetMapping("/partite")
    public String list() {
        return "partite/list";
    }

    @GetMapping("/partite/{id}")
    public String partite(Model model, @PathVariable Long id) {

        Partita partita = this.partitaService.findById(id);

        model.addAttribute("partita", partita);
        model.addAttribute("commenti", this.commentoService.findByPartitaId(id));
        model.addAttribute("commento", new Commento());

        return "partite/show";


    }
}
