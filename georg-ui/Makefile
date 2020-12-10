#!make

all: build

build:
	docker build -t georgnrm/georg-ui:v1.2.0 .


# docker login
release:
	docker push georgnrm/georg-ui:v1.2.0
