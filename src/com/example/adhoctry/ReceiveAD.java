package com.example.adhoctry;

import com.example.adhoctry.ReceiveMain.ListCursorAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReceiveAD extends Fragment {
	 private DB mDbHelper; //��@DB���O
	 private Cursor mCursor; //���Ʈw��ƪ��e��
	 private View rootView;
	
	public ReceiveAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_ad, container, false);
    	//�]�w�e���ҹ�����XML��
    	
    	return rootView;
    }
	/*private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //���}DB
        
        mCursor = mDbHelper.getAll();
        //�I�sDB��getAll�禡�A���o��Ʃ�imCursor(��Ʈw��ƪ��e��)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //��@ListCursorAdapter���f(�]�w�n�b�e���W��ܪ�ListView�˦��A�ǤJListView�Ҧb��activity��mCursor�ӳ]�w)
        
        setListAdapter(cadapter);
        //���汵�f
        
	}*/
    
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//�qcursor�̮���ƥX�ө��e���W
			
			TextView adtitle =(TextView)view.findViewById(R.id.ad_receive_title);
			TextView adtime = (TextView)view.findViewById(R.id.ad_receive_time);
			TextView adcontext = (TextView)view.findViewById(R.id.ad_receive_context);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			adtitle.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			adtime.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			adcontext.setText(cursor.getString(
					cursor.getColumnIndex(DB.KEY_CONTEXT)));
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
}
