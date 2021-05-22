package gachon.mpclass.seterm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;





import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class shopListAdapter extends BaseAdapter implements View.OnClickListener {
    private ListBtnClickListener listBtnClickListener = null;
public interface ListBtnClickListener{
    void onListBtnClick(int position, int resourceid);
}

        private Context context;
        private ArrayList<shopListItem> listItems = new ArrayList<shopListItem>();

        private OnCheckedListener doCheck;
        private String uid;
        public interface OnCheckedListener{
            void OnListBtnClick(View v, int pos, ImageButton imBtn);
        }

        public shopListAdapter(@NonNull Context context, int resource,@NonNull List objects, ListBtnClickListener listBtnClickListener){
            this.context = context;
            this.listBtnClickListener = listBtnClickListener;
        }

         public shopListAdapter(Context context,ListBtnClickListener listBtnClickListener){
        this.context = context;
             this.listBtnClickListener = listBtnClickListener;
        }
        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int i) {
            return listItems.get(i);
        }

        public Object getID(int i) {
            return listItems.get(i).getName();
        }
        public String getUid(int i){return listItems.get(i).getUid();}

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int pos = position;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, parent, false);
            }


            shopListItem listItem = listItems.get(position);
            final TextView nameTextView = (TextView) convertView.findViewById(R.id.shopName);
            final TextView AddressTextView = (TextView) convertView.findViewById(R.id.detailAddress);
            final TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance);
            final TextView uidTextView = (TextView) convertView.findViewById(R.id.uid);
         /*   Button gotoReserve = (Button) convertView.findViewById(R.id.gotoReserve);
            gotoReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,userReservationDetail.class);
                    intent.putExtra("uid",uid);
                }
            });*/

          /*  try{
                if (listItem.getDone().equals("false")){
                    doneCheck.setImageResource(R.drawable.done_not_checked_image);
                }
                else if(listItem.getDone().equals("true")){
                    doneCheck.setImageResource(R.drawable.done_checked_image);
                }
            }catch (Exception e){
                Log.d("error", "no!");
            }*/


            // 가져온 데이터를 텍스트뷰에 입력
            nameTextView.setText(listItem.getName());
            AddressTextView.setText(listItem.getDetailAddress());
            distanceTextView.setText(listItem.getDistance());
            String a = listItem.getUid();
            uidTextView.setText(listItem.getUid());
            Button gotoReserve = (Button) convertView.findViewById(R.id.gotoReserve);
            gotoReserve.setTag(position);
            gotoReserve.setOnClickListener(this);

            return convertView;
        }
        @Override
         public void onClick(View view) {
             if (this.listBtnClickListener != null) {
                 Log.d("TAG", "Double: 왕왕");
                 this.listBtnClickListener.onListBtnClick((int) view.getTag(), view.getId());
             }
         }

        public void addItem(String name, String detailAddress, String distance, String uid){
            shopListItem listItem = new shopListItem();

            listItem.setName(name);
            listItem.setDetailAddress(detailAddress);
            listItem.setDistance(distance);
            listItem.setUid(uid);


            listItems.add(listItem);
        }

        public void removeItem(int pos){
            listItems.remove(pos);
            notifyDataSetChanged();
        }


}
