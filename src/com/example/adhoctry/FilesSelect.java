package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FilesSelect extends Fragment {
	
	View rootView;
	TextView connected_device;
	Button btnFilePhoto;
	Button btnFileVideo;
	Button btnFileOther;
	Button btnDisConnect;
	String connectedInfo;
	String isOwnerInfo;
	String isFormedInfo;
	boolean isReadyReceive = false;
	private int PORT =8898;
	private int count=0;	
	
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
    	isOwnerInfo = bundle.getString("isOwnerInfo");
    	isFormedInfo = bundle.getString("isFormedInfo");
    	
    	initialViews();
    	onClickListeners();
    	connected_device.setText(device_info);
    	
    	transferUpdate();
    	
    	return rootView;
    }

	private void transferUpdate() {
		if(isOwnerInfo=="true" && isOwnerInfo=="true"){
			Context ctx_receive = (Context) FilesSelect.this.getActivity();
    		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
	        serviceIntent.setAction(FileTransferService.ACTION_RECEIVE_FILE);  
	        serviceIntent.putExtra("port", PORT);
	        ctx_receive.startService(serviceIntent); 
		}else if(isOwnerInfo=="true" && count>=1){
			Context ctx_send = (Context) this.getActivity();
    		Intent serviceIntent = new Intent(this.getActivity(),FileTransferService.class);
	        serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);  
	        serviceIntent.putExtra("port", PORT);
	        ctx_send.startService(serviceIntent);  
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
			count +=1;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("image/*");  
            startActivityForResult(intent, 20); 
			
		}
		
	};
	
	public OnClickListener video = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			count +=1;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("video/*");  
            startActivityForResult(intent, 30); 
			
		}
		
	};
	
	public OnClickListener other = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			count +=1;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.setType("files/*");  
            startActivityForResult(intent, 40); 
			
		}
		
	};
	
	public OnClickListener disconnect = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			 isReadyReceive = true;
			 transferUpdate();
			
		}
		
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == 20) {  
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
        }else if(requestCode== 30){
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
