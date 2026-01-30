package pt.ua.nicevolunteers.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pt.ua.nicevolunteers.application.repository.ApplicationRepository;
import pt.ua.nicevolunteers.promoter.domain.Promoter;
import pt.ua.nicevolunteers.promoter.repository.PromoterRepository;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final VolunteerRepository volunteerRepository;
    private final PromoterRepository promoterRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;

    public DataLoader(VolunteerRepository volunteerRepository,
                      PromoterRepository promoterRepository,
                      OpportunityRepository opportunityRepository,
                      ApplicationRepository applicationRepository) {
        this.volunteerRepository = volunteerRepository;
        this.promoterRepository = promoterRepository;
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Loading initial data...");

        createPromoters();
        createVolunteers();
        createOpportunities();

        logger.info("Initial data loaded successfully!");
    }

    private void createPromoters() {
        Promoter deti = new Promoter(
                "DETI - Departamento de Eletrónica, Telecomunicações e Informática",
                "deti@ua.pt",
                "SecurePass123"
        );
        deti.updateProfile(
                "Departamento de Engenharia da UA",
                "https://www.ua.pt/deti",
                "+351234370350"
        );

        Promoter aaua = new Promoter(
                "AAUAv - Associação Académica da Universidade de Aveiro",
                "aauav@ua.pt",
                "SecurePass123"
        );
        aaua.updateProfile(
                "Associação de Estudantes da UA",
                "https://www.aauav.pt",
                "+351234372300"
        );

        Promoter camara = new Promoter(
                "Câmara Municipal de Aveiro",
                "camara@ua.pt",
                "SecurePass123"
        );
        camara.updateProfile(
                "Parceiro institucional da UA",
                "https://www.cm-aveiro.pt",
                "+351234378300"
        );

        promoterRepository.save(deti);
        promoterRepository.save(aaua);
        promoterRepository.save(camara);

        logger.info("Created {} promoters", 3);
    }

    private void createVolunteers() {
        Volunteer joao = new Volunteer(
                "João Silva",
                "joao.silva@ua.pt",
                Integer.toHexString("Password123".hashCode())
        );
        joao.setBio("Estudante de Engenharia Informática apaixonado por tecnologia");
        joao.setSkills(Arrays.asList("Java", "Python", "React", "Design Gráfico"));
        joao.setInterests(Arrays.asList("Tecnologia", "Educação", "Ambiente"));

        Volunteer maria = new Volunteer(
                "Maria Santos",
                "maria.santos@ua.pt",
                Integer.toHexString("Password123".hashCode())
        );
        maria.setBio("Investigadora em Biologia Marinha");
        maria.setSkills(Arrays.asList("Biologia", "Investigação", "Fotografia"));
        maria.setInterests(Arrays.asList("Ambiente", "Ciência", "Divulgação"));

        Volunteer carlos = new Volunteer(
                "Carlos Oliveira",
                "carlos.oliveira@ua.pt",
                Integer.toHexString("Password123".hashCode())
        );
        carlos.setBio("Professor de Matemática na UA");
        carlos.setSkills(Arrays.asList("Matemática", "Mentoria", "Ensino"));
        carlos.setInterests(Arrays.asList("Educação", "Mentoria", "Comunidade"));

        volunteerRepository.save(joao);
        volunteerRepository.save(maria);
        volunteerRepository.save(carlos);

        logger.info("Created {} volunteers", 3);
    }

    private void createOpportunities() {
        Opportunity beach = new Opportunity(
                "Limpeza da Praia da Barra",
                "camara@ua.pt",
                "Voluntários para ajudar na limpeza da Praia da Barra. "
                        + "Atividade ao ar livre que promove a consciência ambiental. "
                        + "Material de limpeza fornecido.",
                15,
                "Praia da Barra, Aveiro",
                "Ambiente",
                LocalDate.now().plusDays(7),
                OpportunityStatus.OPEN
        );

        Opportunity mentoria = new Opportunity(
                "Mentoria para Alunos do 1º Ano",
                "deti@ua.pt",
                "Programa de mentoria para ajudar caloiros a adaptarem-se à universidade. "
                        + "Sessões semanais de 2 horas durante um semestre.",
                25,
                "Campus Universitário, DETI",
                "Educação",
                LocalDate.now().plusDays(14),
                OpportunityStatus.OPEN
        );

        Opportunity queima = new Opportunity(
                "Apoio à Organização da Queima das Fitas",
                "aauav@ua.pt",
                "Voluntários para apoiar a organização da Queima das Fitas. "
                        + "Funções incluem logística, apoio aos visitantes e coordenação de atividades.",
                20,
                "Centro de Aveiro",
                "Eventos",
                LocalDate.now().plusDays(30),
                OpportunityStatus.OPEN
        );

        Opportunity conferencia = new Opportunity(
                "Apoio à Conferência TechUA 2026",
                "deti@ua.pt",
                "Suporte técnico e logístico durante a conferência anual de tecnologia. "
                        + "Oportunidade de networking com profissionais da área.",
                30,
                "Auditório da Reitoria",
                "Tecnologia",
                LocalDate.now().plusDays(45),
                OpportunityStatus.OPEN
        );

        Opportunity visitas = new Opportunity(
                "Guia de Visitas ao Campus",
                "aauav@ua.pt",
                "Acompanhamento de grupos de futuros alunos em visitas guiadas ao campus. "
                        + "Partilha da experiência universitária.",
                10,
                "Campus Universitário",
                "Divulgação",
                LocalDate.now().plusDays(10),
                OpportunityStatus.OPEN
        );

        Opportunity bicicletas = new Opportunity(
                "Oficina de Reparação de Bicicletas",
                "camara@ua.pt",
                "Voluntários para ensinar reparação básica de bicicletas à comunidade. "
                        + "Promove mobilidade sustentável.",
                15,
                "Parque da Cidade, Aveiro",
                "Ambiente",
                LocalDate.now().plusDays(20),
                OpportunityStatus.OPEN
        );

        Opportunity hackathon = new Opportunity(
                "Organização do Hackathon UA",
                "deti@ua.pt",
                "Apoio à organização do maior hackathon da universidade. "
                        + "Funções incluem coordenação de equipas, logística e comunicação.",
                35,
                "DETI - Sala de Projetos",
                "Tecnologia",
                LocalDate.now().plusDays(60),
                OpportunityStatus.OPEN
        );

        Opportunity jardins = new Opportunity(
                "Manutenção dos Jardins do Campus",
                "camara@ua.pt",
                "Atividade de jardinagem e manutenção dos espaços verdes do campus. "
                        + "Contribui para um ambiente universitário mais agradável.",
                12,
                "Campus Universitário",
                "Ambiente",
                LocalDate.now().plusDays(5),
                OpportunityStatus.OPEN
        );

        opportunityRepository.save(beach);
        opportunityRepository.save(mentoria);
        opportunityRepository.save(queima);
        opportunityRepository.save(conferencia);
        opportunityRepository.save(visitas);
        opportunityRepository.save(bicicletas);
        opportunityRepository.save(hackathon);
        opportunityRepository.save(jardins);

        logger.info("Created {} opportunities", 8);
    }
}
