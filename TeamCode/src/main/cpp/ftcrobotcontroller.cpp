#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("ftcrobotcontroller");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("ftcrobotcontroller")
//      }
//    }


extern "C"
JNIEXPORT jfloat JNICALL
Java_org_firstinspires_ftc_teamcode_FileTest_twoCharToFloat(JNIEnv *env, jobject thiz, jchar first,
                                                            jchar second) {
    // TODO: implement twoCharToFloat()
    char16_t chars[2] = {first, second};
    return *((float *)chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_FileTest_floatToTwoChar(JNIEnv *env, jobject thiz, jchar first,
                                                            jchar second) {
    // TODO: implement floatToTwoChar()

}