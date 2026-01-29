package pt.ua.nicevolunteers.monitoring;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class VolunteerMetricsService {

    private final VolunteerRepository volunteerRepository;
    private final MeterRegistry meterRegistry;

    public VolunteerMetricsService(VolunteerRepository volunteerRepository,
                                    MeterRegistry meterRegistry) {
        this.volunteerRepository = volunteerRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {

        Gauge.builder("nicevolunteers.volunteers.count", volunteerRepository, repo -> repo.count())
                .description("Total number of registered volunteers")
                .register(meterRegistry);

        Gauge.builder("nicevolunteers.volunteers.total.points", volunteerRepository,
                repo -> repo.findAll()
                            .stream()
                            .mapToInt(v -> v.getPoints())
                            .sum())
                .description("Total points accumulated by all volunteers")
                .register(meterRegistry);
    }
}
