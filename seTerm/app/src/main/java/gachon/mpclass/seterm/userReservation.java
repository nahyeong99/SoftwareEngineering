package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class userReservation extends AppCompatActivity implements shopListAdapter.ListBtnClickListener{
    FirebaseAuth firebaseAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private ChildEventListener mChild;
    FirebaseUser user;
    Double userLongitude=0.0, userLatitude=0.0;
    Double managerLongitude=0.0, managerLatitude=0.0;
    Location userLocation;
    Location ManagerLocation;
    float distance = 0;
    String d;
    FirebaseUser manager;
    ArrayList<Manager> list =new ArrayList<Manager>();
    private ListView shopListView;
    shopListItem listItem;
    private shopListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservation);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference managerRef = mReference.child("Managers");
        firebaseAuth = FirebaseAuth.getInstance();

        shopListView = (ListView)findViewById(R.id.shopListView);
         listItem = new shopListItem();
         adapter = new shopListAdapter(userReservation.this,R.id.gotoReserve,list,this);
        shopListView.setAdapter(adapter);
        user = firebaseAuth.getCurrentUser();

     mReference.child("Customers").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    userLocation = new Location("userLocation");
                    ManagerLocation = new Location("managerLocation");

                    String latit = task.getResult().child("latitude").getValue().toString();
                    String longit = task.getResult().child("longitude").getValue().toString();

                    userLatitude = Double.parseDouble(latit);
                    userLongitude = Double.parseDouble(longit);

                        userLocation.setLatitude(userLatitude);
                        userLocation.setLongitude(userLongitude);
                    Toast.makeText(getApplicationContext(),userLatitude+" / "+userLongitude,Toast.LENGTH_LONG).show();
                }
            }
        });


        managerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Manager manager = ds.getValue(Manager.class);
                    list.add(manager);
                    String shopName = ds.child("name").getValue(String.class);
                    String uid = ds.getKey();
                    String detailAddress = ds.child("detailAdress").getValue(String.class);
                    String longitude = ds.child("longitude").getValue(String.class);
                    String latitude = ds.child("latitude").getValue(String.class);
                    Log.d("TAG", "String:  "+ longitude + " / " + latitude+"--"+uid);

                    if(detailAddress!=null&&latitude != null&& longitude != null) {
                    managerLatitude = Double.parseDouble(latitude);
                     managerLongitude = Double.parseDouble(longitude);
                        Log.d("TAG", "Double:  "+managerLatitude + " / " + managerLongitude+"--"+uid);
                        ManagerLocation.setLatitude(37.3275966);
                        ManagerLocation.setLongitude(126.8607009);

                        distance = userLocation.distanceTo(ManagerLocation);
                         d  = Float.toString(distance);

                       // listItem.setDistance(d);
                       // listItem.setDetailAddress(detailAddress);
                      //  listItem.setName(shopName);
                       // listItem.setUid(uid);
                        adapter.addItem(shopName, detailAddress, d, uid);

                      // if(distance < 4000) {
                        //   adapter.add("[" + shopName + "]" + detailAddress + "\ndistance:" + d);
                      //  }
                    }

            }
                shopListView.setAdapter(adapter);
            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

            });

    }
    @Override
    public void onListBtnClick(int position, int resourceid) {

        Manager selected =  list.get(position);
        String uid =  selected.getUid();

        switch(resourceid) {

            case R.id.gotoReserve:
                Intent intent = new Intent(getApplicationContext(), userReservationDetail.class);
                intent.putExtra("uid", uid);
                Log.d("TAG", "개빡쳐:  "+uid);
                startActivity(intent);
                break;

        }
        }

}