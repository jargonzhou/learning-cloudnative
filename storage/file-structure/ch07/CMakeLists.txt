add_executable(test_recording test_recording.cc recording.cc )
target_link_libraries (test_recording ${EXTRA_LIBS})

add_executable(test_text_idx test_text_idx.cc recording.cc)
target_link_libraries (test_text_idx ${EXTRA_LIBS})

add_executable(test_text_idx_file test_text_idx_file.cc text_idx_file.h recording.cc)
target_link_libraries (test_text_idx_file ${EXTRA_LIBS})