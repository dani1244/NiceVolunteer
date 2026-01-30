import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import api from "../api/api";

export default function OpportunityDetails() {
  const { id } = useParams();
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [opportunity, setOpportunity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [applying, setApplying] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    loadOpportunity();
  }, [id]);

  const loadOpportunity = async () => {
    try {
      const response = await api.get(`/api/opportunities/${id}`);
      setOpportunity(response.data);
    } catch (err) {
      setError("Erro ao carregar oportunidade");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleApply = async () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    setApplying(true);
    setError("");
    setSuccess("");

    try {
      await api.post(`/api/opportunities/${id}/apply`, {
        volunteerId: user.id
      });
      setSuccess("Candidatura submetida com sucesso!");
      setTimeout(() => navigate("/profile"), 2000);
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao candidatar-se");
    } finally {
      setApplying(false);
    }
  };

  if (loading) {
    return <div className="loading">A carregar...</div>;
  }

  if (error && !opportunity) {
    return <div className="error-message">{error}</div>;
  }

  if (!opportunity) {
    return <div className="error-message">Oportunidade não encontrada</div>;
  }

  return (
    <div className="opportunity-details-page">
      <button onClick={() => navigate(-1)} className="btn-back">
        ← Voltar
      </button>

      <div className="opportunity-details-card">
        <div className="details-header">
          <h1>{opportunity.title}</h1>
          <span className="points-badge-large">{opportunity.points} pontos</span>
        </div>

        {success && <div className="success-message">{success}</div>}
        {error && <div className="error-message">{error}</div>}

        <div className="details-section">
          <h3>Descrição</h3>
          <p>{opportunity.description}</p>
        </div>

        <div className="details-grid">
          <div className="details-section">
            <h3>Informações</h3>
            <ul className="info-list">
              <li>
                <strong>Local:</strong> {opportunity.location}
              </li>
              <li>
                <strong>Data:</strong>{" "}
                {new Date(opportunity.date).toLocaleDateString("pt-PT")}
              </li>
              {opportunity.category && (
                <li>
                  <strong>Categoria:</strong> {opportunity.category}
                </li>
              )}
              {opportunity.duration && (
                <li>
                  <strong>Duração:</strong> {opportunity.duration}
                </li>
              )}
            </ul>
          </div>

          {opportunity.requiredSkills && opportunity.requiredSkills.length > 0 && (
            <div className="details-section">
              <h3>Competências Necessárias</h3>
              <div className="tags">
                {opportunity.requiredSkills.map((skill, idx) => (
                  <span key={idx} className="tag">
                    {skill}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>

        {opportunity.status === "OPEN" && (
          <button
            onClick={handleApply}
            className="btn-primary"
            disabled={applying || !isAuthenticated}
          >
            {applying
              ? "A candidatar..."
              : isAuthenticated
              ? "Candidatar-me"
              : "Faça login para candidatar-se"}
          </button>
        )}
      </div>
    </div>
  );
}
