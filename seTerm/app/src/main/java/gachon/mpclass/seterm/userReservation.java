package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    List<String> Array = new ArrayList<String>();
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
                    String uid = ds.getKey();
                    String detailAddress = ds.child("detailAdress").getValue(String.class);
                    String longitude = ds.child("longitude").getValue(String.class);
                    String latitude = ds.child("latitude").getValue(String.class);
                    Log.d("TAG", longitude + " / " + latitude+"--"+uid);
                    Array.add(uid);
                    if(managerLatitude != null&& managerLongitude != null) {
                   managerLatitude = Double.parseDouble(latitude);
                   managerLongitude = Double.parseDouble(longitude);
                        ManagerLocation.setLatitude(managerLatitude);
                        ManagerLocation.setLongitude(managerLongitude);

                        distance = userLocation.distanceTo(ManagerLocation);
                        String d  = Float.toString(distance);
                       // Array.add()
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
         shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // 상세정보 화면으로 이동하기(인텐트 날리기)
                    // 1. 다음화면을 만든다
                    // 2. AndroidManifest.xml 에 화면을 등록한다
                    // 3. Intent 객체를 생성하여 날린다
                    Intent intent = new Intent(
                            getApplicationContext(), // 현재화면의 제어권자
                            userReservationDetail.class); // 다음넘어갈 화면

                    // intent 객체에 데이터를 실어서 보내기
                    // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                    intent.putExtra("uid", Array.get(position));


                    startActivity(intent);
                }
            });



    }

}