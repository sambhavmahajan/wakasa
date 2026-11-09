# Wakasa
An email as a service application. Create an account, configure SMTP configuration, send post request to send email.

## System Design
<img width="1047" height="583" alt="image" src="https://github.com/user-attachments/assets/3f3e0684-6264-49ce-803e-d50d8f0e62bb" />

### Profiles
- dev and prod profiles, dev is used for testing and has h2 console by default.
- prod needs configuration.
- set PROFILE env to either dev or prod to change between them.

### .env
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

### Email, Password and Secret standards
- Apache Commons Validator EmailValidator class is used to validate the email, which uses RFC 822 standards.
- Password is salted with bycrypt.
- Secret(key) is generated using sha256(email + randomLongNumber)

### Run
```console
docker compose up --build
```
Configure ```spring.jpa.hibernate.ddl-auto``` property for dev or prod as create(initial) and later as validate(subsequent runs).

### Useage
- Create account
- Login account
- Insert required SMTP configuration in dashboard
- Click save

### API Request
JSON Payload:
```
{
  "key": "59650d3d0599ded000eafd880b6dd24dbfd140300426e870f80800cba176232c",
  "to": "70747596+sambhavmahajan@users.noreply.github.com",
  "subject": "test",
  "message": "test"
}
```
Type: POST <br>
Response codes: 400, 500, 200
