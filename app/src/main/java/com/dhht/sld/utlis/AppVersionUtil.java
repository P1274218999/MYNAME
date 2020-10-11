package com.dhht.sld.utlis;

import android.content.Context;
import android.content.pm.PackageManager;

import com.tamsiree.rxkit.RxDeviceTool;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/23  0:49
 * 文件描述：app版本检查管理
 */
public class AppVersionUtil {

    /**
     * 获取版本号名称
     */
    public static String getVerName(Context context) {
        String verName = RxDeviceTool.getAppVersionName(context);
        return verName;
    }


    /**
     * @param version1
     * @param version2
     * @return 0 相同 1 不相同
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals( version2 )) {
            return 0;
        }
        String[] version1Array = version1.split( "\\." );
        String[] version2Array = version2.split( "\\." );
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min( version1Array.length, version2Array.length );
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt( version1Array[index] )
                - Integer.parseInt( version2Array[index] )) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt( version1Array[i] ) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt( version2Array[i] ) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}
