sources := error_functions.c get_num.c
objects := $(subst .c,.o,$(sources))
lib_tlpi := libtlpi.a
to_cleans += $(lib_tlpi) $(objects)

all: $(lib_tlpi)

include ../common.mk


$(lib_tlpi): $(objects)
	$(AR) $(ARFLAGS) $@ $^
