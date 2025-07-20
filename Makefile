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
	$(call cdr) && mvn test

build:
	$(call cdr) && ./mvnw clean install

# dcu:
# 	$(call cdr) && docker compose up --build

# pcu:
# 	$(call cdr) && podman compose up --build

dcw:
	$(call cdr) && $(call cw,docker)

pcw:
	$(call cdr) && $(call cw,podman)

reset_tags:
	git tag -l | xargs git tag -d

clean:
	$(call cdr) && rm -rf target