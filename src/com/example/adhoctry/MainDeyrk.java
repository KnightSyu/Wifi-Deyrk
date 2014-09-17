package com.example.adhoctry;

import java.util.Locale;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

public class MainDeyrk extends FragmentActivity implements ActionBar.TabListener,OnPageChangeListener {
	
    SectionsPagerAdapter mSectionsPagerAdapter; //宣告接口(五碎片刷來刷去的樣式)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deyrk_main);
        //設定容器(這個是最底層的容器)
        
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //設定action bar樣式(上面五個分頁頁籤的樣式)
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //設定接口(設定對應的五個fragment)

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //設定ViewPager(頁面)
        mViewPager.setOffscreenPageLimit(5);
        //保存每頁的狀態(刷來刷去不會跳回分頁首頁)
        
        //刷來刷去時切換目前頁籤的傾聽器
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //設定每個頁籤的文字內容
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
        	
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
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
}
