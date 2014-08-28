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
	
	
	//�غc�l
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
            // �T�{�O�_wifi direct �����\enable,�åB�q��activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
				//wifi direct �w�}��
				
			}else{
				
			}
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {  
            // �I�s requestPeers() �h���o����ҧ�쪺peer
        	if (manager != null) {  
                //���o�H�����쪺peer�]�ƲM��
				manager.requestPeers(channel, myPeerListListener);  
            } 
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {  
            // peer�s���o���ܤƮ� 
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {  
            // peer�]�ưT���ܤƮ�(ex:������W��etc...)
        }  

	}

}
