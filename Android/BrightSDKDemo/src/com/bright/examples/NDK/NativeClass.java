package com.bright.examples.NDK;

import android.util.Log;

public class NativeClass {
	 //数组a中的每个元素都加上b，返回值为在C++中数据是否为a中数据拷贝得到的（按值拷贝还是传递指针）   
    public static native boolean jniArrayAdd(int[] a, int b);   
  // 在C++中创建Java中的int数组，其中元素为 数组a中的对应元素乘以b   
    public static native int[] jnitArrayMul(int[] a,int b);   
    static {   
        Log.i("NativeClass","before load library");   
        System.loadLibrary("Beacontest");//注意这里为自己指定的.so文件，无lib前缀，亦无后缀   
        Log.i("NativeClass","after load library");     
    }   
}
