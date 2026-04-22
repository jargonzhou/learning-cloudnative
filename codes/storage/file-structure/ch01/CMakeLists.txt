add_executable(test_person test_person.cc person.cc person.h)
target_link_libraries(test_person PRIVATE spdlog::spdlog)