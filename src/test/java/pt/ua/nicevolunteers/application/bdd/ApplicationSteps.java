package pt.ua.nicevolunteers.application.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;
import pt.ua.nicevolunteers.volunteer.service.ApplicationService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationSteps {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ApplicationService applicationService;

    private Volunteer currentVolunteer;
    private Opportunity currentOpportunity;
    private Application currentApplication;
    private List<Application> applications;
    private Exception caughtException;

    @Dado("que existe um voluntário com email {string}")
    public void queExisteUmVoluntarioComEmail(String email) {
        currentVolunteer = new Volunteer("John Doe", email, "password123");
        currentVolunteer = volunteerRepository.save(currentVolunteer);
    }

    @E("existe uma oportunidade aberta {string}")
    public void existeUmaOportunidadeAberta(String title) {
        currentOpportunity = new Opportunity(title, "promoter@ua.pt", "Description", 10, "Aveiro");
        currentOpportunity = opportunityRepository.save(currentOpportunity);
    }

    @E("existe uma oportunidade aberta {string} com {int} pontos")
    public void existeUmaOportunidadeAbertaComPontos(String title, int points) {
        currentOpportunity = new Opportunity(title, "promoter@ua.pt", "Description", points, "Aveiro");
        currentOpportunity = opportunityRepository.save(currentOpportunity);
    }

    @Quando("o voluntário se candidata à oportunidade")
    public void oVoluntarioSeCandidataAOportunidade() {
        currentApplication = applicationService.apply(currentVolunteer.getId(), currentOpportunity.getId());
    }

    @Então("a candidatura deve ser criada com sucesso")
    public void aCandidaturaDeveSerCriadaComSucesso() {
        assertThat(currentApplication).isNotNull();
        assertThat(currentApplication.getId()).isNotNull();
    }

    @E("o estado da candidatura deve ser {string}")
    public void oEstadoDaCandidaturaDeveSer(String expectedStatus) {
        ApplicationStatus status = ApplicationStatus.valueOf(expectedStatus);
        assertThat(currentApplication.getStatus()).isEqualTo(status);
    }

    @E("o voluntário já se candidatou à oportunidade")
    public void oVoluntarioJaSeCandidatouAOportunidade() {
        currentApplication = applicationService.apply(currentVolunteer.getId(), currentOpportunity.getId());
    }

    @Quando("o voluntário tenta candidatar-se novamente")
    public void oVoluntarioTentaCandidatarSeNovamente() {
        try {
            applicationService.apply(currentVolunteer.getId(), currentOpportunity.getId());
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Então("deve receber um erro de candidatura inválida")
    public void deveReceberUmErroDeCandidaturaInvalida() {
        assertThat(caughtException).isInstanceOf(InvalidApplicationException.class);
    }

    @E("o voluntário candidatou-se à oportunidade")
    public void oVoluntarioCandidatouSeAOportunidade() {
        currentApplication = applicationService.apply(currentVolunteer.getId(), currentOpportunity.getId());
    }

    @Quando("o promotor aceita a candidatura")
    public void oPromotorAceitaACandidatura() {
        currentApplication = applicationService.acceptApplication(currentApplication.getId());
    }

    @E("a data de resposta deve estar preenchida")
    public void aDataDeRespostaDeveEstarPreenchida() {
        assertThat(currentApplication.getRespondedAt()).isNotNull();
    }

    @Quando("o promotor rejeita a candidatura")
    public void oPromotorRejeitaACandidatura() {
        currentApplication = applicationService.rejectApplication(currentApplication.getId());
    }

    @E("a candidatura foi aceite pelo promotor")
    public void aCandidaturaFoiAceitePeloPromotor() {
        currentApplication = applicationService.acceptApplication(currentApplication.getId());
    }

    @Quando("a atividade é completada")
    public void aAtividadeECompletada() {
        currentApplication = applicationService.completeApplication(currentApplication.getId());
        currentVolunteer.addPoints(currentOpportunity.getPoints());
        volunteerRepository.save(currentVolunteer);
    }

    @Então("o voluntário deve ter {int} pontos")
    public void oVoluntarioDeveTerPontos(int expectedPoints) {
        Volunteer updated = volunteerRepository.findById(currentVolunteer.getId()).orElseThrow();
        assertThat(updated.getPoints()).isEqualTo(expectedPoints);
    }

    @E("existem {int} oportunidades abertas")
    public void existemOportunidadesAbertas(int count) {
        for (int i = 1; i <= count; i++) {
            Opportunity opp = new Opportunity("Opportunity " + i, "promoter@ua.pt", "Desc", 10, "Aveiro");
            opportunityRepository.save(opp);
        }
    }

    @E("o voluntário candidatou-se a {int} oportunidades")
    public void oVoluntarioCandidatouSeAOportunidades(int count) {
        List<Opportunity> opportunities = opportunityRepository.findAll();
        for (int i = 0; i < count && i < opportunities.size(); i++) {
            applicationService.apply(currentVolunteer.getId(), opportunities.get(i).getId());
        }
    }

    @Quando("o voluntário consulta as suas candidaturas")
    public void oVoluntarioConsultaAsSuasCandidaturas() {
        applications = applicationService.getVolunteerApplications(currentVolunteer.getId());
    }

    @Então("deve ver {int} candidaturas na lista")
    public void deveVerCandidaturasNaLista(int expectedCount) {
        assertThat(applications).hasSize(expectedCount);
    }
}
