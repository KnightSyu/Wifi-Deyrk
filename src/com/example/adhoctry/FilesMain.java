package com.example.adhoctry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.adhoctry.CollectionMain.ListCursorAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FilesMain extends ListFragment{
	
	View rootView;
	String[] device_s = {"SyuWei 的 iphone", "Lu's GSmart", "Dream's Z1", "虹音 的 小米3"};
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	
	IntentFilter mIntentFilter;
	
	WifiP2pDeviceList peers;
	
	TextView device_name;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        
        mManager = (WifiP2pManager) this.getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this.getActivity(), this.getActivity().getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this.getActivity());
        //初始化WiFiDirect
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //定義action(用來傾聽動作)
        
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                
            }

            @Override
            public void onFailure(int reasonCode) {
                
            }
        });
        
        setAdapter(rootView);
        //更新畫面的ListView
        
        return rootView;
    }
    
    private void setAdapter(View rootView) {
    	
    	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
    	
    	//存陣列內容(連線的裝置名稱)
    	for(int i=0; i<device_s.length; i++)
    	{
    		Map<String,Object> item = new HashMap<String,Object>();
    		item.put("device",device_s[i]);
        	data.add(item);
    	}
    	
    	SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),data,R.layout.listview_files,new String[]{"device"},new int[]{R.id.device})
    	{
    		TextView device_name;
    		@Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                Button b=(Button)v.findViewById(R.id.connect);
                device_name = (TextView)v.findViewById(R.id.device);
                b.setTag(device_name.getText().toString());
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Toast.makeText(arg0.getContext().getApplicationContext(),""+arg0.getTag(),Toast.LENGTH_SHORT).show();
                        /*
                        WifiP2pDevice device = null;
                    	WifiP2pConfig config = new WifiP2pConfig();
                    	config.deviceAddress = device.deviceAddress;
                    	mManager.connect(mChannel, config, new ActionListener() {
                    	
                    	    @Override
                    	    public void onSuccess() {
                    	        //success logic
                    	    }
                    	    
                    	    @Override
                    	    public void onFailure(int reason) {
                    	        //failure logic
                    	    }
                    	});
                    	*/
                    }
                });
                return v;
            }
    	};
        //設定接口
    	
        setListAdapter(adapter);
        //執行接口
	}
    
    //當ListView有被點擊時運行的函式
  	public void onListItemClick(ListView l, View v, int position, long id) {
  	    super.onListItemClick(l, v, position, id);
  	    LinearLayout show_device_info = (LinearLayout)v.findViewById(R.id.show_device_info);
  	    show_device_info.setVisibility(View.VISIBLE);
  	}
    
    /* register the broadcast receiver with the intent values to be matched */
    @Override
	public void onResume() {
        super.onResume();
        this.getActivity().registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
	public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(mReceiver);
    }
    
    //當附近的點有變動時所跑的函式
    public void onPeersAvailable (WifiP2pDeviceList peers){
    	this.peers = peers;
    }
}