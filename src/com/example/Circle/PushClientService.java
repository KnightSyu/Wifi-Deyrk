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

public class PushClientService extends IntentService {

	private static final String TAG = "PushClientService";
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "SEND_FILE";
    public static final String ACTION_RECEIVE_FILE = "REVEICE_FILE";
    
    public static final String EXTRAS_TITLE = "Title";
    public static final String EXTRAS_CONTEXT = "Context";
    public static final String EXTRAS_KIND = "Kind";
    public static final String EXTRAS_IMAGE_SIZE = "Img_Size";
    public static final String EXTRAS_IMAGE_BYTEARRAY = "Img_ByteArray";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public int count=0;
    private int requestCode =0;
    
    Handler mHandler;
    private Socket socket;
    Context context;
    
	public PushClientService() {
		super("PushClientService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		context = getApplicationContext();
		if (intent.getAction().equals(ACTION_SEND_FILE)){
			try {
				String title = intent.getExtras().getString(EXTRAS_TITLE);
	        	String context = intent.getExtras().getString(EXTRAS_CONTEXT);
	        	String kind = intent.getExtras().getString(EXTRAS_KIND);
	        	int size = intent.getExtras().getInt(EXTRAS_IMAGE_BYTEARRAY);
	        	byte[] byteArray = intent.getExtras().getByteArray(EXTRAS_IMAGE_SIZE);
	        	
				String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
				int port = 8900;
	        	socket = new Socket();
	        	socket.bind(null);
	        	socket.connect(new InetSocketAddress(host, port),SOCKET_TIMEOUT);
	        	
	        	DataOutputStream os = new DataOutputStream(socket.getOutputStream());
	        	/*
	        	ContentResolver cr = context.getContentResolver();
	        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(fileUri)));
        		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);*/
        		
        		os.writeUTF(title);
        		os.writeUTF(context);
        		os.writeUTF(kind);
        		os.writeInt(size);
	            os.write(byteArray);
	            os.flush();
	        	
	        	Bundle bundle = intent.getExtras();
	            if (bundle != null) {
	                Messenger messenger = (Messenger) bundle.get("messenger");
	                Message msg = Message.obtain();
	                Bundle sendData = new Bundle();
	                sendData.putString("sended", "推播訊息成功！");
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
	}
}
