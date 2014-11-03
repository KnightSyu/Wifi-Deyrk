package com.example.Circle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PushServerAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private TextView statusText;
    private ServerSocket serverSocket = null;
    private Socket client = null;
    private int PORT = 8900;

    public PushServerAsyncTask(Context context, TextView statusText) {
        this.context = context;
        this.statusText = (TextView) statusText;
    }

    protected String doInBackground(String... params) {
    	if(isCancelled()){
    		try {
				client.close();
				serverSocket.close();
			} catch (IOException e) {
				
			}
    		return null;
    	}
    	
        try {
        	serverSocket = new ServerSocket(PORT);
        	client = serverSocket.accept();
        	
        	DataInputStream is = new DataInputStream(client.getInputStream());
        	String title = is.readUTF();
        	String context = is.readUTF();
        	String kind = is.readUTF();
            
        	int size = 0;
        	size = is.readInt();
        	byte[] buffer = new byte[size];
            int len = 0;
            while(len < size){
            	len += is.read(buffer, len, size-len);
            	publishProgress((int) ((len / (float) size) * 100), size);
            }
        	
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            
            client.close();
            serverSocket.close();
        	
            return "接收推播訊息成功！";
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    
	@Override  
    protected void onProgressUpdate(Integer... progresses) {
		//statusText.setText("資料接收中，進度：" + progresses[0] + "%！檔案大小 "+df.format((float)(progresses[1]/(float)1048576))+" MB");
    }
	
    protected void onPostExecute(String result) {
        //Toast.makeText(context, "接收完成！"+result, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        new PushServerAsyncTask(context, statusText).execute(PORT+"");
    }
}