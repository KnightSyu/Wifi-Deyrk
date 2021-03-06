package com.example.Circle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Circle.R;

public class MainDeyrk extends FragmentActivity implements ActionBar.TabListener,OnPageChangeListener,PeerListListener,ConnectionInfoListener {
	
    SectionsPagerAdapter mSectionsPagerAdapter; //宣告接口(五碎片刷來刷去的樣式)
    ViewPager mViewPager;
    public Intent serviceIntent;
    public static WifiP2pManager mManager;
	public static Channel mChannel;
	private static WiFiDirectBroadcastReceiver mReceiver;
	private static IntentFilter mIntentFilter = new IntentFilter();
	public static int avaliablePeersNumber = 0;
	private WifiP2pInfo connectedInfo;
	private static WifiP2pDevice devices_info;
	private int connectionCount = 0;
	public boolean isConnected = false;
	public static boolean cancelConnect = false;
	private int PORT8888 =8888;
	private boolean btn_send = false;
	public static List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	public static List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	public static WifiP2pDevice device;
	public static int deviceNumber;
	public static String IP_SERVER;
	String localIP;
	String clientIP;
	static FilesMain FM = new FilesMain();
	static FilesSelect FS = new FilesSelect();
	static ReceiveMain RM = new ReceiveMain();
	static CollectionMain CM = new CollectionMain();
	static PushMain PM = new PushMain();
	public static boolean isOwnerInfo;
	public static boolean isFormedInfo;
	static final Handler handler = new Handler();
	static Runnable runnable_connect;
	static Runnable runnable_push;
	DB mDbHelper = new DB(this);
	private PushServerAsyncTask psat;
	int nowPage = 0;
	private FileServerAsyncTask fsat;
	private int PORT1 =8898;
	private int PORT2 =8899;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deyrk_main);
        //設定容器(這個是最底層的容器)
        
        serviceIntent = new Intent(this,FileTransferService.class);
        
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //定義action(用來傾聽動作)
        
        runnable_connect = new Runnable() {
            public void run() {
            	if(avaliablePeersNumber>0 && !isConnected){
            		for(int i=0; i<peers.size(); i++)
            		{
            			WifiP2pDevice device = peers.get(i);
            			if(device.deviceAddress.equals("9a:e7:9a:2b:23:75")){
            				Toast.makeText(getApplicationContext(), "發出連線要求！",Toast.LENGTH_SHORT).show();
            				connectpeers(i);
            			}
            		}
            	}
            	else{
            		pushAndReceive();
            	}
            }
        };
        
        runnable_push = new Runnable() {
            public void run() {
            	pushAndReceive();
            }
        };
        
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //設定action bar樣式(上面五個分頁頁籤的樣式)
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //設定接口(設定對應的五個fragment)

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //設定ViewPager(頁面)
        mViewPager.setOffscreenPageLimit(10);
        //保存每頁的狀態(刷來刷去不會跳回分頁首頁)
        
        //刷來刷去時切換目前頁籤的傾聽器
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	nowPage = position;
            	if(nowPage==0){
            		/*try {
						fsat.serverSocket.close();
					} catch (Exception e1) {
						Toast.makeText(getApplicationContext(), "fsat e: "+e1.getMessage(),Toast.LENGTH_SHORT).show();
					}*/
            		try {
						RM.setAdapter();
					} catch (Exception e) {
					}
            		handler.post(MainDeyrk.runnable_connect);
            	}
            	else if(nowPage==1){
            		/*try {
						fsat.serverSocket.close();
					} catch (Exception e1) {
						Toast.makeText(getApplicationContext(), "fsat e: "+e1.getMessage(),Toast.LENGTH_SHORT).show();
					}*/
            		try {
						PM.setAdapter();
					} catch (Exception e) {
						
					}
            		//pushAndReceive();
            	}
            	else if(nowPage==2){
            		try {
						psat.serverSocket.close();
					} catch (Exception e1) {
						Toast.makeText(getApplicationContext(), "psat e: "+e1.getMessage(),Toast.LENGTH_SHORT).show();
					}
            		if(isOwnerInfo && isFormedInfo){
                		try{
                			fsat = new FileServerAsyncTask(getApplicationContext());
                    		fsat.execute(PORT1+"");
                    		Toast.makeText(getApplicationContext(), "owner等待接收檔案！",Toast.LENGTH_SHORT).show();
                    	}catch(Exception e){
                    		Toast.makeText(getApplicationContext(), "oe: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    	}
                	}
                	else if(isFormedInfo){
                		try{
                			fsat = new FileServerAsyncTask(getApplicationContext());
                    		fsat.execute(PORT2+"");
                    		Toast.makeText(getApplicationContext(), "client等待接收檔案！",Toast.LENGTH_SHORT).show();
                    	}catch(Exception e){
                    		Toast.makeText(getApplicationContext(), "ce: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    	}
                	}
            	}
            	else if(nowPage==3){
            		try {
						CM.setAdapter();
					} catch (Exception e) {
						
					}
            	}
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //設定每個頁籤的文字內容
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
        	
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        handler.postDelayed(MainDeyrk.runnable_connect, 5000);
    }
    
    public void initialize_WiFiP2PManager(){
    	mManager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, this.getMainLooper(), null);
        //初始化WiFiDirect
    }
    
    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        initialize_WiFiP2PManager();
        
        discoverpeers();
        mReceiver = new WiFiDirectBroadcastReceiver(mChannel, mManager, this);
        this.registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        this.unregisterReceiver(mReceiver);
        handler.removeCallbacks(runnable_connect);
        handler.removeCallbacks(runnable_push);
        super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//內建的"設定" (Setting那個)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //當頁籤被點擊，切換為該頁籤畫面
    	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	//當頁籤沒有被選擇時會呼叫的函式
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	
    }
    
    //設定接口(每個頁籤對應的分頁)
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	//選不同的頁面就建不同的碎片
        	
        	Fragment fragment;
        	Bundle args = new Bundle();
        	args.putInt("section_number", position + 1);
        	//傳bundle給碎片(目前所選的頁面位置+1)
        	
        	switch (position) {
        		case 0:
        			fragment = new ReceiveRoot();
                    fragment.setArguments(args);
        			return fragment;
        		case 1:
        			fragment = new PushRoot();
                    fragment.setArguments(args);
        			return fragment;
        		case 2:
        			fragment = new FilesRoot();
                    fragment.setArguments(args);
        			return fragment;
        		case 3:
        			fragment = new CollectionRoot();
                    fragment.setArguments(args);
        			return fragment;
        		case 4:
        			fragment = new SetRoot();
                    fragment.setArguments(args);
        			return fragment;
        	}
        	return null;
        }

        @Override
        public int getCount() {
            //設定有幾個頁籤
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	
        	//設定頁籤的標題
        	
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }
    }
    
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}
	
	private static void discoverpeers() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            	
            }

            @Override
            public void onFailure(int reasonCode) {
            	
            }
        });
	}
	
	@SuppressLint("NewApi")
	private void stopPeerDiscovery() {
		mManager.stopPeerDiscovery(mChannel, new ActionListener() {
			@Override
            public void onSuccess() {
            	
            }

            @Override
            public void onFailure(int reasonCode) {
            	
            }
		});
	}
	
	//當附近的點有變動時所跑的函式
    @Override
    public void onPeersAvailable (WifiP2pDeviceList peers){
    	MainDeyrk.peers.clear();
    	//用來收集所發現的peers數量,並放入num_avaliablepeers
    	Collection<WifiP2pDevice> collection = peers.getDeviceList(); 
    	avaliablePeersNumber =collection.size();
    	
    	MainDeyrk.peers.addAll(peers.getDeviceList());
    	//Toast.makeText(rootView.getContext(),"onPeersAvailable,共有: "+avaliablePeersNumber+"個使用者",Toast.LENGTH_SHORT).show();
    	
    	try{
    		FM.setAdapter();
    	}catch(Exception e){
    		
    	}
    }
    
    public static void cancelConnectNow(){
    	mManager.cancelConnect(mChannel, new ActionListener() {
			
			@Override
			public void onSuccess() {
				
			}
			
			@Override
			public void onFailure(int reason) {
				
			}
		});
    }
    
    public void pushAndReceive(){
    	if(isOwnerInfo && isFormedInfo){
        	for(int i=0; i<peers.size(); i++)
    		{
    			WifiP2pDevice device = peers.get(i);
    			if(device.deviceAddress.equals("9a:e7:9a:2b:23:75")){
    				Toast.makeText(getApplicationContext(), "開始接收商家推播訊息！",Toast.LENGTH_SHORT).show();
    				//server
    				psat = new PushServerAsyncTask(this);
    				psat.execute(PORT8888+"");
    			}
    		}
        }
        else if(isFormedInfo){
        	Toast.makeText(getApplicationContext(), "我是商家！",Toast.LENGTH_SHORT).show();
        	//client
            
            Handler handler = new Handler() {
    		    @Override
    		    public void handleMessage(Message msg) {
    		    	Bundle reply = msg.getData();
    		    	Toast.makeText(getApplicationContext(),reply.getString("sended"),Toast.LENGTH_SHORT).show();
    		    }
    		};
        	
        	Intent serviceIntent = new Intent(this, PushClientService.class);
            /*
            serviceIntent.putExtra(PushClientService.EXTRAS_TITLE, title);
            serviceIntent.putExtra(PushClientService.EXTRAS_CONTEXT, context_);
            serviceIntent.putExtra(PushClientService.EXTRAS_KIND, kind);
            serviceIntent.putExtra(PushClientService.EXTRAS_IMAGE_SIZE, img.length);
            serviceIntent.putExtra(PushClientService.EXTRAS_IMAGE_BYTEARRAY, img);*/
            serviceIntent.putExtra(PushClientService.EXTRAS_GROUP_OWNER_ADDRESS, IP_SERVER);
            serviceIntent.putExtra(PushClientService.EXTRAS_PORT, PORT8888);
            serviceIntent.putExtra("messenger", new Messenger(handler));
    		startService(serviceIntent);
            
    		//handler.postDelayed(MainDeyrk.runnable_push,5000);
        }
    }
    
    @Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		
		//Toast.makeText(getActivity(), "OnConnectionAvaliable-running",Toast.LENGTH_SHORT).show();
		
    	//stopPeerDiscovery();
		/*
		mManager.requestGroupInfo(mChannel,new WifiP2pManager.GroupInfoListener() {
	        @Override
	        public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
	            Collection<WifiP2pDevice> peerList = wifiP2pGroup.getClientList();
	            ArrayList<WifiP2pDevice> list = new ArrayList<WifiP2pDevice>(peerList);
	            if(list.size()>0){
	            	//組長才會知道有幾個裝置連上群組，list.size不包括組長
	            }
	        }
	    });
		*/
		connectedInfo = info;
		isOwnerInfo = info.isGroupOwner;
		isFormedInfo = info.groupFormed;
        
        localIP = Utils.getIPAddress(true);
        IP_SERVER = info.groupOwnerAddress.getHostAddress();
        
        try{
    		FM.changeToSelect();
    	}catch(Exception e){
    		
    	}
        if(nowPage==0 || nowPage==1){
        	pushAndReceive();
        }
        
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
		
		//Toast.makeText(getActivity(), "isConnected: "+isConnected, Toast.LENGTH_LONG).show();

		//要做的事...要如何從被連接的狀態抓到device的資訊
		//Toast.makeText(getActivity(), "組長:"+info.isGroupOwner, Toast.LENGTH_SHORT).show();
	}
    
    @SuppressLint("NewApi")
	public static void connectpeers(int d_number) {
	    
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
		//config.groupOwnerIntent = 15;  //確認被連的一定是GO(Group Owner)
		
		mManager.connect(mChannel, config, new ActionListener(){

			@Override
			public void onFailure(int reason) {
				String reasonString = null;
				switch(reason){
		    	case 0:
		    		reasonString = "網路錯誤";
		    		break;
		    	case 1:
		    		reasonString = "您的設備沒有支援WifiP2p功能";
		    		break;
		    	case 2:
		    		reasonString = "Framework is busy";
		    		break;
		    	default:
		    		reasonString ="未知的錯誤!";
		    		break;
		    	}
				//mManager.removeGroup(mChannel, null);
				//cancelConnectNow();
				discoverpeers();
			}

			@Override
			public void onSuccess() {
				handler.removeCallbacks(MainDeyrk.runnable_connect);
				handler.removeCallbacks(runnable_push);
			}
			
		});
		
	}
    
}
