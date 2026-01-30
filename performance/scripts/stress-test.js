import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

// Stress test: gradually increase load until system breaks
export const options = {
  stages: [
    { duration: '1m', target: 20 },   // Ramp up to 20 users
    { duration: '2m', target: 40 },   // Ramp up to 40 users
    { duration: '2m', target: 60 },   // Ramp up to 60 users
    { duration: '2m', target: 80 },   // Ramp up to 80 users - stress point
    { duration: '1m', target: 100 },  // Push to 100 users
    { duration: '2m', target: 0 },    // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'], // Allow higher latency under stress
    http_req_failed: ['rate<0.2'],     // Allow up to 20% error rate at peak
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Test application workflow under stress

  // 1. Browse opportunities
  let response = http.get(`${BASE_URL}/api/opportunities/open`);
  check(response, {
    'browse opportunities - status ok': (r) => r.status === 200 || r.status === 401,
  });

  sleep(0.5);

  // 2. Create volunteer
  const volunteerPayload = JSON.stringify({
    name: `StressUser_${__VU}_${__ITER}`,
    email: `stress${__VU}_${__ITER}@ua.pt`,
    password: 'password123',
  });

  response = http.post(`${BASE_URL}/api/volunteers`, volunteerPayload, {
    headers: { 'Content-Type': 'application/json' },
  });

  const checkRes = check(response, {
    'create volunteer - not server error': (r) => r.status < 500,
  });

  errorRate.add(!checkRes);

  sleep(0.5);

  // 3. Attempt login
  const loginPayload = JSON.stringify({
    email: `stress${__VU}_${__ITER}@ua.pt`,
    password: 'password123',
  });

  response = http.post(`${BASE_URL}/api/auth/login`, loginPayload, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(response, {
    'login - not server error': (r) => r.status < 500,
  });

  sleep(1);
}
