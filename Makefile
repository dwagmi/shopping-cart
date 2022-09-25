all: up

up:
	docker-compose up --build

db:
	docker-compose up -d database

down:
	docker-compose down --volumes --remove-orphans