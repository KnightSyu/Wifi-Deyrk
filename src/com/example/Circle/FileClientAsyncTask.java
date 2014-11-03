package com.example.Circle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FileClientAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private TextView statusText;
    private static final int SOCKET_TIMEOUT = 5000;
    private Socket socket;

    public FileClientAsyncTask(Context context,TextView statusText) {
        this.context = context;
        this.statusText = (TextView) statusText;
    }

    protected String doInBackground(String... params) {
        try {
        	socket = new Socket();
        	socket.bind(null);
        	socket.connect(new InetSocketAddress(params[1], Integer.parseInt(params[2])),SOCKET_TIMEOUT);
        	
        	String dataName = getRealPathFromURI(Uri.parse(params[0]));
            //String[] dataType = dataName.split("[.;\\s]+",2);
        	
        	ContentResolver cr = context.getContentResolver();  //取得當前應用的 ContentResolver instence
        	DataOutputStream os = new DataOutputStream(socket.getOutputStream());  //接收水
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(params[0])));
        	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        	byte[] byteArray = stream.toByteArray();
            //InputStream is = cr.openInputStream(Uri.parse(params[0])); //產出水 
            //copyFile(is, os);
        	os.writeInt(stream.size());
            os.write(byteArray);
            os.writeUTF(dataName);
            
            os.flush();
            
            socket.close();
            /*
            if (socket != null && socket.isConnected()){
            	socket.close();
            }
            */
            return "成功傳送文件！檔名："+dataName;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private void copyFile(InputStream is, DataOutputStream os) throws IOException {
    	
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
    
    public String getRealPathFromURI(Uri contentUri) {
    	String fileName = "";
    	ContentResolver cr = context.getContentResolver();
    	String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor metaCursor = cr.query(contentUri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    fileName = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return fileName;
    }
    
    protected void onProgressUpdate(Integer... progresses) { 
    	statusText.setText("傳送進度: " + progresses[0] + "%");
    }
    
    protected void onPostExecute(String result) {
        if (result != null) {
        	Toast.makeText(context, result, Toast.LENGTH_SHORT).show(); 
        }
    }
}