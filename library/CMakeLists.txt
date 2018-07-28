cmake_minimum_required(VERSION 3.4.1)

file(GLOB decoder_src jni/*.c)


add_library(gpuimage-library SHARED
            ${decoder_src}
            )

#支持-std=gnu++11
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

#判断编译器类型,如果是gcc编译器,则在编译选项中加入c++11支持
#if(CMAKE_COMPILER_IS_GNUCXX)
#   set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
#    message(STATUS "optional:-std=c++11")
#endif(CMAKE_COMPILER_IS_GNUCXX)

find_library(ndk-lib
             log
             )

target_link_libraries(gpuimage-library
                     android
					 ${ndk-lib}
					  )