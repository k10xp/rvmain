.PHONY: fmt test reset_tags clean

fmt:
	mvn prettier:write

test:
	cd rvapi && mvn test

reset_tags:
	git tag -l | xargs git tag -d

clean:
	rm -rf ./rvapi/target
	rm ./Data/repotags.db