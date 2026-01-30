import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// Custom metrics
const errorRate = new Rate('errors');

// Test configuration
export const options = {
  stages: [
    { duration: '30s', target: 10 },  // Ramp up to 10 users
    { duration: '1m', target: 10 },   // Stay at 10 users for 1 minute
    { duration: '30s', target: 20 },  // Ramp up to 20 users
    { duration: '1m', target: 20 },   // Stay at 20 users for 1 minute
    { duration: '30s', target: 0 },   // Ramp down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
    http_req_failed: ['rate<0.05'],   // Error rate should be less than 5%
    errors: ['rate<0.1'],             // Custom error rate should be less than 10%
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Test 1: Get all opportunities (most common read operation)
  let response = http.get(`${BASE_URL}/api/opportunities/open`);

  let checkRes = check(response, {
    'GET /opportunities/open - status is 200': (r) => r.status === 200,
    'GET /opportunities/open - response time < 500ms': (r) => r.timings.duration < 500,
  });

  errorRate.add(!checkRes);

  sleep(1);

  // Test 2: Create a volunteer (write operation)
  const volunteerPayload = JSON.stringify({
    name: `User ${__VU}_${__ITER}`,
    email: `user${__VU}_${__ITER}@ua.pt`,
    password: 'testpassword123',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  response = http.post(`${BASE_URL}/api/volunteers`, volunteerPayload, params);

  checkRes = check(response, {
    'POST /volunteers - status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    'POST /volunteers - response time < 800ms': (r) => r.timings.duration < 800,
  });

  errorRate.add(!checkRes);

  sleep(1);

  // Test 3: Get volunteer by ID (if creation was successful)
  if (response.status === 200 || response.status === 201) {
    const volunteer = JSON.parse(response.body);

    response = http.get(`${BASE_URL}/api/volunteers/${volunteer.id}`);

    checkRes = check(response, {
      'GET /volunteers/{id} - status is 200': (r) => r.status === 200,
      'GET /volunteers/{id} - response time < 300ms': (r) => r.timings.duration < 300,
    });

    errorRate.add(!checkRes);
  }

  sleep(1);
}
