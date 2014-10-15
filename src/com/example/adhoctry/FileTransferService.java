package com.example.adhoctry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

public class FileTransferService extends IntentService {

	private static final String TAG = "FileTransferService";  
    private static final int SOCKET_TIMEOUT = 5000;  
    public static final String ACTION_SEND_FILE = "SEND_FILE";
    public static final String ACTION_RECEIVE_FILE = "REVEICE_FILE";
    public int count=0;
    private int requestCode =0;
    
    Handler mHandler;
    
	public FileTransferService() {
		super("FileTransferService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
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
			
		}
		
		

	}
	
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
	}

}
