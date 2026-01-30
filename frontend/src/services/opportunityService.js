import api from "../api/api";

export const getOpenOpportunities = async () => {
  const response = await api.get("/api/opportunities/open");
  return response.data;
};
