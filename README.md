# RatesRisk

RatesRisk is a small interest-rate P&L calculator for bond and futures positions. Add a position with its previous and current market prices, then review the result by trade, desk, product family, or across the whole book.

The project uses Java 17, Spring Boot, PostgreSQL, Docker Compose, and JUnit 5.

## Run it locally

You need Docker Desktop with Compose. From this folder:

1. Copy `.env.example` to `.env`.
2. Choose local values for `APP_USER`, `APP_PASSWORD`, `DATABASE_USER`, and `DATABASE_PASSWORD` in `.env`. Keep `.env` private; Git ignores it.
3. Start the app:

   ```sh
   docker compose up --build
   ```

Open [http://localhost:8081](http://localhost:8081). Sign in using the `APP_USER` and `APP_PASSWORD` values from your `.env` file. To stop the app, run `docker compose down`.

For a direct JVM run, use JDK 17 and Maven 3.9+, set the same environment variables, then run `mvn spring-boot:run`.

## What the calculator does

It treats quantity as signed: positive is long and negative is short. Prices are points for comparable market observations of the same instrument. Results are rounded to two decimal places using HALF_UP.

| Product | P&L formula | Price convention |
| --- | --- | --- |
| Bond | quantity × face value × (current price − previous price) ÷ 100 | Price quoted per 100 face value |
| Future | quantity × contract multiplier × (current price − previous price) | Multiplier is currency value per price point |

For example, 10 bonds with 1,000 face value moving from 99.50 to 99.625 produce **12.50**. Five futures with a 1,000 multiplier moving from 98.00 to 98.50 produce **2,500.00**.

This is price P&L only. It does not include accrued interest, coupons, carry, fees, FX, convexity, or risk sensitivities such as DV01.

## Dashboard and API

The dashboard lets you sign in, add a bond or future, and view the P&L totals. The API is also available directly:

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/positions` | Save a position |
| `GET` | `/api/positions` | List positions |
| `GET` | `/api/pnl` | View trade results and desk/family totals |
| `GET` | `/actuator/health` | Check service health |

Business endpoints use HTTP Basic authentication in the local profile. For example:

```sh
curl -u "$APP_USER:$APP_PASSWORD" http://localhost:8081/api/pnl
```

Example bond request:

```json
{
  "tradeId": "UST-10Y-001",
  "desk": "Rates-US",
  "productFamily": "Government Bond",
  "productType": "BOND",
  "quantity": 10,
  "faceValue": 1000,
  "previousPrice": 99.50,
  "currentPrice": 99.625
}
```

For a future, set `productType` to `FUTURE`, provide `contractMultiplier`, and leave out `faceValue`. Invalid requests return HTTP 400; API requests without valid credentials return HTTP 401.

## Tests and CI

Run the unit and regression tests with:

```sh
mvn verify
```

The suite has 46 JUnit test invocations, including 40 parameterized futures regression cases and hand-calculated bond/futures examples. GitHub Actions runs the Maven verification job on pushes and pull requests.

## Production profile

The `prod` Spring profile uses OAuth 2.0 resource-server authentication. It accepts JWT access tokens only when signature, issuer, time claims, and the configured audience are valid. Set `SPRING_PROFILES_ACTIVE=prod`, `OIDC_ISSUER_URI`, and `OIDC_AUDIENCE`; API clients send `Authorization: Bearer <token>`.

The Kubernetes example in [`k8s/deployment.yaml`](k8s/deployment.yaml) expects an external PostgreSQL database and a pre-created Kubernetes Secret named `ratesrisk-db` with a `password` key. Connect that secret to your cloud secret manager, enable secret encryption at rest, replace the sample image/database/identity-provider values, and expose the service through your TLS-enabled ingress. The local dashboard is for development; production is API-first.

The repository does not contain production credentials or an identity-provider configuration. You must supply those environment-specific values before deploying.
