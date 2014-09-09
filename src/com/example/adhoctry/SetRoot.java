package com.example.adhoctry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SetRoot extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.root_set, container, false);
		//設定碎片容器
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		Fragment fm = new SetMain();
		fm.setArguments(getArguments());
		//繼續傳遞bundle
		transaction.replace(R.id.root_set, fm);
		transaction.commit();
		//在容器上創建第一頁的fragment(各分頁的首頁)

		return view;
	}
}
