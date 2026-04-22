include ../common.mk

SOURCES = log.c error.c print.c sync.c thd.c fd.c sock.c
OBJECTS = $(subst .c,.o,$(SOURCES))
LIB = libapue.a

all: $(OBJECTS)
	$(AR) $(ARFLAGS) $(LIB) $^

clean:
	$(RM) $(OBJECTS) $(LIB)
