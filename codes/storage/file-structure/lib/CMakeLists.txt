add_library(fs 
  buf.cc buf.h 
  buf_file.cc buf_file.h
  fs_string.cc fs_string.h
  rec_file.h
  idx_text.cc idx_text.h
  idx.tpp idx.h
  btreenode.tpp btree.tpp btree.h
  hash.cc hash.h)
target_link_libraries(fs ${LOG_LIBS})

add_executable(test_fs_string test_fs_string.cc fs_string.cc)
target_link_libraries (test_fs_string PUBLIC fs ${LOG_LIBS})
