# Face Recognition Based on Android
release 0.9  


## Description
基于安卓的脸部识别。  
经过C++验证的脸部识别算法，和杭州艾芯智能AXON M2官方提供的安卓平台相结合。  
实现基于深度图象的人脸识别。  

## Software
Android Studio 3.14  

## Compiling
* Windows  
* Linux
* Mac  
## Version   
### V0.1  
* 原始框架  

### V0.5
* 通过 JNI 实现 Java 运行 C++ 代码
* 截取 OpenNI 的数据流并转换为 ByteBuffer传入 C++
* C++ 将 Java 传入的数据转换为 OpenCV 中的 Mat 类型数据
* C++ 处理后的图象转为 Bitmap 格式并显示

### V0.9
* 经过验证的 C++ 程序合并入工程
* 传入深度图象，通过 C++ 找到人脸并显示
