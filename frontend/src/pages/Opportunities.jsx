import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getOpenOpportunities } from "../services/opportunityService";

export default function Opportunities() {
  const [opportunities, setOpportunities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadOpportunities();
  }, []);

  const loadOpportunities = async () => {
    try {
      const data = await getOpenOpportunities();
      setOpportunities(data);
    } catch (err) {
      setError("Erro ao carregar oportunidades");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">A carregar oportunidades...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="opportunities-page">
      <div className="page-header">
        <h1>Oportunidades de Voluntariado</h1>
        <p>Encontre oportunidades que correspondem ao seu perfil</p>
      </div>

      {opportunities.length === 0 ? (
        <div className="empty-state">
          <p>Não há oportunidades disponíveis no momento.</p>
        </div>
      ) : (
        <div className="opportunities-grid">
          {opportunities.map((opp) => (
            <div key={opp.id} className="opportunity-card">
              <div className="opportunity-header">
                <h3>{opp.title}</h3>
                <span className="points-badge">{opp.points} pontos</span>
              </div>

              <p className="opportunity-description">
                {opp.description?.substring(0, 150)}
                {opp.description?.length > 150 ? "..." : ""}
              </p>

              <div className="opportunity-details">
                <div className="detail-item">
                  <span className="icon">📍</span>
                  <span>{opp.location}</span>
                </div>
                <div className="detail-item">
                  <span className="icon">📅</span>
                  <span>{new Date(opp.date).toLocaleDateString("pt-PT")}</span>
                </div>
                {opp.category && (
                  <div className="detail-item">
                    <span className="icon">🏷️</span>
                    <span>{opp.category}</span>
                  </div>
                )}
              </div>

              <Link to={`/opportunities/${opp.id}`} className="btn-secondary">
                Ver Detalhes
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
