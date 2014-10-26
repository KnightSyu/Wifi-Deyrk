package com.example.Circle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FileServerAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    //private TextView statusText;
    private String dataName = "dataName";
    private Socket client;
    private Boolean rb;

    public FileServerAsyncTask(Context context) {
        this.context = context;
        //this.statusText = (TextView) statusText;
    }

    protected String doInBackground(String... params) {
    	if(isCancelled()) return null;
    	
        try {
        	ServerSocket serverSocket = new ServerSocket(Integer.parseInt(params[0]));
        	client = serverSocket.accept();
            
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                    + ".jpg");
            File dirs = new File(f.getParent());
            if (!dirs.exists()) {
                dirs.mkdirs();  //創建資料夾
            }
            f.createNewFile();
            InputStream inputstream = client.getInputStream();
            copyFile(inputstream, new FileOutputStream(f));
            /*
            InputStream inputstream2 = client.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputstream2);
            BufferedReader br = new BufferedReader(isr);
            dataName = br.readLine();*/
            client.close();
            serverSocket.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private void copyFile(InputStream is, FileOutputStream fos) throws IOException {
    	byte buf[] = new byte[1024];
		int length;
		
		try{
			while ((length = is.read(buf)) != -1) {
                fos.write(buf, 0, length);
            }
			fos.close();
			is.close();
		}catch(Exception e){
			
		}
		/*
    	int ch = 0;
    	
    	try {
            while((ch=is.read()) != -1){
                fos.write(ch);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally{
            fos.close();
            is.close();
        }*/
	}
    
    protected void onPostExecute(String result) {
        if (result != null) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + result), "image/*");
            context.startActivity(intent);
        }
        //Toast.makeText(context, "接收完成！"+result, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "接收完成！result:"+result, Toast.LENGTH_SHORT).show();
    }
}