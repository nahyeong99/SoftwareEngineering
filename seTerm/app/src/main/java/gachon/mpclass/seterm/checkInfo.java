package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//매니저가 예약 확인
public class checkInfo extends AppCompatActivity {
    ListView reservationListView;
    ArrayAdapter adapter;
    FirebaseAuth firebaseAuth;
    private ValueEventListener mDatabase;
    private ArrayList<String> items = new ArrayList<>();
    public static List<String> keyList = new ArrayList<String>();
    private DatabaseReference dataRef;
    String customerName;
    String flowerName;
    String flowerNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);
        firebaseAuth = FirebaseAuth.getInstance();

        reservationListView = findViewById(R.id.reservationListView);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,items);
         FirebaseUser user=firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Managers").child(user.getUid()).child("reservation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    reservation reservation1 = ds.getValue(reservation.class);
                    keyList.add(ds.getKey());
                    if(ds.hasChild("CustomerUid"))
                    {
                        customerName = ds.child("CustomerUid").getValue().toString();
                    }
                    if(ds.hasChild("flowernum"))
                    {
                        flowerNum = ds.child("flowernum").getValue().toString();
                    }
                    if(ds.hasChild("flowerUid"))
                    {
                        flowerName = ds.child("flowerUid").getValue().toString();
                    }





                    // num = ds.child("flowerNum").getValue(String.class);

                    adapter.add("꽃이름: "+flowerName+" 꽃개수: "+flowerNum+" 고객이름: "+customerName);
                    DatabaseReference crf = FirebaseDatabase.getInstance().getReference().child("Customers");
                    DatabaseReference frf = FirebaseDatabase.getInstance().getReference().child("Managers").child("Flowers");
/*
                   crf.child(CustomerUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DataSnapshot> task) {
                           if (!task.isSuccessful()) {
                               Log.e("firebase", "Error getting data", task.getException());
                           }
                           else {
                               Log.d("firebase", String.valueOf(task.getResult().getValue()));

                               customerName = task.getResult().child("name").getValue().toString();
                               customerPn = task.getResult().child("phonenumber").getValue().toString();

                           }
                       }
                   });
                   frf.child(CustomerUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DataSnapshot> task) {
                           if (!task.isSuccessful()) {
                               Log.e("firebase", "Error getting data", task.getException());
                           }
                           else {
                               Log.d("firebase", String.valueOf(task.getResult().getValue()));

                               flowerColor = task.getResult().child("flowercolor").getValue().toString();
                               flowerName = task.getResult().child("flowername").getValue().toString();
                           }
                       }
                   });
                  adapter.add("["+customerName+"]"+" 전화번호: "+customerPn+"\n"+flowerColor+flowerName+" "+num"송이");*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });

        reservationListView.setAdapter(adapter);
        dataRef = FirebaseDatabase.getInstance().getReference();

        reservationListView.setOnItemLongClickListener( new ListViewItemLongClickListener() );

    }
    int selectedPos = -1;


    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);
            FirebaseUser user=firebaseAuth.getCurrentUser();

            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    items.remove(selectedPos);
                    dataRef.child("Managers").child(user.getUid()).child("reservation").child(keyList.get(position)).removeValue();
                    keyList.remove(position);

                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }

            });

            // '아니오' 버튼이 클릭되면
            alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });


            alertDlg.setMessage( String.format( getString(R.string.alert_msg_delete),
                    items.get(position)) );
            alertDlg.show();
            return false;
        }

    }

}