import api from "../api/api";

const pointsService = {
  /**
   * Get points balance for a volunteer
   * @param {string} volunteerId - UUID of the volunteer
   * @returns {Promise<Object>} Points balance response with {volunteerId, balance}
   */
  async getBalance(volunteerId) {
    const response = await api.get(`/api/points/balance/${volunteerId}`);
    return response.data;
  },

  /**
   * Get points transaction history for a volunteer
   * @param {string} volunteerId - UUID of the volunteer
   * @returns {Promise<Array>} List of points transactions
   */
  async getHistory(volunteerId) {
    const response = await api.get(`/api/points/history/${volunteerId}`);
    return response.data;
  }
};

export default pointsService;
