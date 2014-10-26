package com.example.adhoctry;

import java.net.InetAddress;
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
import android.content.Intent;
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

public class FilesMain extends ListFragment implements PeerListListener,ConnectionInfoListener{
	
	View rootView;
	
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WiFiDirectBroadcastReceiver mReceiver;
	private int avaliablePeersNumber =0;  //用來存放collection.size()所蒐集到的可用peers
	private int deviceNumber;
	private WifiP2pInfo connectedInfo;
	private WifiP2pDevice devices_info;
	private int connectionCount = 0;
	private boolean isConnected = false;
	private int PORT =8898;
	private boolean btn_send = false;
	IntentFilter mIntentFilter;
	List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	TextView device_name;
	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //定義action(用來傾聽動作)
        
        mManager = (WifiP2pManager) this.getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this.getActivity(), this.getActivity().getMainLooper(), null);
        
        //初始化WiFiDirect
        
        discoverpeers();
        
        setAdapter(rootView);
        //更新畫面的ListView
        
        return rootView;
    }

	private void discoverpeers() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            	
            }

            @Override
            public void onFailure(int reasonCode) {
            	Toast.makeText(rootView.getContext(),"onFailure reasonCode: "+reasonCode,Toast.LENGTH_SHORT).show();
            }
        });
	}
    
    private void setAdapter(View rootView) {
    	
    	
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
                    	connectpeers((Integer) arg0.getTag());
                    	
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
        mReceiver = new WiFiDirectBroadcastReceiver(mChannel, mManager, this);
        this.getActivity().registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
	public void onPause() {
        this.getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }
    
    //當附近的點有變動時所跑的函式
    @Override
    public void onPeersAvailable (WifiP2pDeviceList peers){
    	Toast.makeText(rootView.getContext(), "成功搜尋到使用者",Toast.LENGTH_SHORT).show();
    	this.peers.clear();
    	//用來收集所發現的peers數量,並放入num_avaliablepeers
    	Collection<WifiP2pDevice> collection = peers.getDeviceList(); 
    	avaliablePeersNumber =collection.size();
    	
    	this.peers.addAll(peers.getDeviceList());
    	Toast.makeText(this.getActivity().getApplicationContext(),"共有: "+avaliablePeersNumber+"個使用者",Toast.LENGTH_SHORT).show();
    	setAdapter(rootView);
    }
    
	public void connectpeers(int d_number) {
		
		final WifiP2pDevice device =  (WifiP2pDevice) peers.get(d_number);
		devices_info = device;
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		config.groupOwnerIntent = 0;  //確認被連的一定是GO(Group Owner)
		mManager.connect(mChannel, config, new ActionListener(){

			@Override
			public void onFailure(int reason) {
				String reasonString = null;
				switch(reason){
		    	case 0:
		    		reasonString = "網路錯誤";
		    		break;
		    	case 1:
		    		reasonString = "你的設備沒有支援WifiP2p功能";
		    		break;
		    	case 2:
		    		reasonString = "Framework is busy";
		    		break;
		    	default:
		    		reasonString ="未知的錯誤!";
		    		break;
		    	}
				Toast.makeText(getActivity(), "連接失敗,失敗原因:"+reasonString, Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onSuccess() {
				
				
			}
			
		});
		
		
	}
	
	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		
		isConnected = true;
		connectedInfo = info;
		String connectedInfos = connectedInfo.groupOwnerAddress.getHostAddress();
		Toast.makeText(getActivity(), "info:"+connectedInfo, Toast.LENGTH_SHORT).show();
		
		Toast.makeText(getActivity(), "hostAddress:"+connectedInfos, Toast.LENGTH_SHORT).show();
		
		Toast.makeText(getActivity(), "OnConnectionAvaliable-running",Toast.LENGTH_LONG).show();

		

		//要做的事...要如何從被連接的狀態抓到device的資訊
		//Toast.makeText(getActivity(), "組長:"+info.isGroupOwner, Toast.LENGTH_SHORT).show();
		Bundle args = new Bundle();
    	
    	Map<String,Object> item = new HashMap<String,Object>();
        item = data.get(deviceNumber);
        
        
        boolean isOwnerInfo = info.isGroupOwner;
        boolean isFormedInfo = info.groupFormed;
        args.putString("deviceName", (String) item.get("deviceName"));
        args.putString("connectedInfo",connectedInfos);  //抓connectionInfo到另一個fragment
        args.putBoolean("isOwnerInfo",isOwnerInfo);  //抓detailInfo到另一個fragment
        args.putBoolean("isFormedInfo",isFormedInfo);  //抓detailInfo到另一個fragment
        Fragment fragment = new FilesSelect();
        fragment.setArguments(args);
		FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_files, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
		
		
		
		
			  
		
		
	}


}