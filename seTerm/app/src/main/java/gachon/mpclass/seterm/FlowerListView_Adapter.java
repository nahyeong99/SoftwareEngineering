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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    String flowercolor;
    String flowername;
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


        String Uid = user.getUid(); //????????????
        String post = list.getUid(); //???????????????


        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flowerNum = editTextflowerNum.getText().toString();
                int f1 = Integer.parseInt(list.getFlowernumber());
                int f2 = Integer.parseInt(flowerNum);
                int f3 = f1-f2;
                if (f1>=f2) {

                    String flower = Integer.toString(f3);

                    String managerID = list.getUid();
                    DatabaseReference rf = reserveRef.child(managerID).child("reservation");
                    flowerUid = dbRef.child(G.keyList.get(position)).getKey();
                    list.setFlowernumber(flower);
                    FirebaseDatabase.getInstance().getReference().child("Managers").child(post).child("Flowers").child(G.keyList.get(position)).child("flowernumber").setValue(flower);
              /*  dbRef.child(G.keyList.get(position)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));

                            flowercolor = task.getResult().child("flowercolor").getValue().toString();
                            flowername = task.getResult().child("flowername").getValue().toString();
                            Log.d("TAG", "??? ??????: " + flowername);
                        }
                    }
                });*/
                    String uUid = user.getUid();//??? ?????? ????????? customer ?????????
                    Log.d("TAG", "??? ?????????: " + flowerUid);
                    G.hashMap.put("CustomerUid", uUid);
                    G.hashMap.put("flowernum", flowerNum);
                    G.hashMap.put("flowerUid", flowerUid);
                    G.hashMap.put("flowername", flowername);
                    G.hashMap.put("flowercolor", flowercolor);
                    rf.push().setValue(G.hashMap);
                    G.hashMap.clear();
                    notifyDataSetChanged();
//                Intent intent = new Intent(parent.getContext(),userCheckReservation.class);
//                context.startActivity(intent);
                } else {
                    Toast.makeText(parent.getContext(), "?????? ???????????? ??? ?????? ????????? ??? ????????????", Toast.LENGTH_SHORT);
                }


            }
        });
        try {
            if (Uid != null) {
                if (Uid.equals(post)) {

                    showButton(delete);
                    delete.setOnClickListener(new View.OnClickListener() {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://seterm-d4ac0.appspot.com").child("flower/" + list.getFileName());

                        @Override
                        public void onClick(View v) {
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //?????? ?????? ??????

                                }
                            });
                            dbRef.child(G.keyList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    showButton(change);
                    change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View view = LayoutInflater.from(context).inflate(R.layout.activity_edit, null, false);
                            builder.setView(view);
                            final Button edit = (Button) view.findViewById(R.id.upload);
                            final EditText name = (EditText) view.findViewById(R.id.flowername);
                            final EditText color = (EditText) view.findViewById(R.id.flowercolor);
                            final EditText num = (EditText) view.findViewById(R.id.flowernumber);

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
                            });
                            dialog.show();
                        }
                    });
                } else {
                    hideButton(delete);
                    hideButton(change);

                }
            }
        } catch (NullPointerException ignored) {

        }

        if (list.getImgUrl() == null)//????????? ?????????
        {
            textView1.setText("[" + list.getFlowercolor() + "] ");
            textView2.setText(list.getFlowername());
            textView3.setText(" (?????? ?????? : " + list.getFlowernumber() + "???)");
        } else {
            textView1.setText("[" + list.getFlowercolor() + "] ");
            textView2.setText(list.getFlowername());
            textView3.setText(" (?????? ?????? : " + list.getFlowernumber() + "???)");
            Glide.with(convertView)
                    .load(list.getImgUrl())
                    .into(imageView);
        }


        cnt++;
        return convertView;
    }
}
