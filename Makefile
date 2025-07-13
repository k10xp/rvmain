.PHONY: reset_tags build dcu pcu

reset_tags:
	git tag -l | xargs git tag -d

build:
	cd rvapi && ./mvnw clean install

dcu:
	cd rvapi && docker compose up --build

pcu:
	cd rvapi && podman compose up --build