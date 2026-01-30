# Performance Testing with k6

This directory contains k6 performance test scripts for the NiceVolunteer application.

## Prerequisites

Install k6:
```bash
# macOS
brew install k6

# Linux
sudo gpg -k
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6

# Windows
choco install k6
```

## Test Scripts

### 1. Load Test (`load-test.js`)
Tests normal application load with gradual ramp-up.

**Run:**
```bash
k6 run performance/scripts/load-test.js
```

**Stages:**
- Ramp up to 10 users (30s)
- Maintain 10 users (1m)
- Ramp up to 20 users (30s)
- Maintain 20 users (1m)
- Ramp down (30s)

**Thresholds:**
- 95% of requests < 500ms
- Error rate < 5%

### 2. Spike Test (`spike-test.js`)
Tests application behavior under sudden traffic spikes.

**Run:**
```bash
k6 run performance/scripts/spike-test.js
```

**Stages:**
- Warm up to 5 users (10s)
- **SPIKE to 50 users** (10s)
- Maintain spike (30s)
- Scale down (20s)

**Thresholds:**
- 95% of requests < 1s
- Error rate < 10%

### 3. Stress Test (`stress-test.js`)
Gradually increases load to find system breaking point.

**Run:**
```bash
k6 run performance/scripts/stress-test.js
```

**Stages:**
- Progressive ramp: 20 → 40 → 60 → 80 → 100 users
- Total duration: ~10 minutes

**Thresholds:**
- 95% of requests < 2s
- Error rate < 20% at peak

## Running Tests

### Against Local Server
```bash
# Start the application first
mvn spring-boot:run

# In another terminal, run tests
k6 run performance/scripts/load-test.js
```

### Against Custom URL
```bash
k6 run -e BASE_URL=http://localhost:8080 performance/scripts/load-test.js
```

### With Custom Options
```bash
# Run with 50 virtual users for 2 minutes
k6 run --vus 50 --duration 2m performance/scripts/load-test.js
```

## Interpreting Results

k6 outputs several metrics:

- **http_req_duration**: Response time (p95 is important)
- **http_req_failed**: Percentage of failed requests
- **iterations**: Number of test iterations completed
- **vus**: Number of virtual users active

### Example Good Result:
```
✓ http_req_duration..........: avg=245ms  p(95)=450ms
✓ http_req_failed............: 1.2%
✓ iterations.................: 1500
```

### Example Bad Result:
```
✗ http_req_duration..........: avg=1.2s   p(95)=3.5s
✗ http_req_failed............: 15%
```

## CI/CD Integration

To run in GitHub Actions, add to `.github/workflows/performance.yml`:

```yaml
- name: Install k6
  run: |
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
    echo "deb https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
    sudo apt-get update
    sudo apt-get install k6

- name: Run Performance Tests
  run: k6 run performance/scripts/load-test.js
```

## Grafana Dashboards

k6 can export results to InfluxDB/Grafana for visualization:

```bash
k6 run --out influxdb=http://localhost:8086/k6 performance/scripts/load-test.js
```

## Best Practices

1. **Always warm up**: Include a ramp-up phase
2. **Test realistic scenarios**: Mix read/write operations
3. **Set appropriate thresholds**: Based on SLAs
4. **Monitor resources**: CPU, memory, database connections
5. **Run regularly**: Detect performance regressions early
