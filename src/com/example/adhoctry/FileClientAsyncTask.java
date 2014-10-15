package com.example.adhoctry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FileClientAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    //private TextView statusText;
    private static final int SOCKET_TIMEOUT = 5000;

    public FileClientAsyncTask(Context context) {
        this.context = context;
        //this.statusText = (TextView) statusText;
    }

    protected String doInBackground(String... params) {
        try {
        	Socket socket = new Socket();
        	socket.bind(null);
        	socket.connect(new InetSocketAddress(params[1], Integer.parseInt(params[2])),SOCKET_TIMEOUT);
        	
        	ContentResolver cr = context.getContentResolver();  //取得當前應用的 ContentResolver instence
            OutputStream os = socket.getOutputStream();  //接收水
            InputStream is = cr.openInputStream(Uri.parse(params[0])); //產出水 
            copyFile(is, os);
            
            socket.close();
            /*
            if (socket != null && socket.isConnected()){
            	socket.close();
            }
            */
            return "成功傳送文件！";
        } catch (IOException e) {
            return null;
        }
    }

    private void copyFile(InputStream is, OutputStream os) throws IOException {
    	
    	byte buf[] = new byte[1024];
		int length;
		
		try{
			while ((length = is.read(buf)) != -1) {
                os.write(buf, 0, length);
            }
			os.close();
			is.close();
		}catch(Exception e){
			
		}
		
    	/*
    	int ch = 0;
    	
    	try {
            while((ch=is.read()) != -1){
                os.write(ch);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally{
            os.close();
            is.close();
        }*/
	}
    
    protected void onPostExecute(String result) {
        if (result != null) {
        	Toast.makeText(context, result, Toast.LENGTH_SHORT).show(); 
        }
    }
}