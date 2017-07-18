package com.example.com.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketUitl {

    /**
     * 客户端代码
     *
     * @param IP
     * @param context
     * @return
     */
    public static int Client(String IP, String context) {
        try {
            Socket socket = new Socket(IP, 1243);
            socket.setSoTimeout(10000);
            Log.e("socket", "客户端已启动");
            OutputStream os = socket.getOutputStream();

            PrintWriter pw = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(os)));
            pw.write(context);
            pw.flush();
            socket.shutdownOutput();
            pw.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 服务端代码
     * @return
     */

    public static String sSocket() {
        StringBuffer sb = null;
        try {
            ServerSocket serverSocket = new ServerSocket(1243);
            Socket client = serverSocket.accept();
            InputStream is = client.getInputStream();
            sb=new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            client.shutdownOutput();
            br.close();
            is.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }
}
