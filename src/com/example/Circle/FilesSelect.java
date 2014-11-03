package com.example.Circle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Circle.R;

public class FilesSelect extends Fragment {
	
	View rootView;
	TextView connected_device;
	Button btnFilePhoto;
	Button btnFileVideo;
	Button btnFileOther;
	Button btnDisConnect;
	String connectedInfo;
	TextView progress;
	boolean isOwnerInfo;
	boolean isFormedInfo;
	boolean isClickSend = false;
	private int PORT1 =8898;
	private int PORT2 =8899;
	private int count=0;
	private FileServerAsyncTask fsat;
	private ServerSocket serverSocket = null;
    private Socket client = null;
    
	public FilesSelect(){
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_files_select, container, false);
    	connected_device = (TextView)rootView.findViewById(R.id.connected_device);
    	
    	Bundle bundle = this.getArguments();
    	String device_info = bundle.getString("deviceName");
		isOwnerInfo = MainDeyrk.isOwnerInfo;
		isFormedInfo = MainDeyrk.isFormedInfo;
		
		if(isFormedInfo && isOwnerInfo){
		//	clientIP = device.deviceAddress;
			connectedInfo = "192.168.1.108";
			Toast.makeText(this.getActivity(), "我是組長，我目標傳給IP "+connectedInfo, Toast.LENGTH_LONG).show();
		}else if(isFormedInfo){
			connectedInfo = MainDeyrk.IP_SERVER;
			Toast.makeText(this.getActivity(), "我不是組長，我目標傳給IP "+connectedInfo, Toast.LENGTH_LONG).show();
		}
    	
    	initialViews();
    	onClickListeners();
    	connected_device.setText(device_info);
    	
    	if(isOwnerInfo && isFormedInfo){
    		fsat = new FileServerAsyncTask(this.getActivity(),progress);
    		fsat.execute(PORT1+"");
    	}
    	else if(isFormedInfo){
    		fsat = new FileServerAsyncTask(this.getActivity(),progress);
    		fsat.execute(PORT2+"");
    	}
    	
    	//transferUpdate();
    	
    	//Toast.makeText(getActivity(), "OnCreateView-running",Toast.LENGTH_LONG).show();
    	/*
    	final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
             public void run() {
            	 if(isFormedInfo){
            		 Toast.makeText(getActivity(), "handler run()",Toast.LENGTH_SHORT).show();
            		 handler.postDelayed(this,5000);
            	 }
            	 else{
            		 MainDeyrk.cancelConnect = true;
            		 Fragment fragment = MainDeyrk.FM;
            		 FragmentTransaction trans = getFragmentManager().beginTransaction();
          	         trans.replace(R.id.root_files, fragment);
          	         trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
          	         trans.commit();
            	 }
             } 
        }, 5000);*/
    	
    	return rootView;
    }

	private void onClickListeners() {
		btnFilePhoto.setOnClickListener(photo);
		btnFileVideo.setOnClickListener(video);
		btnFileOther.setOnClickListener(other);
		btnDisConnect.setOnClickListener(disconnect);
	}

	private void initialViews() {
		btnFilePhoto = (Button)rootView.findViewById(R.id.photo);
    	btnFileVideo = (Button)rootView.findViewById(R.id.video);
    	btnFileOther = (Button)rootView.findViewById(R.id.other);
    	btnDisConnect = (Button)rootView.findViewById(R.id.disconnect);
    	progress = (TextView)rootView.findViewById(R.id.Progress);
	}
	
	public OnClickListener photo = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			isClickSend = true;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("image/*");  
            startActivityForResult(intent, 20); 
            
		}
		
	};
	
	public OnClickListener video = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("video/*");  
            startActivityForResult(intent, 30); 
			
		}
		
	};
	
	public OnClickListener other = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("*/*");  
            startActivityForResult(intent, 40); 
            
		}
		
	};
	
	public OnClickListener disconnect = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			//transferUpdate();
			MainDeyrk.cancelConnectNow();
			changeToFilesMain();
			/*
			try{
				MainDeyrk.FS.changeToFilesMain();
			}catch(Exception e){
			}*/
		}
		
	};
	
	public void changeToFilesMain(){
		Fragment fragment = MainDeyrk.FM;
		FragmentTransaction trans = getFragmentManager().beginTransaction();
	    trans.replace(R.id.root_files, fragment);
	    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	    trans.commit();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(data!=null){
        	if (requestCode == 20) {  
            	//Context ctx_send = (Context) this.getActivity();
        		/*Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
                Uri uri = data.getData();  
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
                serviceIntent.putExtra("uri", uri.toString());  
                serviceIntent.putExtra("host",connectedInfo);  
                serviceIntent.putExtra("port", PORT);
                serviceIntent.putExtra("requestCode", requestCode);
                Toast.makeText(this.getActivity(),"uri:"+uri.toString()+"/host:"+connectedInfo+"/port:"+PORT,Toast.LENGTH_LONG).show();
                getActivity().startService(serviceIntent);*/
        		Uri uri = data.getData();
        		/*
        		if(isOwnerInfo && isFormedInfo){
        			new FileClientAsyncTask(this.getActivity(),(TextView)rootView.findViewById(R.id.Progress)).execute(uri.toString(),connectedInfo,PORT2+"");
            	}
            	else{
            		new FileClientAsyncTask(this.getActivity(),(TextView)rootView.findViewById(R.id.Progress)).execute(uri.toString(),connectedInfo,PORT1+"");
            	}
        		*/
        		Handler handler = new Handler() {
        		    @Override
        		    public void handleMessage(Message msg) {
        		    	Bundle reply = msg.getData();
        		    	Toast.makeText(rootView.getContext(),reply.getString("sended"),Toast.LENGTH_SHORT).show();
        		    }
        		};
        		Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, connectedInfo);
                serviceIntent.putExtra("messenger", new Messenger(handler));
        		if(isOwnerInfo && isFormedInfo){
        			serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT2);
        		}
        		else if(isFormedInfo){
                    serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT1);
        		}
        		getActivity().startService(serviceIntent);
            }else if(requestCode== 30){
            	/*Context ctx_send = (Context) this.getActivity();
        		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
                Uri uri = data.getData();  
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
                serviceIntent.putExtra("uri", uri.toString());  
                serviceIntent.putExtra("host",connectedInfo);  
                serviceIntent.putExtra("port", PORT);
                serviceIntent.putExtra("requestCode", requestCode);
                //d_name2.setText("uri:"+uri.toString()+"/host:"+connectedInfo.groupOwnerAddress.getHostAddress()+"/port:"+PORT);
                ctx_send.startService(serviceIntent);*/
            	Uri uri = data.getData();
            	Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, connectedInfo);
        		if(isOwnerInfo && isFormedInfo){
        			serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT2);
        		}
        		else if(isFormedInfo){
                    serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT1);
        		}
        		getActivity().startService(serviceIntent);
            }else if(requestCode== 40){
            	/*
            	Context ctx_send = (Context) this.getActivity();
        		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
                */
                Uri uri = data.getData();  
                
                /*serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
                serviceIntent.putExtra("uri", uri.toString());  
                serviceIntent.putExtra("host",connectedInfo);  
                serviceIntent.putExtra("port", PORT2);
                serviceIntent.putExtra("requestCode", requestCode);
                //d_name2.setText("uri:"+uri.toString()+"/host:"+connectedInfo.groupOwnerAddress.getHostAddress()+"/port:"+PORT);
                ctx_send.startService(serviceIntent);
                */
                Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, connectedInfo);
        		if(isOwnerInfo && isFormedInfo){
        			serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT2);
        		}
        		else if(isFormedInfo){
                    serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, PORT1);
        		}
        		getActivity().startService(serviceIntent);
            }
        }
    }
	
}
