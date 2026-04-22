# 添加库
add_library(lib 
	error_functions.c
	get_num.c)
	
# 库的包含文件: 使用方的需求(usage requirement)
target_include_directories(lib
	INTERFACE ${CMAKE_CURRENT_SOURCE_DIR}
	)