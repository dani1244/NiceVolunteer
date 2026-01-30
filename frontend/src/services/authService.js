import api from "../api/api";

export const login = async (email, password) => {
  const response = await api.post("/api/auth/login", { email, password });
  return response.data;
};

export const register = async (volunteerData) => {
  const response = await api.post("/api/volunteers/register", volunteerData);
  return response.data;
};

export const getCurrentUser = async () => {
  const response = await api.get("/api/volunteers/me");
  return response.data;
};
