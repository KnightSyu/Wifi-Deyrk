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
import android.database.Cursor;
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
    ServerSocket serverSocket = null;
    Socket client = null;
    private int PORT;
    String[] title;
	String[] context_save;
	String[] kind;
	int[] size;
	byte[][] buffer;
	DB mDbHelper;
	int num = 0;

    public PushServerAsyncTask(Context context) {
        this.context = context;
    }

    protected String doInBackground(String... params) {
    	if(isCancelled()) return null;
    	
        try {
        	
        	//publishProgress();
        	PORT = Integer.parseInt(params[0]);
        	serverSocket = new ServerSocket(PORT);
        	client = serverSocket.accept();
        	
        	DataInputStream is = new DataInputStream(client.getInputStream());
        	
        	num = is.readInt();
        	
        	title = new String[num];
        	context_save = new String[num];
        	kind = new String[num];
        	size = new int[num];
        	buffer = new byte[num][];
        	
        	for(int i=0; i<num; i++){
        		title[i] = is.readUTF();
            	context_save[i] = is.readUTF();
            	kind[i] = is.readUTF();
            	size[i] = is.readInt();
            	buffer[i] = new byte[size[i]];
                int len = 0;
                while(len < size[i]){
                	len += is.read(buffer[i], len, size[i]-len);
                	//publishProgress((int) ((len / (float) size) * 100), size);
                }
        	}
            
            is.close();
            
            client.close();
            serverSocket.close();
        	
            return "接收推播訊息成功！";
        } catch (IOException e) {
            return e.getMessage();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
    
	@Override  
    protected void onProgressUpdate(Integer... progresses) {
		//statusText.setText("資料接收中，進度：" + progresses[0] + "%！檔案大小 "+df.format((float)(progresses[1]/(float)1048576))+" MB");
		Toast.makeText(context, "接收推播囉...", Toast.LENGTH_SHORT).show();
	}
	
    protected void onPostExecute(String result) {
        //Toast.makeText(context, "接收完成！"+result, Toast.LENGTH_SHORT).show();
        
    	mDbHelper = new DB(context);
    	mDbHelper.open();
        Cursor c;
        
        for(int i=0; i<num; i++){
        	Bitmap bitmap = BitmapFactory.decodeByteArray(buffer[i], 0, buffer[i].length);
            
            c = mDbHelper.getVsContext(context_save[i]);
            
            if(c.getCount()==0){
            	mDbHelper.create(title[i], context_save[i], bitmap, kind[i]);
            }
        }
    	
        try{
        	MainDeyrk.RM.setAdapter();
    	}catch(Exception e){
    		
    	}
    	
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        
        new PushServerAsyncTask(context).execute(PORT+"");
    }
}