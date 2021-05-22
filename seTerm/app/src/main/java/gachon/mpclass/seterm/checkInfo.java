package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
//매니저가 예약 확인
public class checkInfo extends AppCompatActivity {
    ListView reservationListView;
    ArrayAdapter adapter;
    FirebaseAuth firebaseAuth;
    private ValueEventListener mDatabase;
    String customerName;
    String customerPn;
    String flowerName;
    String flowerColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);
        firebaseAuth = FirebaseAuth.getInstance();

        reservationListView = findViewById(R.id.reservationListView);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1);
         FirebaseUser user=firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Managers").child(user.getUid()).child("reservation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    reservation reservation1 = ds.getValue(reservation.class);
                    String num = reservation1.getNum();
                    String CustomerUid = reservation1.getCustomerUid();
                    String flowerUid = reservation1.getFlowerUid();


                   // num = ds.child("flowerNum").getValue(String.class);
                   CustomerUid = ds.child("CustomerUid").getValue(String.class);
                   flowerUid = ds.child("flowerUid").getValue(String.class);
                    adapter.add("꽃이름: "+flowerUid+" 꽃개수: "+num+" 고객이름: "+CustomerUid);
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
    }
}