package com.example.Circle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Circle.R;

public class ReceiveMain extends ListFragment {
	
    private DB mDbHelper; //��@DB���O
    private Cursor mCursor; //���Ʈw��ƪ��e��
    private View rootView;
    private ListView kind;
    private SlidingDrawer sd;

    public ReceiveMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_main, container, false);
    	//�]�w�e���ҹ�����XML��
    	
        setAdapter();
        //��s�e����ListView
        
        kind = (ListView) rootView.findViewById(R.id.content_receive);
        sd = (SlidingDrawer) rootView.findViewById(R.id.slidingDrawer_receive);
        kind.setOnItemClickListener(new OnItemClickListener(){
        	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(view.getContext(), "onItemClick: "+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
				sd.animateClose();
			}
        });
        
    	return rootView;
    }
    

	public void setAdapter() {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //���}DB
        
        mCursor = mDbHelper.getAllReceive();
        //�I�sDB��getAll�禡�A���o��Ʃ�imCursor(��Ʈw��ƪ��e��)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //��@ListCursorAdapter���f(�]�w�n�b�e���W��ܪ�ListView�˦��A�ǤJListView�Ҧb��activity��mCursor�ӳ]�w)
        
        setListAdapter(cadapter);
        //���汵�f
        
	}
	
	//ListCursorAdapter���f
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//�qcursor�̮���ƥX�ө��e���W
			
			TextView title =(TextView)view.findViewById(R.id.title_ad);
			TextView time = (TextView)view.findViewById(R.id.time);
			TextView kind = (TextView)view.findViewById(R.id.kind);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			kind.setText(cursor.getString(
					cursor.getColumnIndex(DB.KEY_KIND)));
			
			//byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			//image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			//�qcursor���Ϥ��X�����ɩ�bImageView���{���X
			
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.listview_receive_collection,null);
			//�]�wListView�˦�
			bindView(v,context,cursor);
			
			return v;
		}
	}
	
	//�I����اR�����
	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    mDbHelper.delete(id);
		
		setAdapter(rootView);
	}*/
	
	//��ListView���Q�I���ɹB�檺�禡
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    

	    FragmentTransaction trans = getFragmentManager().beginTransaction();  
        Fragment fragment = new ReceiveAD();
        trans.replace(R.id.root_receice, fragment);
        Bundle args = new Bundle();
        args.putLong("section_id", id);
        fragment.setArguments(args);

	 
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
        
        //�Nroot_receice(���ðϪ����h�e��)��e��fragment������ReceiveAD
	    
	}
	
}