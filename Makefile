.PHONY: reset_tags build

reset_tags:
	git tag -l | xargs git tag -d

build:
	cd rvapi && ./mvnw clean install