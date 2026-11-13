# Wakasa
An email as a service application. Create an account, configure SMTP configuration, send post request to send email.

## System Design
<img width="1047" height="583" alt="image" src="https://github.com/user-attachments/assets/3f3e0684-6264-49ce-803e-d50d8f0e62bb" />

## Profiles
- dev, prod, data-seeder profiles, dev is used for testing and has h2 console by default.
- prod needs configuration.
- data-seeder is used for seed 10,000 users
- set PROFILE env to either dev or prod to change between them.

## .env
```
REDIS_HOST=redis
REDIS_PORT=6379
DATABASE_URL=jdbc:postgresql://host/db
DATABASE_USERNAME=username
DATABASE_PASSWORD=password
DATABASE=postgresql
PORT=10000
PROFILE=dev
```

## Email, Password and Secret standards
- Apache Commons Validator EmailValidator class is used to validate the email, which uses RFC 822 standards.
- Password is salted with bycrypt.
- Secret(key) is generated using sha256(email + randomLongNumber)

## Run
```console
docker compose up --build
```
Configure ```spring.jpa.hibernate.ddl-auto``` property for dev or prod as create(initial) and later as validate(subsequent runs).

## Useage
- Create account
- Login account
- Insert required SMTP configuration in dashboard
- Click save

## API Request
JSON Payload:
```
{
  "key": "59650d3d0599ded000eafd880b6dd24dbfd140300426e870f80800cba176232c",
  "to": "70747596+sambhavmahajan@users.noreply.github.com",
  "subject": "test",
  "message": "test"
}
```
Endpoint: /api/V1 POST <br>
Response codes: 400, 500, 200

## Benchmarks
Explain Analyze run on last column of app_token, fetching by token value(tested on postgresql 18, seeded by UserAndTokenSeeder(see dataseeders package))
```sql
explain analyze
select * from app_token
where token_value='b6f21235a19d14b6b44b091d6d34d3f0e8510d1763b74c2b5bbee5c19d6df836'
```
| Test No. | Without Index (ms) | With Index (ms) |
|---:|---:|---:|
| 1 | 2.051 | 0.103 |
| 2 | 3.222 | 0.058 |
| 3 | 1.671 | 0.056 |
| 4 | 1.576 | 0.061 |
| 5 | 1.650 | 0.058 |
| 6 | 1.790 | 0.073 |
| 7 | 1.710 | 0.050 |
| 8 | 1.696 | 0.051 |
| 9 | 1.646 | 0.060 |
| 10 | 1.489 | 0.064 |
| **Mean** | **1.8501** | **0.0634** |

Performance Improvement:
- Average execution time reduced by 96.57%.
- Query execution is approximately 29.18× faster with the index.

## License

This project is licensed under the MIT License. See the ![LICENSE](/LICENSE) file for more details.
