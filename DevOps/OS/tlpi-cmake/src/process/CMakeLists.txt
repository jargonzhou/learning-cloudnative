add_executable(process_id process_id.c)
add_executable(mem_segments mem_segments.c)
add_executable(necho necho.c)
add_executable(display_env display_env.c)

add_executable(modify_env modify_env.c)
target_link_libraries(modify_env PUBLIC ${EXTRA_LIBS})

add_executable(longjmp longjmp.c)
add_executable(setjmp_vars setjmp_vars.c)