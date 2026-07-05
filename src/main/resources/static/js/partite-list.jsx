// piccolo componente React: elenco partite con filtri (torneo, squadra, stato)

// prendiamo useState/useEffect direttamente dall'oggetto globale React (niente import, siamo senza build tool)
const { useState, useEffect } = React;

function ElencoPartite() {
  // partite mostrate a schermo (risultato dei filtri)
  const [partite, setPartite] = useState([]);
  // opzioni disponibili per le select (tornei e squadre esistenti)
  const [opzioni, setOpzioni] = useState({ tornei: [], squadre: [] });
  // valori scelti dall'utente nei filtri ("" = nessun filtro)
  const [torneoId, setTorneoId] = useState("");
  const [squadraId, setSquadraId] = useState("");
  const [stato, setStato] = useState("");
  // true mentre aspettiamo la risposta del server
  const [caricamento, setCaricamento] = useState(true);
  // messaggio di errore, se la chiamata al server fallisce
  const [errore, setErrore] = useState(null);

  // eseguito una sola volta al montaggio: carica le opzioni per i filtri
  useEffect(() => {
    fetch("/api/partite/opzioni-filtro")
      .then((res) => res.json())
      .then((data) => setOpzioni(data))
      .catch(() => setErrore("Impossibile caricare i filtri"));
  }, []);

  // eseguito ogni volta che cambia un filtro: ricarica l'elenco partite dal server
  useEffect(() => {
    setCaricamento(true);

    // costruiamo la query string solo con i filtri effettivamente impostati
    const parametri = new URLSearchParams();
    if (torneoId) parametri.append("torneoId", torneoId);
    if (squadraId) parametri.append("squadraId", squadraId);
    if (stato) parametri.append("stato", stato);

    fetch("/api/partite?" + parametri.toString())
      .then((res) => res.json())
      .then((data) => {
        setPartite(data);
        setErrore(null);
      })
      .catch(() => setErrore("Impossibile caricare le partite"))
      .finally(() => setCaricamento(false));
  }, [torneoId, squadraId, stato]);

  // svuota tutti i filtri in un colpo solo
  function azzeraFiltri() {
    setTorneoId("");
    setSquadraId("");
    setStato("");
  }

  return (
    <div>
      <div className="filtri-partite">
        {/* filtro per torneo */}
        <select value={torneoId} onChange={(e) => setTorneoId(e.target.value)}>
          <option value="">Tutti i tornei</option>
          {opzioni.tornei.map((t) => (
            <option key={t.id} value={t.id}>{t.nome}</option>
          ))}
        </select>

        {/* filtro per squadra (casa o trasferta) */}
        <select value={squadraId} onChange={(e) => setSquadraId(e.target.value)}>
          <option value="">Tutte le squadre</option>
          {opzioni.squadre.map((s) => (
            <option key={s.id} value={s.id}>{s.nome}</option>
          ))}
        </select>

        {/* filtro per stato della partita */}
        <select value={stato} onChange={(e) => setStato(e.target.value)}>
          <option value="">Tutti gli stati</option>
          <option value="SCHEDULED">Da giocare</option>
          <option value="PLAYED">Giocata</option>
        </select>

        <button onClick={azzeraFiltri}>Azzera filtri</button>
      </div>

      {/* messaggi di stato: caricamento, errore, elenco vuoto */}
      {caricamento && <p>Caricamento partite...</p>}
      {errore && <p style={{ color: "red" }}>{errore}</p>}
      {!caricamento && !errore && partite.length === 0 && <p>Nessuna partita trovata</p>}

      {/* tabella con le partite filtrate */}
      {!caricamento && !errore && partite.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Ora</th>
              <th>Torneo</th>
              <th>Casa</th>
              <th>Trasferta</th>
              <th>Risultato</th>
              <th>Stato</th>
              <th>Luogo</th>
            </tr>
          </thead>
          <tbody>
            {partite.map((p) => (
              <tr key={p.id}>
                <td>{p.data}</td>
                <td>{p.ora}</td>
                <td>{p.torneoNome}</td>
                <td>{p.squadraDiCasaNome}</td>
                <td>{p.squadraDiTrasfertaNome}</td>
                {/* risultato mostrato solo se la partita è stata giocata */}
                <td>{p.stato === "PLAYED" ? `${p.goalsHome} - ${p.goalsAway}` : "-"}</td>
                <td>{p.etichettaStato}</td>
                <td>{p.luogo}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

// montiamo il componente nel div#root definito nella pagina Thymeleaf
const contenitore = document.getElementById("root");
ReactDOM.createRoot(contenitore).render(<ElencoPartite />);
