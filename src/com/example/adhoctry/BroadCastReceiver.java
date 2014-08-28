package com.example.adhoctry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class BroadCastReceiver extends BroadcastReceiver {
	
	private WifiP2pManager manager;
	private Channel channel;
	private ActivityPush activity;
	PeerListListener myPeerListListener;
	
	
	//建構子
	public BroadCastReceiver(WifiP2pManager mManager, Channel mChannel,
			ActivityFiles activityFiles) {
		super();
		
		this.manager = manager;
		this.channel = channel;
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {  
            // 確認是否wifi direct 有成功enable,並且通知activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
				//wifi direct 已開啟
				
			}else{
				
			}
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {  
            // 呼叫 requestPeers() 去取得附近所找到的peer
        	if (manager != null) {  
                //取得以偵測到的peer設備清單
				manager.requestPeers(channel, myPeerListListener);  
            } 
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {  
            // peer連接發生變化時 
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {  
            // peer設備訊息變化時(ex:更改手機名稱etc...)
        }  

	}

}
