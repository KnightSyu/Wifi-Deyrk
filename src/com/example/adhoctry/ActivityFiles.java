package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivityFiles extends Fragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    TextView dummyTextView;
    WifiP2pManager mManager;
    Channel mChannel;
    BroadCastReceiver mReceiver;
    IntentFilter mIntentFilter;
    WifiP2pDevice device ; 
    



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);
        
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	
    	//Intent過濾器
    	mIntentFilter = new IntentFilter();  
    	  
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);  
      
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);  
      
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);  
      
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);  
    	
    	mManager = (WifiP2pManager) this.getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
    	
    	mChannel = mManager.initialize(getActivity().getApplicationContext(),getActivity().getMainLooper(), null);
    				//this --> getActivity().getApplcationContext(),getMainLooper --> getActivity().getMainLooper()
    	mReceiver = new BroadCastReceiver(mManager, mChannel, this);
    	
        
    	// discoverPeers監聽器
    	mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener(){

    		@Override
    		public void onFailure(int arg0) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onSuccess() {
    			// TODO Auto-generated method stub
    			
    		}
        	
        });
    	
        WifiP2pConfig config = new WifiP2pConfig(); 
       /* config.deviceAddress = device.deviceAddress;
    	mManager.connect(mChannel, config, new  ActionListener() {  
    		  
    	    @Override  
    	    public void onSuccess() {  
    	        //成功連線  
    	    }  
    	  
    	    @Override  
    	    public void onFailure(int reason) {  
    	          
    	    }  
    	});
    	
    	*/
        
    	return rootView;
    }


    
    public void onResume() {  
        super.onResume();  
        this.getActivity().registerReceiver(mReceiver, mIntentFilter);  
    }
    public void onPause() {  
        super.onPause();  
        this.getActivity().unregisterReceiver(mReceiver);  
    }
    
	
    


}
