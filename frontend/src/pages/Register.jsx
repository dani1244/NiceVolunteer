import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    bio: "",
    skills: "",
    interests: ""
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const volunteerData = {
        ...formData,
        skills: formData.skills.split(",").map(s => s.trim()).filter(s => s),
        interests: formData.interests.split(",").map(i => i.trim()).filter(i => i)
      };

      await register(volunteerData);
      navigate("/login", {
        state: { message: "Registo realizado com sucesso! Faça login para continuar." }
      });
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao registar. Tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Registo</h2>
        <p className="subtitle">Junte-se à comunidade de voluntários da UA</p>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Nome Completo</label>
            <input
              id="name"
              name="name"
              type="text"
              value={formData.name}
              onChange={handleChange}
              required
              placeholder="João Silva"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email UA</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="joao.silva@ua.pt"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="••••••••"
              minLength="6"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="bio">Bio (opcional)</label>
            <textarea
              id="bio"
              name="bio"
              value={formData.bio}
              onChange={handleChange}
              placeholder="Conte um pouco sobre você..."
              rows="3"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="skills">Competências (separadas por vírgula)</label>
            <input
              id="skills"
              name="skills"
              type="text"
              value={formData.skills}
              onChange={handleChange}
              placeholder="Programação, Design, Fotografia"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="interests">Interesses (separados por vírgula)</label>
            <input
              id="interests"
              name="interests"
              type="text"
              value={formData.interests}
              onChange={handleChange}
              placeholder="Tecnologia, Educação, Ambiente"
              disabled={loading}
            />
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? "Registando..." : "Registar"}
          </button>
        </form>

        <p className="auth-footer">
          Já tem conta? <Link to="/login">Faça login aqui</Link>
        </p>
      </div>
    </div>
  );
}
