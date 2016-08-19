package com.yxh.ryt.util;

import android.content.Context;  
import android.widget.Toast;  
  
/** 
 * Toastͳһ������ 
 *  
 */  
public class ToastUtil  
{  
  
    private ToastUtil()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    }  
  
    public static boolean isShow = true;  
  
    /** 
     * ��ʱ����ʾToast 
     *  
     * @param context 
     * @param message 
     */  
    public static void showShort(Context context, CharSequence message)  
    {  
        if (isShow)  
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();  
    }  
  
    /** 
     * ��ʱ����ʾToast 
     *  
     * @param context 
     * @param message 
     */  
    public static void showShort(Context context, int message)  
    {  
        if (isShow)  
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();  
    }  
  
    /** 
     * ��ʱ����ʾToast 
     *  
     * @param context 
     * @param message 
     */  
    public static void showLong(Context context, CharSequence message)  
    {  
        if (isShow)  
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();  
    }  
  
    /** 
     * ��ʱ����ʾToast 
     *  
     * @param context 
     * @param message 
     */  
    public static void showLong(Context context, int message)  
    {  
        if (isShow)  
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();  
    }  
  
    /** 
     * �Զ�����ʾToastʱ�� 
     *  
     * @param context 
     * @param message 
     * @param duration 
     */  
    public static void show(Context context, CharSequence message, int duration)  
    {  
        if (isShow)  
            Toast.makeText(context, message, duration).show();  
    }  
  
    /** 
     * �Զ�����ʾToastʱ�� 
     *  
     * @param context 
     * @param message 
     * @param duration 
     */  
    public static void show(Context context, int message, int duration)  
    {  
        if (isShow)  
            Toast.makeText(context, message, duration).show();  
    }  
  
}  
