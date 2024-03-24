package com.toponpaydcb.sdk.tool;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSave {
    public static String TAG = "Yole_FileSave";
    // 数据的写入
    public static boolean saveContentToFile(String filePath,String content,Context context) {
        // openFileOutput(要操作的文件名，文件访问模式)
        FileOutputStream fos = null;
        String msg = content;
        try { // 在操作文件的时候可能会报异常，需要进行捕获
            fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            fos.write(msg.getBytes());
            Log.d(TAG, "文件保存成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "文件保存失败：" + e.getMessage());
            return false;
        } finally {
            try {
                fos.close(); // 流是系统中的稀缺资源，在使用完后要及时关闭
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    // 用户数据的读取
    public static String[] readContent(String filePath,Context context){

        File file = new File(filePath);
        if (!file.exists()) {
            return new String[]{""};
        }

        // 获取文件输入流
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filePath);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 字节转换为字符串
            String msg = new String(buffer);
            Log.d(TAG, "文件读取内容："+msg );
            String[] userInfo = msg.split(":");
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "文件读取内容失败："+e.getMessage() );
            return new String[]{""};
        }
        finally {
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
