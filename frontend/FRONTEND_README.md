# NiceVolunteer Frontend

React application for the NiceVolunteer platform.

## Tech Stack

- **React 19** - UI framework
- **React Router v7** - Routing
- **Vite** - Build tool
- **Axios** - HTTP client
- **Cypress** - E2E testing
- **ESLint** - Code linting

## Getting Started

### Prerequisites
- Node.js 20+ (recommended)
- npm 9+

### Installation

```bash
npm install
```

### Development

```bash
npm run dev
```

Application will be available at `http://localhost:5173`

### Build

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

## Testing

### E2E Tests with Cypress

#### Run E2E tests (headless)
```bash
npm run test:e2e
```

#### Run E2E tests (headed - with browser UI)
```bash
npm run test:e2e:headed
```

#### Open Cypress Test Runner
```bash
npm run cypress:open
```

### Test Structure

```
cypress/
├── e2e/                    # E2E test specs
│   ├── register.cy.js      # User registration tests
│   ├── login.cy.js         # User login tests
│   └── opportunities.cy.js # Opportunities browsing tests
├── fixtures/               # Test data
├── support/
│   ├── commands.js         # Custom Cypress commands
│   └── e2e.js             # Support file
└── cypress.config.js       # Cypress configuration
```

### E2E Test Scenarios

#### Registration (`register.cy.js`)
- ✅ Display registration form
- ✅ Validate required fields
- ✅ Successfully register new user
- ✅ Show error for existing email
- ✅ Navigate to login page

#### Login (`login.cy.js`)
- ✅ Display login form
- ✅ Validate required fields
- ✅ Successfully login with valid credentials
- ✅ Show error for invalid credentials
- ✅ Navigate to register page

#### Opportunities (`opportunities.cy.js`)
- ✅ Display list of opportunities
- ✅ Show opportunity details
- ✅ Filter opportunities by search
- ✅ Navigate to opportunity details
- ✅ Apply to opportunity

## Code Quality

### Linting

```bash
npm run lint
```

ESLint is configured with:
- React plugin
- React Hooks plugin
- React Refresh plugin

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── Header.jsx
│   ├── Footer.jsx
│   └── Navigation.jsx
├── context/            # React Context providers
│   └── AuthContext.jsx
├── pages/              # Page components
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   ├── Opportunities.jsx
│   ├── OpportunityDetails.jsx
│   └── Profile.jsx
├── services/           # API services
│   ├── authService.js
│   └── opportunityService.js
├── App.jsx            # Main app component
├── main.jsx           # Entry point
└── index.css          # Global styles
```

## API Integration

The frontend communicates with the backend API at `http://localhost:8080`.

Key endpoints:
- `POST /api/auth/login` - User login
- `POST /api/volunteers` - User registration
- `GET /api/opportunities/open` - List open opportunities
- `POST /api/applications` - Apply to opportunity

## Environment Variables

Create a `.env` file:

```env
VITE_API_URL=http://localhost:8080
```

## CI/CD

E2E tests run automatically in GitHub Actions on:
- Pull requests to `main`
- Pushes to `main`

## Docker

The frontend is containerized and runs with Nginx in production.

Build:
```bash
docker build -t nicevolunteer-frontend .
```

Run:
```bash
docker run -p 80:80 nicevolunteer-frontend
```

## Contributing

1. Follow ESLint rules
2. Write E2E tests for new features
3. Ensure all tests pass before committing
4. Use conventional commit messages

## Troubleshooting

### Cypress won't start
- Ensure you're using Node 20+
- Clear Cypress cache: `npx cypress cache clear`

### API connection issues
- Check backend is running on port 8080
- Verify CORS configuration

### Build fails
- Clear node_modules: `rm -rf node_modules && npm install`
- Clear Vite cache: `rm -rf node_modules/.vite`
