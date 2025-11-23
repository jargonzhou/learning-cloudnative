lib := lib
modules := test

.PHONY: all $(lib) $(modules)
all: $(modules)

$(lib) $(modules): 
	$(MAKE) --directory=$@ $(GOAL)

$(modules): $(lib)

clean:
	$(MAKE) GOAL=clean