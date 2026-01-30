import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import applicationService from "../services/applicationService";
import pointsService from "../services/pointsService";

export default function Profile() {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [applications, setApplications] = useState([]);
  const [points, setPoints] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    loadProfileData();
  }, [isAuthenticated]);

  const loadProfileData = async () => {
    try {
      const [applications, pointsData] = await Promise.all([
        applicationService.getVolunteerApplications(user.id),
        pointsService.getBalance(user.id)
      ]);

      setApplications(applications);
      setPoints(pointsData.balance || 0);
    } catch (err) {
      console.error("Erro ao carregar perfil:", err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">A carregar perfil...</div>;
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div className="profile-info">
          <h1>{user.name}</h1>
          <p className="email">{user.email}</p>
          {user.bio && <p className="bio">{user.bio}</p>}
        </div>

        <div className="points-card">
          <div className="points-value">{points}</div>
          <div className="points-label">Pontos Acumulados</div>
        </div>
      </div>

      {user.skills && user.skills.length > 0 && (
        <div className="profile-section">
          <h2>Competências</h2>
          <div className="tags">
            {user.skills.map((skill, idx) => (
              <span key={idx} className="tag">
                {skill}
              </span>
            ))}
          </div>
        </div>
      )}

      {user.interests && user.interests.length > 0 && (
        <div className="profile-section">
          <h2>Interesses</h2>
          <div className="tags">
            {user.interests.map((interest, idx) => (
              <span key={idx} className="tag tag-secondary">
                {interest}
              </span>
            ))}
          </div>
        </div>
      )}

      <div className="profile-section">
        <h2>Minhas Candidaturas</h2>

        {applications.length === 0 ? (
          <div className="empty-state">
            <p>Ainda não se candidatou a nenhuma oportunidade.</p>
            <button onClick={() => navigate("/opportunities")} className="btn-primary">
              Explorar Oportunidades
            </button>
          </div>
        ) : (
          <div className="applications-list">
            {applications.map((app) => (
              <div key={app.id} className="application-card">
                <div className="application-header">
                  <h3>{app.opportunity.title}</h3>
                  <span className={`status-badge status-${app.status.toLowerCase()}`}>
                    {getStatusText(app.status)}
                  </span>
                </div>

                <div className="application-details">
                  <p>
                    <strong>Local:</strong> {app.opportunity.location}
                  </p>
                  <p>
                    <strong>Data:</strong>{" "}
                    {new Date(app.opportunity.date).toLocaleDateString("pt-PT")}
                  </p>
                  <p>
                    <strong>Pontos:</strong> {app.opportunity.points}
                  </p>
                  <p>
                    <strong>Candidatura:</strong>{" "}
                    {new Date(app.appliedAt).toLocaleDateString("pt-PT")}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

function getStatusText(status) {
  const statusMap = {
    PENDING: "Pendente",
    APPROVED: "Aprovada",
    REJECTED: "Rejeitada",
    COMPLETED: "Concluída"
  };
  return statusMap[status] || status;
}
