package com.example.adhoctry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FilesMain extends Fragment implements OnClickListener,  
PeerListListener, OnItemClickListener, ConnectionInfoListener {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
   private WifiP2pDeviceList peers = new WifiP2pDeviceList(); 
   private WifiP2pManager mManager;
   private Channel mChannel;
   private final IntentFilter intentFilter = new IntentFilter();  
   private BroadCastReceiver mReceiver;
   private Button search_peers;
   //private WifiP2pDevice device;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getActivity(), getActivity().getMainLooper(), null);
        
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);  

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);  

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);  

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
        search_peers = (Button)rootView.findViewById(R.id.wifidirect_search_peer);
        search_peers.setOnClickListener(search_peer);
        
       // WifiP2pConfig config = new WifiP2pConfig();  
        //config.deviceAddress = device.deviceAddress;  
        //config.wps.setup = WpsInfo.PBC; 
        
        return rootView;
    }
    
    public OnClickListener search_peer = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			mManager.discoverPeers(mChannel,new WifiP2pManager.ActionListener(){

				@Override
				public void onFailure(int arg0) {
					// TODO Auto-generated method stub
					onPeersAvailable(peers);
					
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "附近有點", Toast.LENGTH_SHORT).show();
					onPeersAvailable(peers);
					
				}
				
			});
			
		}
    	
    };

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		Collection<WifiP2pDevice> collection = peers.getDeviceList();
		if (collection == null || collection.size() == 0){
			Toast.makeText(getActivity(), "附近沒有點,或請到系統設定開啟direct", Toast.LENGTH_SHORT).show();
		}else{
			//更新顯示peers
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	public void onResume(){
		super.onResume();
		mReceiver = new BroadCastReceiver(mManager,  
                mChannel, this);  
        getActivity().registerReceiver(mReceiver, intentFilter);  
	}
    
    public void onPause() {  
        getActivity().unregisterReceiver(mReceiver);  
        super.onPause();  
    } 

}
