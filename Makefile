.PHONY: fmt test reset_tags build clean \
	dcu dcw pcu pcw

define cdr
	cd rvapi
endef

#replace with docker or podman
define cw
	$1 compose watch
endef

# fmt:
# 	cdr && mvn prettier:write

test:
	cdr && mvn test

build:
	cdr && ./mvnw clean install

# dcu:
# 	cdr && docker compose up --build

# pcu:
# 	cdr && podman compose up --build

dcw:
	cdr && $(call cw,docker)

pcw:
	cdr && $(call cw,podman)

reset_tags:
	git tag -l | xargs git tag -d

clean:
	cdr && rm -rf target