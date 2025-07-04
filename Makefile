.PHONY: reset_tags

fmt:
	mvn prettier:write

reset_tags:
	git tag -l | xargs git tag -d

clean:
	rm -rf ./rvapi/target
	rm ./Data/repotags.db