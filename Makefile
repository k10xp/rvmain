.PHONY: fmt test reset_tags build \
	dcu dcw \
	pcu pcw

fmt:
	cd rvapi && mvn prettier:write

test:
	cd rvapi && mvn test

reset_tags:
	git tag -l | xargs git tag -d

build:
	cd rvapi && ./mvnw clean install

dcu:
	cd rvapi && docker compose up --build

pcu:
	cd rvapi && podman compose up --build

dcw:
	cd rvapi && docker compose watch

pcw:
	cd rvapi && podman compose watch