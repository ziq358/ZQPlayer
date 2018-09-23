cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib
             SHARED
             src/main/cpp/native-lib.cpp )


find_library( log-lib
              log )
target_link_libraries( native-lib
                       -landroid # 解决ANativeWindow_fromSurface 找不到问题
                       ${log-lib} )