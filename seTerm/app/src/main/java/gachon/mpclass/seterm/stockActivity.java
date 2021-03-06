package gachon.mpclass.seterm;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class stockActivity extends AppCompatActivity {
    public String context;
    private FloatingActionButton sendbt;
    private TextView userID;
    public String uid;
    int cnt=0;
    int count =0;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private ListView listView;
    private FirebaseAuth firebaseAuth;

    ArrayList<FlowerListViewItem> item=new ArrayList<FlowerListViewItem>();
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        //????????? ???????????? ???????????? ??????
        sendbt = (FloatingActionButton) findViewById(R.id.write);

        //????????? ????????? ????????? ??? ?????? ???????????? ?????????

        sendbt.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), stockWriteActivity.class);
            startActivityForResult(intent,1);
        });

        uid = user.getUid();
        listView = (ListView) findViewById(R.id.listview1);
        initDatabase();

        FlowerListView_Adapter adapter=new FlowerListView_Adapter(item);
        listView.setAdapter(adapter);

        mReference=mDatabase.getReference().child("Managers").child(uid).child("Flowers");
        //????????? ?????? ??????,,
        mReference.addChildEventListener(new ChildEventListener() {
            //?????? ????????? ?????? ??? ValueListener??? ????????? ?????? ???????????? ???????????? ?????? ?????? ???
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //?????? ????????? ?????????(??? : MessageItem??????) ????????????
                FlowerListViewItem listViewItem= dataSnapshot.getValue(FlowerListViewItem.class);

                //????????? ???????????? ???????????? ???????????? ?????? ArrayList??? ??????
                item.add(listViewItem);
                G.keyList.add(dataSnapshot.getKey());
                int index = G.keyList.indexOf(dataSnapshot.getKey());

                //??????????????? ??????
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1); //??????????????? ????????? ????????? ????????? ?????? ??????
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = G.keyList.indexOf(dataSnapshot.getKey());
                item.remove(index);
                G.keyList.remove(index);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void initDatabase () {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

}
