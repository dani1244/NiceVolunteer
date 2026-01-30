import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

// Spike test: sudden increase in load
export const options = {
  stages: [
    { duration: '10s', target: 5 },   // Warm up
    { duration: '10s', target: 5 },   // Stay at 5 users
    { duration: '10s', target: 50 },  // SPIKE to 50 users
    { duration: '30s', target: 50 },  // Stay at spike
    { duration: '10s', target: 5 },   // Scale down
    { duration: '10s', target: 0 },   // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'], // 95% of requests should be below 1s during spike
    http_req_failed: ['rate<0.1'],     // Error rate should be less than 10% even during spike
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Simulate user browsing opportunities
  const response = http.get(`${BASE_URL}/api/opportunities/open`);

  const checkRes = check(response, {
    'status is 200': (r) => r.status === 200,
    'response time acceptable': (r) => r.timings.duration < 1000,
  });

  errorRate.add(!checkRes);

  sleep(0.5);
}
