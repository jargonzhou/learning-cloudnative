add_executable(test_str_seq test_str_seq.cc str_seq.cc)
target_link_libraries (test_str_seq ${EXTRA_LIBS})

add_executable(test_ledger test_ledger.cc ledger.cc ledger.h)
target_link_libraries (test_ledger ${EXTRA_LIBS})