add_executable(test_btree test_btree.cc)
target_link_libraries (test_btree ${EXTRA_LIBS})

add_executable(debug_btree debug_btree.cc)
target_link_libraries (debug_btree ${EXTRA_LIBS})