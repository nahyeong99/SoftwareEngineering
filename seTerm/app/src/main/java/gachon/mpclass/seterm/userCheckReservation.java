package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userCheckReservation extends AppCompatActivity {
    TextView userReservationCheck;
    String customerUid="";
    String arrayItem="";
    ArrayAdapter adapter;
    String managerUid;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ValueEventListener mDatabase;
    private ArrayList<String> items = new ArrayList<>();
    public static List<String> keyList = new ArrayList<String>();
    private DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_reservation);
        userReservationCheck = (TextView)findViewById(R.id.userReservationCheck);

        Button back = (Button)findViewById(R.id.joinInUserBack) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);
         user=firebaseAuth.getCurrentUser();//현재 customer가 user
        FirebaseDatabase.getInstance().getReference().child("Managers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                   Manager manager= ds.getValue(Manager.class);
                  managerUid = ds.getKey();
                    keyList.add(ds.getKey());
                    if(ds.hasChild("reservation"))
                    {
                        FirebaseDatabase.getInstance().getReference().child("Managers").child(managerUid).child("reservation").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    reservation reservation1 = ds.getValue(reservation.class);
                                    keyList.add(ds.getKey());
                                    if(ds.hasChild("CustomerUid"))
                                    {
                                        customerUid = ds.child("CustomerUid").getValue().toString();
                                        if(customerUid.equals(user.getUid())){
                                            String flowernum = ds.child("flowernum").getValue().toString();

                                            Log.d("userCheckReservation", "꽃 개수: "+flowernum);
                                            arrayItem = arrayItem+ "\n꽃 개수: "+flowernum+"\nmanagerUid: "+managerUid;
                                            userReservationCheck.setText(arrayItem);
                                        }
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                      //adapter.add(items);


                    }

                }
                //

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}