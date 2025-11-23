cmake_minimum_required (VERSION 3.16)
project (file-structure)

# C99, C++11
set (CMAKE_C_STANDARD 99)
set (CMAKE_CXX_STANDARD 11)

# 库
## 日志库: https://github.com/gabime/spdlog
if(NOT TARGET spdlog)
    find_package(spdlog REQUIRED)
    if(spdlog_FOUND)
        MESSAGE(STATUS "Found spdlog: " ${spdlog_VERSION})
    endif(spdlog_FOUND)
endif()
set(LOG_LIBS PRIVATE spdlog::spdlog)
MESSAGE(STATUS ${LOG_LIBS})
## 本地库
include_directories("${PROJECT_SOURCE_DIR}/lib")
set (EXTRA_LIBS PUBLIC fs PRIVATE spdlog::spdlog)
MESSAGE(STATUS ${EXTRA_LIBS})
add_subdirectory (lib)


# 各子目录
add_subdirectory(test)
add_subdirectory(ch01)
add_subdirectory(ch02)
add_subdirectory(ch04)
add_subdirectory(ch07)
add_subdirectory(ch08)
add_subdirectory(ch09)
add_subdirectory(ch12)
