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

public class FileServerAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private TextView statusText;
    private String dataName = "dataName";
    private ServerSocket serverSocket = null;
    private Socket client = null;
    private Boolean rb;
    private int PORT;
    private String[] dataType;
    DecimalFormat df = new DecimalFormat("#.##");

    public FileServerAsyncTask(Context context, TextView statusText) {
        this.context = context;
        this.statusText = (TextView) statusText;
    }
    
    public FileServerAsyncTask(Context context) {
        this.context = context;
    }

    protected String doInBackground(String... params) {
    	if(isCancelled()) return null;
    	
        try {
        	PORT = Integer.parseInt(params[0]);
        	serverSocket = new ServerSocket(PORT);
        	client = serverSocket.accept();
            
        	File f;
        	DataInputStream is = new DataInputStream(client.getInputStream());
        	String name = is.readUTF();
        	dataType = name.split("[.;\\s]+",2);
            
        	int size = 0;
        	size = is.readInt();
        	byte[] buffer = new byte[size];
            int len = 0;
            while(len < size){
            	len += is.read(buffer, len, size-len);
            	publishProgress((int) ((len / (float) size) * 100), size);
            }
        	
            if(dataType[1].equals("jpg")){
            	
            	Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            	f = new File(Environment.getExternalStorageDirectory() + "/"
                        + context.getPackageName() + "/" + name);
                File dirs = new File(f.getParent());
                if (!dirs.exists()) {
                    dirs.mkdirs();  //創建資料夾
                }
                f.createNewFile();
                
            	copyFile2(bitmap, new FileOutputStream(f));
            }
            else{
            	
            	f = new File(Environment.getExternalStorageDirectory() + "/"
                        + context.getPackageName() + "/" + name);
                File dirs = new File(f.getParent());
                if (!dirs.exists()) {
                    dirs.mkdirs();  //創建資料夾
                }
                f.createNewFile();
                
            	FileOutputStream fos = new FileOutputStream(f);
            	copyFile(is, fos);
            	//fos.write(buffer);
            }
        	/*
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                    + ".jpg");
            File dirs = new File(f.getParent());
            if (!dirs.exists()) {
                dirs.mkdirs();  //創建資料夾
            }
            f.createNewFile();
            InputStream inputstream = client.getInputStream();
            copyFile(inputstream, new FileOutputStream(f));*/
            /*
            InputStream inputstream2 = client.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputstream2);
            BufferedReader br = new BufferedReader(isr);
            dataName = br.readLine();*/
            client.close();
            serverSocket.close();
        	
            return name;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private void copyFile2(Bitmap bitmap, FileOutputStream fos) {
    	
    	try {
    		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}

	private void copyFile(DataInputStream is, FileOutputStream fos) throws IOException {
    	byte buf[] = new byte[1024];
		int length;
		
		try{
			while ((length = is.read(buf)) != -1) {
                fos.write(buf, 0, length);
            }
			fos.flush();
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
    
	@Override  
    protected void onProgressUpdate(Integer... progresses) {
		statusText.setText("資料接收中，進度：" + progresses[0] + "%！檔案大小 "+df.format((float)(progresses[1]/(float)1048576))+" MB");
    }
	
    protected void onPostExecute(String result) {
        //Toast.makeText(context, "接收完成！"+result, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "接收完成！\nresult:"+result, Toast.LENGTH_SHORT).show();
        new FileServerAsyncTask(context, statusText).execute(PORT+"");
    }
}