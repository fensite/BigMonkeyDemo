#include <jni.h>
#include <string>

#define BYTE unsigned char

extern "C" JNIEXPORT jstring JNICALL
Java_tv_newtv_screening_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jstring charTojstring(JNIEnv* env, const char* pat){
//定义java String类 strClass
    jclass strClass = (env)->FindClass("java/lang/String");
//获取String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
    jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
//建立byte数组
    jbyteArray bytes = (env)->NewByteArray(strlen(pat));
//将char* 转换为byte数组
    (env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*) pat);
// 设置String, 保存语言类型,用于byte数组转换至String时的参数
    jstring encoding = (env)->NewStringUTF("GB2312");
//将byte数组转换为java String,并输出
    return (jstring) (env)->NewObject(strClass, ctorID, bytes, encoding);
}



//jni获取mac地址
jstring getMacAddress(JNIEnv* env){
//通过JNI找到java中的NetworkInterface类
    jclass cls_networkInterface = env->FindClass("java/net/NetworkInterface");
    if (cls_networkInterface == 0) {
        return env->NewStringUTF("");
    }
//找到getByName方法
    jmethodID jmethodID1 = env->GetStaticMethodID(cls_networkInterface, "getByName", "(Ljava/lang/String;)Ljava/net/NetworkInterface;");
    if (jmethodID1 == 0)
        return env->NewStringUTF("");
    std::string ss = "wlan0";
    jstring jss2 = env->NewStringUTF(ss.c_str());
//调用getByname方法返回NetworkInterface的实例
    jobject jobject1 = env->CallStaticObjectMethod(cls_networkInterface, jmethodID1, jss2);
//找到getHardAddress方法
    jmethodID getHardwareAddress = env->GetMethodID(cls_networkInterface, "getHardwareAddress", "()[B");
    if (getHardwareAddress == 0)
        return env->NewStringUTF("");
//调用getHardAddress方法获取MAC地址的byte[]数组
    jbyteArray jbyte1 = (jbyteArray)env->CallObjectMethod(jobject1, getHardwareAddress);
//下面一些列流程就是讲byte[]数组转换成char类型字符在转换成字符串
    jbyte * olddata = (jbyte*)env->GetByteArrayElements(jbyte1, 0);
    jsize  oldsize = env->GetArrayLength(jbyte1);
// BYTE定义为  #define BYTE unsigned char
    BYTE* bytearr = (BYTE*)olddata;
    int len = (int)oldsize;

    char* data = (char*)env->GetByteArrayElements(jbyte1, 0);
    char *temp = new char[len*2 + 1];
    memset(temp,0,len*2 +1);
    for (int i = 0; i < len; i++) {
        char * buffer = new char[2];
        memset(buffer,2,0);
        sprintf(buffer, "%02X", data[i]);
        memcpy(temp+i*2, buffer, 2);
        delete[] (buffer);
    }
    jstring jMac = charTojstring(env, temp);
    delete[] temp;
    return jMac;
}
