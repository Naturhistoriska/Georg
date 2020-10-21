#!make

all: build

build:
	docker build -t georgnrm/georg-api:v1.0 .


# docker login
release:
	docker push georgnrm/georg-api:v1.0
