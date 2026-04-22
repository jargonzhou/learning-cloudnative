CFLAGS = -Wall -g 
#CFLAGS = -g -O
CPPFLAGS = -I../include
ARFLAGS = rv
RM = rm -rf *.dSYM
LDFLAGS = 
# put library link after source code
LDLIBS = -L../lib -lapue

vpath %.h ../include

# distinguish platform
PLATFORM=$(shell ../systype.sh)

ifeq "$(PLATFORM)" "linux"
# getaddrinfo()
#CFLAGS += -static -DLINUX -D_GNU_SOURCE -pthread
CFLAGS += -DLINUX -D_GNU_SOURCE -pthread
endif
ifeq "$(PLATFORM)" "macos"
CFLAGS += -DMACOS -D_DARWIN_C_SOURCE
endif
