docker-compose down
docker build -t backend-api-votacao:latest ./desafiovotacaofullstack
docker build -t desafio-votacao-fullstack-spa-app:latest ./desafio-votacao-fullstack-front
docker-compose up --build --force-recreate --remove-orphans