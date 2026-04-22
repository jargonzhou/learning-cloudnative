add_executable(get_libc_version gnu/get_libc_version.c)

add_executable(test_lib lib/test_lib.c)
target_link_libraries(test_lib PUBLIC ${EXTRA_LIBS}) # 目标链接库