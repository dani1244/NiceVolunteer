package pt.ua.nicevolunteers.monitoring;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;

@Service
public class OpportunityMetricsService {

    private final OpportunityRepository opportunityRepository;
    private final MeterRegistry meterRegistry;

    public OpportunityMetricsService(OpportunityRepository opportunityRepository,
                                      MeterRegistry meterRegistry) {
        this.opportunityRepository = opportunityRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {

        Gauge.builder("nicevolunteers.opportunities.open",
                opportunityRepository,
                repo -> repo.findByStatus(OpportunityStatus.OPEN).size())
                .description("Number of open opportunities")
                .register(meterRegistry);

        Gauge.builder("nicevolunteers.opportunities.closed",
                opportunityRepository,
                repo -> repo.findByStatus(OpportunityStatus.CLOSED).size())
                .description("Number of closed opportunities")
                .register(meterRegistry);

        Gauge.builder("nicevolunteers.opportunities.completed",
                opportunityRepository,
                repo -> repo.findByStatus(OpportunityStatus.COMPLETED).size())
                .description("Number of completed opportunities")
                .register(meterRegistry);
    }
}
