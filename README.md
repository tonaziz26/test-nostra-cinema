## How To Run Locally

Clone the project

Go to the project directory

```bash
  cd test-back-end/  
```

Open docker in your pc 

Run docker compose

```bash
  docker-compose up -d
```

Build project with 
```bash
  mvn clean package
```

Run project with 

```bash
  mvn spring-boot:run
```

After finish using project stop the project
After that stop docker compose with

```bash
  docker-compose down
```
