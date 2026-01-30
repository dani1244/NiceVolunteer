import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navigation() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          <span className="logo-icon">🤝</span>
          <span className="logo-text">NiceVolunteers</span>
        </Link>

        <div className="nav-links">
          <Link to="/opportunities" className="nav-link">
            Oportunidades
          </Link>

          {isAuthenticated ? (
            <>
              <Link to="/profile" className="nav-link">
                Perfil
              </Link>
              <div className="nav-user">
                <span className="user-name">{user?.name}</span>
                <button onClick={handleLogout} className="btn-logout">
                  Sair
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">
                Login
              </Link>
              <Link to="/register" className="btn-primary btn-sm">
                Registar
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
