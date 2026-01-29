package pt.ua.nicevolunteers.monitoring;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import pt.ua.nicevolunteers.volunteer.service.ApplicationService;

@Service
public class ApplicationMetricsService {

    private final ApplicationService applicationService;
    private final MeterRegistry meterRegistry;

    public ApplicationMetricsService(ApplicationService applicationService,
                                      MeterRegistry meterRegistry) {
        this.applicationService = applicationService;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {
        Gauge.builder("nicevolunteers.applications.count",
                applicationService,
                ApplicationService::countApplications)
                .description("Total number of applications")
                .register(meterRegistry);
    }
}
