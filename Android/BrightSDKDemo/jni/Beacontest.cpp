#include <jni.h>
#include "com_lc_testndk2_NativeClass.h"
#ifdef __cplusplus  //最好有这个，否则被编译器改了函数名字找不到不要怪我
extern "C" {
#endif
JNIEXPORT jboolean JNICALL Java_com_lc_testndk2_NativeClass_jniArrayAdd(
        JNIEnv * env, jclass, jintArray array, jint b) {

    jsize size = env->GetArrayLength(array);
//  jintArray sum=env->NewIntArray(2);

    jboolean isCopy;
    jint* pArray = (jint*) env->GetPrimitiveArrayCritical(array, &isCopy);
    for (int i = 0; i < size; i++)
        pArray[i] += b;
    env->ReleasePrimitiveArrayCritical(array, pArray, JNI_COMMIT);
    //env->ReleasePrimitiveArrayCritical(sum,pSum,JNI_COMMIT);

    return isCopy;
}

JNIEXPORT jintArray JNICALL Java_com_lc_testndk2_NativeClass_jnitArrayMul(
        JNIEnv * env, jclass, jintArray array, jint b) {

    jsize size = env->GetArrayLength(array);
    jintArray product = env->NewIntArray(size);
    jint* pArray = (jint*) env->GetPrimitiveArrayCritical(array, 0);
    jint* pProduct=(jint*)env->GetPrimitiveArrayCritical(product,0);
//  jintArray product = env->NewIntArray(size); //不能在这里创建！！因为上面的方法会使java进入critical region， 在这里创建的话虚拟机直接崩溃
    for (int i = 0; i < size; i++)
        pProduct[i] =pArray[i]* b;
    env->ReleasePrimitiveArrayCritical(array, pArray, JNI_COMMIT);
    env->ReleasePrimitiveArrayCritical(product,pProduct,JNI_COMMIT);
    return product;
}

#ifdef __cplusplus
}
#endif
