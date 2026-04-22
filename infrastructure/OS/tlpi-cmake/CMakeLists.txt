cmake_minimum_required (VERSION 3.22)

project (tlpi-cmake)

# C17
set (CMAKE_C_STANDARD 17)

# 基本库
add_subdirectory(src/lib)
list(APPEND EXTRA_LIBS lib)

# 测试
add_subdirectory(test)

# 各子系统
add_subdirectory(src/process)
add_subdirectory(src/memory)
#add_subdirectory(src/io)
