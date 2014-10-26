package com.example.Circle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.os.Environment;
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
	boolean isOwnerInfo;
	boolean isFormedInfo;
	boolean isClickSend = false;
	private int PORT =8898;
	private int count=0;
	private FileServerAsyncTask fsat;
	
	public FilesSelect() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_files_select, container, false);
    	connected_device = (TextView)rootView.findViewById(R.id.connected_device);
    	
    	Bundle bundle = this.getArguments();
    	String device_info = bundle.getString("deviceName");
    	connectedInfo = bundle.getString("connectedInfo");
    	isOwnerInfo = bundle.getBoolean("isOwnerInfo");
    	isFormedInfo = bundle.getBoolean("isFormedInfo");
    	
    	initialViews();
    	onClickListeners();
    	connected_device.setText(device_info);
    	
    	fsat = new FileServerAsyncTask(this.getActivity());
		fsat.execute(PORT+"");
    	
    	//transferUpdate();
    	
    	//Toast.makeText(getActivity(), "OnCreateView-running",Toast.LENGTH_LONG).show();
    	
    	return rootView;
    }

	private void transferUpdate() {  //接收端這邊沒反應
		if(isOwnerInfo && isFormedInfo){
			Context ctx_receive = (Context) getActivity();
    		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
	        serviceIntent.setAction(FileTransferService.ACTION_RECEIVE_FILE);  
	        serviceIntent.putExtra("port", PORT);
	        ctx_receive.startService(serviceIntent);
	        Toast.makeText(getActivity(), "等待接收中...",Toast.LENGTH_LONG).show();
		}else if(isFormedInfo && isClickSend){
		/*	Context ctx_send = (Context) this.getActivity();
    		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
	        serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
	        serviceIntent.putExtra("port", PORT);
	        ctx_send.startService(serviceIntent);
	        Toast.makeText(getActivity(), "傳送接收中...",Toast.LENGTH_LONG).show();*/
		}else{
			Toast.makeText(getActivity(), "無法進入isOwnerInfo是["+isOwnerInfo+"],isFormedInfo是["+isFormedInfo+"]",Toast.LENGTH_LONG).show();
		}
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
            intent.setType("files/*");  
            startActivityForResult(intent, 40); 
			
		}
		
	};
	
	public OnClickListener disconnect = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			 transferUpdate();
		}
		
	};
	
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
        		if (fsat != null && (fsat.getStatus() == FileServerAsyncTask.Status.RUNNING)) {
        			Toast.makeText(this.getActivity(),"fsat.cancel(true);",Toast.LENGTH_SHORT).show();
        			fsat.cancel(true);
    			}
        		Uri uri = data.getData();
                new FileClientAsyncTask(this.getActivity(),(TextView)rootView.findViewById(R.id.Progress)).execute(uri.toString(),connectedInfo,PORT+"");
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
                new FileClientAsyncTask(this.getActivity(),(TextView)rootView.findViewById(R.id.Progress)).execute(uri.toString(),connectedInfo,PORT+"");
            }else if(requestCode== 40){
            	Context ctx_send = (Context) this.getActivity();
        		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
                Uri uri = data.getData();  
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
                serviceIntent.putExtra("uri", uri.toString());  
                serviceIntent.putExtra("host",connectedInfo);  
                serviceIntent.putExtra("port", PORT);
                serviceIntent.putExtra("requestCode", requestCode);
                //d_name2.setText("uri:"+uri.toString()+"/host:"+connectedInfo.groupOwnerAddress.getHostAddress()+"/port:"+PORT);
                ctx_send.startService(serviceIntent);
            }
        }
    }
	
}
