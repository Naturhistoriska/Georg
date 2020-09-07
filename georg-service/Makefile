#!make

all: build

build:
	docker build -t georgnrm/georg-api:v0.3.4 .


# docker login
release:
	docker push georgnrm/georg-api:v0.3.4
