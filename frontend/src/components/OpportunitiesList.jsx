import { useEffect, useState } from "react";
import api from "../api/api";

function OpportunitiesList() {
  const [opportunities, setOpportunities] = useState([]);

  useEffect(() => {
    api.get("/opportunities")
      .then(response => setOpportunities(response.data))
      .catch(error => console.error("Erro ao carregar oportunidades:", error));
  }, []);

  return (
    <div style={{ marginTop: "2rem" }}>
      <h2>Oportunidades Abertas</h2>

      {opportunities.length === 0 && <p>Sem oportunidades disponíveis.</p>}

      <ul>
        {opportunities.map(op => (
          <li key={op.id} style={{ marginBottom: "1rem" }}>
            <strong>{op.title}</strong><br/>
            Local: {op.location}<br/>
            Pontos: {op.points}<br/>
            Promotor: {op.promoter}<br/>
            Estado: {op.status}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default OpportunitiesList;
