cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

include_directories(libs/include)

add_library(avutil-55
            SHARED
            IMPORTED)
set_target_properties(avutil-55
                      PROPERTIES IMPORTED_LOCATION
                      ../../../../libs/armeabi/libavutil-55.so)
add_library(swresample-2
             SHARED
             IMPORTED)
set_target_properties(swresample-2
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi/libswresample-2.so)

add_library(avcodec-57
            SHARED
            IMPORTED)
set_target_properties(avcodec-57
                      PROPERTIES IMPORTED_LOCATION
                      ../../../../libs/armeabi/libavcodec-57.so)
add_library(avfilter-6
              SHARED
              IMPORTED)
set_target_properties(avfilter-6
                        PROPERTIES IMPORTED_LOCATION
                        ../../../../libs/armeabi/libavfilter-6.so)
add_library(swscale-4
              SHARED
              IMPORTED)
set_target_properties(swscale-4
                        PROPERTIES IMPORTED_LOCATION
                        ../../../../libs/armeabi/libswscale-4.so)
add_library(avformat-57
            SHARED
            IMPORTED)
set_target_properties(avformat-57
                      PROPERTIES IMPORTED_LOCATION
                      ../../../../libs/armeabi/libavformat-57.so)

find_library( log-lib
              log )
target_link_libraries( native-lib
                       avcodec-57
                       avformat-57
                       avutil-55
                       avfilter-6
                       swresample-2
                       swscale-4
                       -landroid # 解决ANativeWindow_fromSurface 找不到问题
                       ${log-lib} )