package com.appsot.zqt.ip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 
 * @author vFISHv
 * @email vfishv@gmail.com
 *
 */
public class Util {
	
	private static final String TAG = Util.class.getSimpleName();
	private static StringBuilder sb = new StringBuilder();
	/**
     * 从ip的字符串形式得到字节数组形式
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
          //LogFactory.log("从ip的字符串形式得到字节数组形式报错", Level.ERROR, e);
          System.out.println("从ip的字符串形式得到字节数组形式报错");
        }
        return ret;
    }
    /**
     * @param ip ip的字节数组形式
     * @return 字符串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
	    sb.delete(0, sb.length());
    	sb.append(ip[0] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[1] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[2] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[3] & 0xFF);
    	return sb.toString();
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }
    
    /**
     * 将 assets 中的大文件复制到其他地方
     * @param Ctxt
     * @param DBFile
     * @throws IOException
     */
    public static void CopyDatabase(Context Ctxt, File DBFile) throws IOException
    {
        AssetManager am = Ctxt.getAssets();
        OutputStream os = new FileOutputStream(DBFile);
        DBFile.createNewFile();
        byte []b = new byte[1024];
        int i, r;
        String []Files = am.list("");
        Arrays.sort(Files);
//        for(i=1;i<10;i++)
        {
            String fn = IPActivity.QQWryFileName;//String.format("%d.db", i);
//            if(Arrays.binarySearch(Files, fn) < 0)
//                   break;
            InputStream is = am.open(fn);
            while((r = is.read(b)) != -1)
                os.write(b, 0, r);
            is.close();
        }
        os.close();
    }
    
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, "getLocalIpAddress. ", ex);
        }
        return "";
    }

	public String getLocalMacAddress(Context ctt)
	{
		WifiManager wifi = (WifiManager) ctt.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}

