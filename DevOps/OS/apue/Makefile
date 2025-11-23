TARGETS = lib ch07 ch08 ch10 ch11 ch13 ch16

all: $(TARGETS)
	for t in $(TARGETS); do \
		(echo "making $$t:" && $(MAKE) --directory=$$t) || exit 1; \
	done

clean: $(TARGETS)
	for t in $(TARGETS); do \
		(echo "cleaing $$t:" && $(MAKE) --directory=$$t clean) || exit 1; \
	done

	@echo "cleaning log and pid file:"
	$(RM) *.log *.pid
