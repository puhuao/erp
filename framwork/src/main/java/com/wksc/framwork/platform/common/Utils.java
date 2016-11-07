package com.wksc.framwork.platform.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;


import com.wksc.framwork.util.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Various static utility methods
 * Created by Gou Zhuang <gouzhuang@gmail.com> on 2014-11-2.
 */
public class Utils {
    public static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

    public static Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return ois.readObject();
    }

    public static byte[] ObjectToByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            oos = null;
            bytes = baos.toByteArray();
        } finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return bytes;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }



    public static int copy(final InputStream input, final OutputStream output)
            throws IOException {
        int count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        try {
            while (EOF != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
        } finally {
            input.close();
            output.close();
        }
        return count;
    }

    public static String encodeHttpBasicAuthHeader(String username, String password) {
        return "Basic " + Base64.encodeToString(
                (username + ':' + password).getBytes(Charset.forName("UTF-8")),
                Base64.DEFAULT);
    }


    /**
    public static String encodeHttpBasicAuthHeader(String username, String password,String appName) {
        return "Basic " + Base64.encodeToString(
                (username + ':' + password + ':' + appName).getBytes(Charset.forName("UTF-8")),
                Base64.DEFAULT);
     }*/


    public static String encodeHttpBasicAuthHeader(String username, String password, String appName) {
        return "Basic " + Base64Coder.encodeString
                (username + ':' + password + ':' + appName);
    }
}
