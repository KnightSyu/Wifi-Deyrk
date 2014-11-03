package com.example.Circle;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

public class FileTransferService extends IntentService {

	private static final String TAG = "FileTransferService";
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "SEND_FILE";
    public static final String ACTION_RECEIVE_FILE = "REVEICE_FILE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public int count=0;
    private int requestCode =0;
    
    Handler mHandler;
    private Socket socket;
    Context context;
    
	public FileTransferService() {
		super("FileTransferService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		context = getApplicationContext();
		if (intent.getAction().equals(ACTION_SEND_FILE)){
			try {
				String fileUri = intent.getExtras().getString(EXTRAS_FILE_PATH);
				String dataName = getRealPathFromURI(Uri.parse(fileUri));
				String[] dataType = dataName.split("[.;\\s]+",2);
				String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
				int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
				int size = 0;
	        	socket = new Socket();
	        	socket.bind(null);
	        	socket.connect(new InetSocketAddress(host, port),SOCKET_TIMEOUT);
	        	
	        	if(dataType[1].equals("jpg")){
	        		ContentResolver cr = context.getContentResolver();  //取得當前應用的 ContentResolver instence
		        	DataOutputStream os = new DataOutputStream(socket.getOutputStream());  //接收水
		        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
		        	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(fileUri)));
	        		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        		byte[] byteArray = stream.toByteArray();
	        		os.writeUTF(dataName);
	        		os.writeInt(stream.size());
		            os.write(byteArray);
		            os.flush();
	        	}
	        	else{
	        		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
	        		File f = new File(Uri.parse(fileUri).getPath());
	        		os.writeUTF(dataName);
	        		os.writeInt((int)f.length());
	        		ContentResolver cr = context.getContentResolver();
	        		InputStream is = cr.openInputStream(Uri.parse(fileUri));
	        		copyFile(is, os);
		            //os.writeBytes(is.toString());
		            os.flush();
	        	}
	        	
	        	Bundle bundle = intent.getExtras();
	            if (bundle != null) {
	                Messenger messenger = (Messenger) bundle.get("messenger");
	                Message msg = Message.obtain();
	                Bundle sendData = new Bundle();
	                sendData.putString("sended", "成功傳送文件！");
	                msg.setData(sendData); //put the data here
	                try {
	                    messenger.send(msg);
	                } catch (Exception e) {
	                }
	            }
	            
	            socket.close();
	            
	        } catch (IOException e) {
	        	
	        }
		}
		
		/*
		count+=1;
		Toast.makeText(getApplicationContext(), "有進入onHandleIntent！"+count+"次",Toast.LENGTH_SHORT).show();
		if (intent.getAction().equals(ACTION_SEND_FILE)){ // 傳送資料
            String uri = intent.getExtras().getString("uri");  
            String host = intent.getExtras().getString("host");  
            int port = intent.getExtras().getInt("port");
            requestCode = intent.getExtras().getInt("requestCode");
            Socket socket = new Socket(); 
            Toast.makeText(getApplicationContext(), "有進入ACTION_SEND_FILE！",  
                    Toast.LENGTH_SHORT).show();    
            try{
            	Toast.makeText(getApplicationContext(), "文件開始傳送！",  
                        Toast.LENGTH_SHORT).show();
            	socket.bind(null);  //使 Socket 與本機端點建立關聯。
                socket.connect(new InetSocketAddress(host, port),  
                        SOCKET_TIMEOUT);  
                ContentResolver cr = getApplicationContext()  
                        .getContentResolver();  //取得當前應用的 ContentResolver instence
                OutputStream os = socket.getOutputStream();  //接收水
                InputStream is = cr.openInputStream(Uri.parse(uri)); //產出水 
                copyFile(is, os);  
            }catch (Exception e){
            	
            	Toast.makeText(getApplicationContext(), "進入catch,原因:"+e.toString(),  
                        Toast.LENGTH_SHORT).show(); 
            } finally{
            	if (socket != null && socket.isConnected()){
            		try{
            			Toast.makeText(getApplicationContext(), "socket.close",  
                                Toast.LENGTH_SHORT).show(); 
            			socket.close();
            		}catch(Exception e){
            			Toast.makeText(getApplicationContext(), "進入printStack",  
                                Toast.LENGTH_SHORT).show(); 
            			e.printStackTrace(); 
            		}
            	}
            	Toast.makeText(getApplicationContext(), "文件傳送完畢！",  
                        Toast.LENGTH_SHORT).show();
            }
		}
		
		if (intent.getAction().equals(ACTION_RECEIVE_FILE)){//接收資料
			try{
				Toast.makeText(getApplicationContext(), "文件開始接收！",  
                        Toast.LENGTH_SHORT).show();
				int port1 = intent.getExtras().getInt("port");  
                ServerSocket serverSocket = new ServerSocket(port1);  
                Socket client = serverSocket.accept();
                
                if(requestCode==20){ //傳送圖片
                final File f = new File(  
                        Environment.getExternalStorageDirectory()  
                                + "/libra/wifishare_"  
                                + System.currentTimeMillis() + ".jpg");
                File dirs = new File(Environment.getExternalStorageDirectory()  
                        + "/libra/"); 
                if (!dirs.exists()) {  
                    dirs.mkdirs();  //創建資料夾
                    
                }  
                f.createNewFile(); 
                InputStream inputstream = client.getInputStream();  
                copyFile(inputstream, new FileOutputStream(  
                        f));
                serverSocket.close();  
                Toast.makeText(getApplicationContext(), "文件接收完成！",  
                        Toast.LENGTH_SHORT).show();
              //打開圖片
                
                Intent viewIntent = new Intent();  
                viewIntent.setAction(android.content.Intent.ACTION_VIEW);  
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                viewIntent.setDataAndType(  
                        Uri.parse("file://" + f.getAbsolutePath()), "image/*");  
                startActivity(viewIntent);  
                
                }else if(requestCode==30){//傳送影片
                
                final File f = new File(  
                        Environment.getExternalStorageDirectory()  
                                + "/libra/wifishare_"  
                                + System.currentTimeMillis() + ".mp4");
                File dirs = new File(Environment.getExternalStorageDirectory()  
                        + "/libra/"); 
                if (!dirs.exists()) {  
                    dirs.mkdirs();  //創建資料夾
                }  
                f.createNewFile(); 
                InputStream inputstream = client.getInputStream();  
                copyFile(inputstream, new FileOutputStream(  
                        f));
                serverSocket.close();  
                Toast.makeText(getApplicationContext(), "文件接收完成！",  
                        Toast.LENGTH_SHORT).show();
              //打開影片
                
                Intent viewIntent = new Intent();  
                viewIntent.setAction(android.content.Intent.ACTION_VIEW);  
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                viewIntent.setDataAndType(  
                        Uri.parse("file://" + f.getAbsolutePath()), "video/*");  
                startActivity(viewIntent);
                
                }else if(requestCode==40){ //傳送文字檔
                	final File f = new File(  
                            Environment.getExternalStorageDirectory()  
                                    + "/libra/wifishare_"  
                                    + System.currentTimeMillis() + ".txt");
                	File dirs = new File(Environment.getExternalStorageDirectory()  
                            + "/libra/"); 
                    if (!dirs.exists()) {  
                        dirs.mkdirs();  //創建資料夾
                    }  
                    f.createNewFile(); 
                    InputStream inputstream = client.getInputStream();  
                    copyFile(inputstream, new FileOutputStream(  
                            f));
                    serverSocket.close();  
                    Toast.makeText(getApplicationContext(), "文件接收完成！",  
                            Toast.LENGTH_SHORT).show();
                  //打開文字檔
                    
                    Intent viewIntent = new Intent();  
                    viewIntent.setAction(android.content.Intent.ACTION_VIEW);  
                    viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                    viewIntent.setDataAndType(  
                            Uri.parse("file://" + f.getAbsolutePath()), "txt/*");  
                    startActivity(viewIntent);  
                }
                
			}catch(Exception e){
				
			}
			
		}*/
		
		

	}
	/*
	public static boolean copyFile(InputStream inputStream, OutputStream out){
		byte buf[] = new byte[1024]; 
		int length ;
		
		try{
			while ((length = inputStream.read(buf)) != -1) {  
                out.write(buf, 0, length);  
            }  
			 	out.close();  
	            inputStream.close(); 
		}catch(Exception e){
	           
	           return false;  
			
		}
		
			return true;
	}*/
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	private void copyFile(InputStream is, DataOutputStream dos) throws IOException {
    	byte buf[] = new byte[1024];
		int length;
		
		try{
			while ((length = is.read(buf)) != -1) {
                dos.write(buf, 0, length);
            }
			dos.close();
			is.close();
		}catch(Exception e){
			
		}
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

}
