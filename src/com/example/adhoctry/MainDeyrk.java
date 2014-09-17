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
	
    SectionsPagerAdapter mSectionsPagerAdapter; //�ŧi���f(���H����Ө�h���˦�)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deyrk_main);
        //�]�w�e��(�o�ӬO�̩��h���e��)
        
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
}
