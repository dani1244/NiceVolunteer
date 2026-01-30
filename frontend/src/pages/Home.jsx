import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="home-page">
      <section className="hero">
        <h1>Plataforma de Voluntariado da UA</h1>
        <p className="hero-subtitle">
          Conecte-se, contribua e faça a diferença na comunidade académica
        </p>

        <div className="hero-stats">
          <div className="stat-card">
            <div className="stat-value">500+</div>
            <div className="stat-label">Voluntários</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">150+</div>
            <div className="stat-label">Oportunidades</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">10k+</div>
            <div className="stat-label">Horas de Voluntariado</div>
          </div>
        </div>

        <div className="hero-actions">
          {isAuthenticated ? (
            <>
              <Link to="/opportunities" className="btn-primary btn-lg">
                Explorar Oportunidades
              </Link>
              <Link to="/profile" className="btn-secondary btn-lg">
                Meu Perfil
              </Link>
            </>
          ) : (
            <>
              <Link to="/register" className="btn-primary btn-lg">
                Começar Agora
              </Link>
              <Link to="/opportunities" className="btn-secondary btn-lg">
                Ver Oportunidades
              </Link>
            </>
          )}
        </div>
      </section>

      <section className="features">
        <h2>Como Funciona</h2>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">🎯</div>
            <h3>Encontre Oportunidades</h3>
            <p>Descubra atividades alinhadas com seus interesses e competências</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🤝</div>
            <h3>Participe e Contribua</h3>
            <p>Candidate-se e participe em eventos e projetos da comunidade UA</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">⭐</div>
            <h3>Acumule Pontos</h3>
            <p>Ganhe pontos por cada contribuição e atividade completada</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🎁</div>
            <h3>Resgate Benefícios</h3>
            <p>Troque pontos por benefícios, descontos e reconhecimento</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <h2>Pronto para fazer a diferença?</h2>
        <p>Junte-se à comunidade de voluntários da Universidade de Aveiro</p>
        {!isAuthenticated && (
          <Link to="/register" className="btn-primary btn-lg">
            Registar Agora
          </Link>
        )}
      </section>
    </div>
  );
}
