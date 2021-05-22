package gachon.mpclass.seterm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDate;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class FlowerListView_Adapter extends BaseAdapter {
    int cnt = 0;
    Context context;
    ArrayList<FlowerListViewItem> data;
    int check = 0;
    FirebaseAuth uAuth;
String flowerUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Managers").child(user.getUid()).child("Flowers");
    DatabaseReference reserveRef = FirebaseDatabase.getInstance().getReference().child("Managers");//child(user.getUid()).child("reservation");

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
        ImageButton change = (ImageButton) convertView.findViewById(R.id.change);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_preview);
        Button reserve = (Button) convertView.findViewById(R.id.reservationcheck);
        EditText editTextflowerNum = convertView.findViewById(R.id.userflowerNum);



        String Uid = user.getUid(); //현재사용
        String post = list.getUid(); //글쓴사용자

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flowerNum = editTextflowerNum.getText().toString();
                String managerID = list.getUid();
                DatabaseReference rf = reserveRef.child(managerID).child("reservation");
                flowerUid =  dbRef.child(G.keyList.get(position)).getKey();
               String uUid = user.getUid();//이 꽃을 예약한 customer 아이디
                Log.d("TAG", "꽃 아이디: "+flowerUid);
                G.hashMap.put("CustomerUid", uUid);
                G.hashMap.put("flowernum",flowerNum);
                G.hashMap.put("flowerUid",flowerUid);
                rf.push().setValue(G.hashMap);

            }
        });
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

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.activity_edit,null,false);
                builder.setView(view);
                final Button edit = (Button)view.findViewById(R.id.upload);
                final EditText name = (EditText)view.findViewById(R.id.flowername);
                final EditText color = (EditText)view.findViewById(R.id.flowercolor);
                final EditText num = (EditText)view.findViewById(R.id.flowernumber);

                name.setText(list.getFlowername());
                color.setText(list.getFlowercolor());
                num.setText(list.getFlowernumber());

                final AlertDialog dialog = builder.create();
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String editname = name.getText().toString();
                                String editcolor = color.getText().toString();
                                String editnum = num.getText().toString();
                                list.setFlowername(editname);
                                list.setFlowercolor(editcolor);
                                list.setFlowernumber(editnum);
                                dbRef.child(G.keyList.get(position)).child("flowername").setValue(editname);
                                dbRef.child(G.keyList.get(position)).child("flowercolor").setValue(editcolor);
                                dbRef.child(G.keyList.get(position)).child("flowernumber").setValue(editnum);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        dialog.dismiss();
                    }
                });dialog.show();
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
