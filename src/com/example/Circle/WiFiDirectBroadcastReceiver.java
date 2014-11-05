package com.example.Circle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private MainDeyrk mActivity;

    public WiFiDirectBroadcastReceiver(Channel channel,WifiP2pManager manager,
    		MainDeyrk mActivity) {
        super();
        this.mChannel = channel;
        this.mManager = manager;
        this.mActivity =  mActivity;
        //Toast.makeText(this.mActivity.getApplicationContext(), "WiFiDirectBroadcastReceiver建構子",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        	int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct is enabled
            } else {
                // Wi-Fi Direct is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        	
        	// request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, (PeerListListener) mActivity);
                //Toast.makeText(this.mActivity.getActivity(), "WIFI_P2P_PEERS_CHANGED_ACTION",Toast.LENGTH_SHORT).show();
            }else{
            	// mManager is null
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        	if(mManager != null){
        		NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
        		
        		//假如連接成功,則呼叫ConnectionInfoListener()方法
        		mActivity.isConnected = networkInfo.isConnected();
    			if(networkInfo.isConnected()){
    				mManager.requestConnectionInfo(mChannel, (ConnectionInfoListener) mActivity);
    			}
    			else{
    				Toast.makeText(this.mActivity, "目前沒有連線",Toast.LENGTH_SHORT).show();
    				mManager.requestPeers(mChannel, (PeerListListener) mActivity);
    			}
        	}else{
        		Toast.makeText(this.mActivity, "manager==null",Toast.LENGTH_SHORT).show();
        	}
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}