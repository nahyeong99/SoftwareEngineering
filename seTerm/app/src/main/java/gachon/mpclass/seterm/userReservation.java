package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class userReservation extends AppCompatActivity {
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
    FirebaseUser manager;
    private ListView shopListView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservation);
        firebaseAuth = FirebaseAuth.getInstance();
        shopListView = (ListView)findViewById(R.id.shopListView);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference managerRef = mReference.child("Managers");
        userLocation = new Location("userLocation");
        ManagerLocation = new Location("managerLocation");
        user = firebaseAuth.getCurrentUser();
      mReference.child("Customers").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));


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
                    String shopName = ds.child("name").getValue(String.class);
                    String detailAddress = ds.child("detailAdress").getValue(String.class);
                    String longitude = ds.child("longitude").getValue(String.class);
                    String latitude = ds.child("latitude").getValue(String.class);
                    Log.d("TAG", longitude + " / " + latitude);

                    if(managerLatitude != null&& managerLongitude != null) {
                   managerLatitude = Double.parseDouble(latitude);
                   managerLongitude = Double.parseDouble(longitude);
                        ManagerLocation.setLatitude(managerLatitude);
                        ManagerLocation.setLongitude(managerLongitude);

                        distance = userLocation.distanceTo(ManagerLocation);
                        String d  = Float.toString(distance);
                        adapter.add("[" + shopName + "]" + detailAddress + "\ndistance:" + d);
                    }
                    else{
                        adapter.add("[" + shopName + "]" + detailAddress + " distance:" + distance);
                    }
            }};

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

            });

            shopListView.setAdapter(adapter);



    }

}