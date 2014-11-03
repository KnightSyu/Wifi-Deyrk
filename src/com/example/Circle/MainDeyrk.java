package com.example.Circle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.os.Bundle;
import android.os.Handler;
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
	
    SectionsPagerAdapter mSectionsPagerAdapter; //�ŧi���f(���H����Ө�h���˦�)
    ViewPager mViewPager;
    public Intent serviceIntent;
    public static WifiP2pManager mManager;
	public static Channel mChannel;
	private WiFiDirectBroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter = new IntentFilter();
	public static int avaliablePeersNumber = 0;
	private WifiP2pInfo connectedInfo;
	private static WifiP2pDevice devices_info;
	private int connectionCount = 0;
	private boolean isConnected = false;
	public static boolean cancelConnect = false;
	private int PORT =8898;
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
	public static boolean isOwnerInfo;
	public static boolean isFormedInfo;
	static final Handler handler = new Handler();
	static Runnable runnable;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deyrk_main);
        //�]�w�e��(�o�ӬO�̩��h���e��)
        
        serviceIntent = new Intent(this,FileTransferService.class);
        
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //�w�qaction(�ΨӶ�ť�ʧ@)

        mManager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, this.getMainLooper(), null);
        //��l��WiFiDirect
        
        discoverpeers();
        
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //�]�waction bar�˦�(�W�����Ӥ������Ҫ��˦�)
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //�]�w���f(�]�w����������fragment)

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //�]�wViewPager(����)
        mViewPager.setOffscreenPageLimit(5);
        //�O�s�C�������A(��Ө�h���|���^��������)
        
        //��Ө�h�ɤ����ثe���Ҫ���ť��
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //�]�w�C�ӭ��Ҫ���r���e
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
        	
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        runnable = new Runnable() {
            public void run() {
            	if(avaliablePeersNumber>0){
            		for(int i=0; i<peers.size(); i++)
            		{
            			WifiP2pDevice device = peers.get(i);
            			if(device.deviceAddress.equals("9a:e7:9a:2b:23:75")){
            				Toast.makeText(getApplicationContext(), "�o�X�s�u�n�D�I",Toast.LENGTH_SHORT).show();
            				connectpeers(i);
            			}
            		}
            	}else{
            		handler.postDelayed(this,5000);
            		//Toast.makeText(getApplicationContext(), "handler�����A����I",Toast.LENGTH_SHORT).show();
            	}
            }
        };
        
        handler.post(MainDeyrk.runnable);
    }
    
    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WiFiDirectBroadcastReceiver(mChannel, mManager, this);
        this.registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        this.unregisterReceiver(mReceiver);
        handler.removeCallbacks(runnable);
        super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//���ت�"�]�w" (Setting����)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //���ҳQ�I���A�������ӭ��ҵe��
    	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	//���ҨS���Q��ܮɷ|�I�s���禡
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	
    }
    
    //�]�w���f(�C�ӭ��ҹ���������)
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	//�藍�P�������N�ؤ��P���H��
        	
        	Fragment fragment;
        	Bundle args = new Bundle();
        	args.putInt("section_number", position + 1);
        	//��bundle���H��(�ثe�ҿ諸������m+1)
        	
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
            //�]�w���X�ӭ���
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	
        	//�]�w���Ҫ����D
        	
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
	
	private void discoverpeers() {
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
	
	//������I���ܰʮɩҶ]���禡
    @Override
    public void onPeersAvailable (WifiP2pDeviceList peers){
    	MainDeyrk.peers.clear();
    	//�ΨӦ����ҵo�{��peers�ƶq,�é�Jnum_avaliablepeers
    	Collection<WifiP2pDevice> collection = peers.getDeviceList(); 
    	avaliablePeersNumber =collection.size();
    	
    	MainDeyrk.peers.addAll(peers.getDeviceList());
    	//Toast.makeText(rootView.getContext(),"onPeersAvailable,�@��: "+avaliablePeersNumber+"�ӨϥΪ�",Toast.LENGTH_SHORT).show();
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
	            	//�ժ��~�|���D���X�Ӹ˸m�s�W�s�աAlist.size���]�A�ժ�
	            }
	        }
	    });
		*/
		connectedInfo = info;
		isOwnerInfo = info.isGroupOwner;
		isFormedInfo = info.groupFormed;
        
        localIP = Utils.getIPAddress(true);
        IP_SERVER = info.groupOwnerAddress.getHostAddress();
        
        if(isOwnerInfo && isFormedInfo){
        	for(int i=0; i<peers.size(); i++)
    		{
    			WifiP2pDevice device = peers.get(i);
    			if(device.deviceAddress.equals("9a:e7:9a:2b:23:75")){
    				Toast.makeText(getApplicationContext(), "�P�Ӯa�s�u���I",Toast.LENGTH_SHORT).show();
    			}
    		}
        }
        else if(isFormedInfo){
        	Toast.makeText(getApplicationContext(), "�ڬO�Ӯa�I",Toast.LENGTH_SHORT).show();
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

		//�n������...�n�p��q�Q�s�������A���device����T
		//Toast.makeText(getActivity(), "�ժ�:"+info.isGroupOwner, Toast.LENGTH_SHORT).show();
        
        try{
        	FM.changeToSelect();
    	}catch(Exception e){
    		
    	}
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
				mManager.removeGroup(mChannel, null);
				cancelConnectNow();
			}

			@Override
			public void onSuccess() {
				handler.removeCallbacks(MainDeyrk.runnable);
			}
			
		});
		
	}
}
