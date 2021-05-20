package gachon.mpclass.seterm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDate;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FlowerListView_Adapter extends BaseAdapter {
    int cnt = 0;
    Context context;
    ArrayList<FlowerListViewItem> data;
    int check = 0;
    FirebaseAuth uAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Managers").child(user.getUid()).child("Flowers");

    FlowerListView_Adapter(Context context) {
        this.context = context;
    }

    public FlowerListView_Adapter(ArrayList<FlowerListViewItem> data) {
        this.data = data;

    }

    public void clear() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void showButton(ImageButton btn) {
        btn.setVisibility(View.VISIBLE);
    }

    public void hideButton(ImageButton btn) {
        btn.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FlowerListViewItem list = data.get(position);
        convertView = inflater.inflate(R.layout.listviewflower, parent, false);
        TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_preview);
        String Uid = user.getUid(); //현재사용
        String post = list.getUid(); //글쓴사용자


        showButton(delete);
        delete.setOnClickListener(new View.OnClickListener() {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://seterm-d4ac0.appspot.com").child("flower/" + list.getFileName());

            @Override
            public void onClick(View v) {
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //사진 삭제 성공

                    }
                });
                dbRef.child(G.keyList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        if (list.getImgUrl() == null)//사진이 없을때
        {
            textView1.setText("[" + list.getFlowercolor() + "] ");
            textView2.setText(list.getFlowername());
            textView3.setText(" (남은 수량 : " + list.getFlowernumber() + "개)");
        } else {
            textView1.setText("[" + list.getFlowercolor() + "] ");
            textView2.setText(list.getFlowername());
            textView3.setText(" (남은 수량 : " + list.getFlowernumber() + "개)");
            Glide.with(convertView)
                    .load(list.getImgUrl())
                    .into(imageView);
        }


        cnt++;
        return convertView;
    }
}
