
INCLUDE_TLPI := ../include
LIB_TLPI_DIR := ../lib
LIB_TLPI = tlpi

CC = gcc
CPPFLAGS += $(addprefix -I,$(INCLUDE_TLPI))
CFLAGS += -std=c99 -g -Wall -W

.PHONY: clean
clean:
	$(RM) $(to_cleans)