package com.example.Circle;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.Circle.R;
import com.example.Circle.CollectionMain.ListCursorAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass.Device;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
	
	private static WifiP2pManager mManager = MainDeyrk.mManager;
	private static Channel mChannel = MainDeyrk.mChannel;
	private int avaliablePeersNumber =0;  //用來存放collection.size()所蒐集到的可用peers
	private WifiP2pInfo connectedInfo;
	private WifiP2pDevice devices_info;
	private int connectionCount = 0;
	private boolean isConnected = false;
	private boolean cancelConnect = false;
	private int PORT =8898;
	private boolean btn_send = false;
	TextView device_name;
	WifiP2pDevice device;
	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	int deviceNumber;
	
	public FilesMain(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		cancelConnect = bundle.getBoolean("cancelConnect");
    	
        rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        
        //setAdapter();
        //更新畫面的ListView
        
		/*
        Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	         public void run() {
	        	 connectToPusher();
	         }
	    }, 5000);*/
        
        return rootView;
    }
    
    public void setAdapter() {
    	
    	this.data = MainDeyrk.data;
    	this.peers = MainDeyrk.peers;
    	this.deviceNumber = MainDeyrk.deviceNumber;
    	
    	data.clear();
    	
		//Toast.makeText(this.getActivity().getApplicationContext(),"共有: "+avaliablePeersNumber+"個使用者",Toast.LENGTH_SHORT).show();
    	
    	//存陣列內容(連線的裝置名稱)
    	for(int i=0; i<peers.size(); i++)
    	{
    		Map<String,Object> item = new HashMap<String,Object>();
    		WifiP2pDevice device = peers.get(i);
    		item.put("deviceName",device.deviceName);
    		item.put("devicePosition",i);
        	data.add(item);
    	}
    	
    	SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),data,R.layout.listview_files,new String[]{"deviceName"},new int[]{R.id.device})
    	{
    		
    		@Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                deviceNumber = position;  //收集device的編號

                Button b=(Button)v.findViewById(R.id.connect);
                device_name = (TextView)v.findViewById(R.id.device);
                Map<String,Object> item = new HashMap<String,Object>();
                item = data.get(position);
                //b.setTag(item.get("deviceName"));
                b.setTag(item.get("devicePosition"));
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        //Toast.makeText(arg0.getContext().getApplicationContext(),""+arg0.getTag(),Toast.LENGTH_SHORT).show();
                    	MainDeyrk.connectpeers((Integer) arg0.getTag());
                    	
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
	
	public void changeToSelect(){
		
		
		
		Bundle args = new Bundle();
    	
    	Map<String,Object> item = new HashMap<String,Object>();
        item = data.get(deviceNumber);
        
        args.putString("deviceName", (String) item.get("deviceName"));
        
        Fragment fragment = MainDeyrk.FS;
        fragment.setArguments(args);
		FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_files, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
	}
	
}