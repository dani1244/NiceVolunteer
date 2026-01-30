import { useEffect, useState } from "react";
import api from "../services/api";

export default function OpportunitiesPage() {
  const [opportunities, setOpportunities] = useState([]);

  useEffect(() => {
    api.get("/opportunities")
      .then(res => setOpportunities(res.data))
      .catch(err => console.error("Erro ao ir ao backend:", err));
  }, []);

  return (
    <main style={{ padding: "1rem" }}>
      <h2>Oportunidades Abertas</h2>

      {opportunities.length === 0 && (
        <p>Sem oportunidades disponíveis.</p>
      )}

      <ul>
        {opportunities.map(o => (
          <li key={o.id}>
            <strong>{o.title}</strong> — {o.location} ({o.points} pontos)
          </li>
        ))}
      </ul>
    </main>
  );
}
