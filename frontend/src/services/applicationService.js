import api from "../api/api";

const applicationService = {
  /**
   * Apply to an opportunity
   * @param {string} volunteerId - UUID of the volunteer
   * @param {string} opportunityId - UUID of the opportunity
   * @returns {Promise} Application response
   */
  async apply(volunteerId, opportunityId) {
    const response = await api.post("/api/applications", {
      volunteerId,
      opportunityId
    });
    return response.data;
  },

  /**
   * Get applications for a volunteer
   * @param {string} volunteerId - UUID of the volunteer
   * @returns {Promise<Array>} List of applications
   */
  async getVolunteerApplications(volunteerId) {
    const response = await api.get(`/api/applications/volunteer/${volunteerId}`);
    return response.data;
  },

  /**
   * Get applications for an opportunity
   * @param {string} opportunityId - UUID of the opportunity
   * @returns {Promise<Array>} List of applications
   */
  async getOpportunityApplications(opportunityId) {
    const response = await api.get(`/api/applications/opportunity/${opportunityId}`);
    return response.data;
  }
};

export default applicationService;
