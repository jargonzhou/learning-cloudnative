TARGET = main
sources := $(TARGET).c
to_cleans += $(TARGET)

$(TARGET): $(sources)
	$(CC) $(CPPFLAGS) $(CFLAGS) -o $@ $^ -L$(LIB_TLPI_DIR) -l$(LIB_TLPI)

include ../common.mk
