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
    
    public static final String EXTRAS_TITLE = "Title";
    public static final String EXTRAS_CONTEXT = "Context";
    public static final String EXTRAS_KIND = "Kind";
    public static final String EXTRAS_IMAGE_SIZE = "Img_Size";
    public static final String EXTRAS_IMAGE_BYTEARRAY = "Img_ByteArray";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_PORT = "go_port";
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
		try {
			String title = intent.getExtras().getString(EXTRAS_TITLE);
        	String context = intent.getExtras().getString(EXTRAS_CONTEXT);
        	String kind = intent.getExtras().getString(EXTRAS_KIND);
        	int size = intent.getExtras().getInt(EXTRAS_IMAGE_SIZE);
        	byte[] byteArray = intent.getExtras().getByteArray(EXTRAS_IMAGE_BYTEARRAY);
        	
			String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
			int port = intent.getExtras().getInt(EXTRAS_PORT);
        	socket = new Socket();
        	socket.bind(null);
        	socket.connect(new InetSocketAddress(host, port),SOCKET_TIMEOUT);
        	
        	DataOutputStream os = new DataOutputStream(socket.getOutputStream());
    		
    		os.writeUTF(title);
    		os.writeUTF(context);
    		os.writeUTF(kind);
    		os.writeInt(size);
            os.write(byteArray);
            os.flush();
            os.close();
            
            
            socket.close();
            
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Messenger messenger = (Messenger) bundle.get("messenger");
                Message msg = Message.obtain();
                Bundle sendData = new Bundle();
                sendData.putString("sended", "成功推播訊息！");
                msg.setData(sendData); //put the data here
                try {
                    messenger.send(msg);
                } catch (Exception e) {
                }
            }
            
        } catch (IOException e) {
        	
        }
	}
}
