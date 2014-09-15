package com.example.adhoctry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.adhoctry.CollectionMain.ListCursorAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.bluetooth.BluetoothClass.Device;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FilesMain extends ListFragment{
	
	View rootView;
	String[] device = {"SyuWei 的 iphone", "Lu's GSmart", "Dream's Z1", "虹音 的 小米3"};
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_files_main, container, false);
        
        setAdapter(rootView);
        //更新畫面的ListView
        
        return rootView;
    }
    
    private void setAdapter(View rootView) {
        
    	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
    	
    	//存陣列內容(連線的裝置名稱)
    	for(int i=0; i<device.length; i++)
    	{
    		Map<String,Object> item = new HashMap<String,Object>();
    		item.put("device",device[i]);
        	data.add(item);
    	}
    	
    	SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),data,R.layout.listview_files,new String[]{"device"},new int[]{R.id.device});
        //設定接口
    	
        setListAdapter(adapter);
        //執行接口
	}
}
