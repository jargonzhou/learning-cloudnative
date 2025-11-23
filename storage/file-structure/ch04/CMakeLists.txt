add_executable(test_person_wr test_person_wr.cc)

add_executable(test_buf test_buf.cc person_buf.cc )
target_link_libraries (test_buf ${EXTRA_LIBS})
