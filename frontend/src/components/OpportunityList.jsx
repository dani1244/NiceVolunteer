import { useEffect, useState } from "react";
import { getOpenOpportunities } from "../services/opportunityService";

export default function OpportunityList() {
  const [opportunities, setOpportunities] = useState([]);

  useEffect(() => {
    getOpenOpportunities()
      .then(setOpportunities)
      .catch(err => console.error("Erro ao carregar oportunidades", err));
  }, []);

  if (opportunities.length === 0) {
    return <p>Sem oportunidades disponíveis.</p>;
  }

  return (
    <ul>
      {opportunities.map(o => (
        <li key={o.id}>
          <strong>{o.title}</strong><br />
          {o.location} – {o.points} pontos
        </li>
      ))}
    </ul>
  );
}
