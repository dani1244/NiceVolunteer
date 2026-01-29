package pt.ua.nicevolunteers.monitoring;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import pt.ua.nicevolunteers.volunteer.repository.PointsTransactionRepository;

@Service
public class PointsMetricsService {

    private final PointsTransactionRepository repository;
    private final MeterRegistry meterRegistry;

    public PointsMetricsService(PointsTransactionRepository repository,
                                 MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {

        Gauge.builder("nicevolunteers.points.transactions.count",
                repository,
                repo -> repo.count())
                .description("Total number of points transactions")
                .register(meterRegistry);

        Gauge.builder("nicevolunteers.points.total",
                repository,
                repo -> repo.findAll()
                            .stream()
                            .mapToInt(t -> t.getPoints())
                            .sum())
                .description("Total number of points awarded")
                .register(meterRegistry);
    }
}
