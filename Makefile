all: up

up:
	docker-compose up

down:
	docker-compose down --volumes --remove-orphans