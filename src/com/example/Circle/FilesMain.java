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
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
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
	private int avaliablePeersNumber =0;  //�ΨӦs��collection.size()�һ`���쪺�i��peers
	private int deviceNumber;
	private WifiP2pInfo connectedInfo;
	private WifiP2pDevice devices_info;
	private int connectionCount = 0;
	private boolean isConnected = false;
	private boolean isConnecting = false;
	private int PORT =8898;
	private boolean btn_send = false;
	IntentFilter mIntentFilter;
	List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	TextView device_name;
	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	WifiP2pDevice device;
	public static String IP_SERVER;
	public static String localIP;
	public static String clientIP;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //�w�qaction(�ΨӶ�ť�ʧ@)
        
        mManager = (WifiP2pManager) this.getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this.getActivity(), this.getActivity().getMainLooper(), null);
        //��l��WiFiDirect
        
        discoverpeers();
        
        setAdapter(rootView);
        //��s�e����ListView
        
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
    	
		//Toast.makeText(this.getActivity().getApplicationContext(),"�@��: "+avaliablePeersNumber+"�ӨϥΪ�",Toast.LENGTH_SHORT).show();
    	
    	//�s�}�C���e(�s�u���˸m�W��)
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
                deviceNumber = position;  //����device���s��

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
        //�]�w���f
    	
        setListAdapter(adapter);
        //���汵�f
	}
    
    //��ListView���Q�I���ɹB�檺�禡
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
    
    //������I���ܰʮɩҶ]���禡
    @Override
    public void onPeersAvailable (WifiP2pDeviceList peers){
    	this.peers.clear();
    	//�ΨӦ����ҵo�{��peers�ƶq,�é�Jnum_avaliablepeers
    	Collection<WifiP2pDevice> collection = peers.getDeviceList(); 
    	avaliablePeersNumber =collection.size();
    	
    	this.peers.addAll(peers.getDeviceList());
    	//Toast.makeText(rootView.getContext(),"onPeersAvailable,�@��: "+avaliablePeersNumber+"�ӨϥΪ�",Toast.LENGTH_SHORT).show();
    	setAdapter(rootView);
    	if(avaliablePeersNumber>0 && !isConnecting){
    		
    		for(int i=0; i<this.peers.size(); i++)
        	{
        		WifiP2pDevice device = this.peers.get(i);
        		Toast.makeText(rootView.getContext(),"deviceAddress["+i+"]:"+device.deviceAddress,Toast.LENGTH_SHORT).show();
        		if(device.deviceAddress.equals("9a:e7:9a:2b:23:75")){
        			connectpeers(i);
        			Toast.makeText(rootView.getContext(),"connectpeers(i)",Toast.LENGTH_SHORT).show();
        		}
        	}
    	}
    }
    
	@SuppressLint("NewApi")
	public void connectpeers(int d_number) {
		
		device =  (WifiP2pDevice) peers.get(d_number);
		devices_info = device;
		
		mManager.removeGroup(mChannel, null);
		
		HashMap<String, String> record = new HashMap<String, String>();

		WifiP2pServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance("_hello", "_world._tcp", record);
		
		mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
			
			@Override
			public void onFailure(int reason) {
			}
			
			@Override
			public void onSuccess() {
				//create group, making this device the owner of the group
				mManager.createGroup(mChannel, null);
			}
		});
		
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		//config.groupOwnerIntent = 15;  //�T�{�Q�s���@�w�OGO(Group Owner)
		
		mManager.connect(mChannel, config, new ActionListener(){

			@Override
			public void onFailure(int reason) {
				String reasonString = null;
				switch(reason){
		    	case 0:
		    		reasonString = "�������~";
		    		break;
		    	case 1:
		    		reasonString = "�z���]�ƨS���䴩WifiP2p�\��";
		    		break;
		    	case 2:
		    		reasonString = "Framework is busy";
		    		break;
		    	default:
		    		reasonString ="���������~!";
		    		break;
		    	}
				Toast.makeText(getActivity(), "�s������,���ѭ�]:"+reasonString, Toast.LENGTH_SHORT).show();
				
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
		String connectedInfos="";
		boolean isOwnerInfo = info.isGroupOwner;
        boolean isFormedInfo = info.groupFormed;
        
        localIP = Utils.getIPAddress(true);
        IP_SERVER = info.groupOwnerAddress.getHostAddress();
        /*
        String client_mac_fixed = new String(device.deviceAddress).replace("99", "19");
        clientIP = Utils.getMACAddress(client_mac_fixed);
        */
        /*
		if(clientIP.equals(IP_SERVER)){
			clientIP = device.deviceAddress;
			connectedInfos = clientIP;
		}
		else{
			connectedInfos = IP_SERVER;
		}
		*/
		//connectedInfos = info.groupOwnerAddress.getHostAddress();
		//Toast.makeText(getActivity(), "info:"+connectedInfo, Toast.LENGTH_LONG).show();
		
		if(info.groupFormed && info.isGroupOwner){
		//	clientIP = device.deviceAddress;
			connectedInfos = "192.168.1.108";
			Toast.makeText(getActivity(), "�ڬO�ժ��A�ڥؼжǵ�IP "+connectedInfos, Toast.LENGTH_LONG).show();
		}else{
			connectedInfos = IP_SERVER;
			Toast.makeText(getActivity(), "�ڤ��O�ժ��A�ڥؼжǵ�IP "+connectedInfos, Toast.LENGTH_LONG).show();
		}
		
		Toast.makeText(getActivity(), "connectedInfos: "+connectedInfos, Toast.LENGTH_LONG).show();
		
		//Toast.makeText(getActivity(), "OnConnectionAvaliable-running",Toast.LENGTH_SHORT).show();

		

		//�n������...�n�p��q�Q�s�������A���device����T
		//Toast.makeText(getActivity(), "�ժ�:"+info.isGroupOwner, Toast.LENGTH_SHORT).show();
		Bundle args = new Bundle();
    	
    	Map<String,Object> item = new HashMap<String,Object>();
        item = data.get(deviceNumber);
        
        args.putString("deviceName", (String) item.get("deviceName"));
        args.putString("connectedInfo",connectedInfos);  //��connectionInfo��t�@��fragment
        args.putBoolean("isOwnerInfo",isOwnerInfo);  //��detailInfo��t�@��fragment
        args.putBoolean("isFormedInfo",isFormedInfo);  //��detailInfo��t�@��fragment
        Fragment fragment = new FilesSelect();
        fragment.setArguments(args);
		FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_files, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
	}
}