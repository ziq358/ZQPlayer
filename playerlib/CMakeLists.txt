cmake_minimum_required(VERSION 3.4.1)

message("CMake日志 ANDROID_ABI = ${ANDROID_ABI}")
message("CMake日志 CMAKE_CURRENT_SOURCE_DIR = ${CMAKE_CURRENT_SOURCE_DIR}")
message("CMake日志 CMAKE_SOURCE_DIR = ${CMAKE_SOURCE_DIR}")
# 当前文件存在的目录
#设置变量
set(JNI_LIBS_INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/libs/include)
set(JNI_LIBS_ABI_DIR ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI})
set(JNI_SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp)

#查找在某个路径下的所有源文件。赋值到 变量
aux_source_directory(${JNI_SOURCE_DIR} ALL_JNI_SOURCE_FILE)
message("CMake日志 ALL_JNI_SOURCE_FILE = ${ALL_JNI_SOURCE_FILE}")

#使用指定的源文件向工程中添加一个库。
add_library(player-lib SHARED ${ALL_JNI_SOURCE_FILE})
#将给定的路径添加到编译器搜索包含文件（.h文件）的路径列表中。
include_directories(${JNI_LIBS_INCLUDE_DIR})

add_library(avutil-55 SHARED IMPORTED)
#为一个目标设置属性 , 其中最重要的属性是IMPORTED_LOCATION，它指定了主库文件在磁盘上的位置。
set_target_properties(avutil-55 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libavutil-55.so)
message("CMake日志 libavutil = ${JNI_LIBS_ABI_DIR}/libavutil-55.so")

add_library(swresample-2 SHARED IMPORTED)
set_target_properties(swresample-2 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libswresample-2.so)
add_library(avcodec-57 SHARED IMPORTED)
set_target_properties(avcodec-57 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libavcodec-57.so)
add_library(avfilter-6 SHARED IMPORTED)
set_target_properties(avfilter-6 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libavfilter-6.so)
add_library(swscale-4 SHARED IMPORTED)
set_target_properties(swscale-4 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libswscale-4.so)
add_library(avformat-57 SHARED IMPORTED)
set_target_properties(avformat-57 PROPERTIES IMPORTED_LOCATION ${JNI_LIBS_ABI_DIR}/libavformat-57.so)
#查找一个库文件
find_library(log-lib log )
#将给定的库链接到一个目标上。
target_link_libraries( player-lib
                       avcodec-57
                       avformat-57
                       avutil-55
                       avfilter-6
                       swresample-2
                       swscale-4
                       -landroid # 解决ANativeWindow_fromSurface 找不到问题
                       ${log-lib} )