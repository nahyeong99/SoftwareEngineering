package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userEditInfo extends AppCompatActivity {

    String newName;
    String newNumber;
    String UserID, UserName,UserNumber;
String nickname;
    EditText editName;
    EditText editNumber;
    TextView viewID;
    TextView viewName;
    TextView viewNumber;
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_info);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editName = findViewById(R.id.editUserName);
        editNumber = findViewById(R.id.editUserNumber);
        viewID = findViewById(R.id.viewUserEmail);
        viewName = findViewById(R.id.viewUserName);
        viewNumber = findViewById(R.id.viewUserNumber);

        user=firebaseAuth.getCurrentUser();
        nickname = user.getUid();
        mDatabase.child("Customers").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    String email = user.getEmail();
                    String uid = user.getUid();
                    viewID.setText(email);
                    //UserPW = task.getResult().child("pwd").getValue().toString();
                    //viewPW.setText(UserPW);
                    UserName = task.getResult().child("name").getValue().toString();
                    viewName.setText(UserName);
                    UserNumber = mDatabase.child("Users").child("phonenumber").get().toString();
                    viewNumber.setText(UserNumber);

                }
            }
        });


        Button backButton = findViewById(R.id.editInfoManagerBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button editInfoButton = findViewById(R.id.editInfoManager);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
            }
        });
    }

    private void editInfo() {
        //edittext에서 value 받아와서 정보 업데이트 해주기
        user=firebaseAuth.getCurrentUser();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Customers");
        newName = editName.getText().toString();
        if(TextUtils.isEmpty(newName)){
            G.hashMap.put("name", UserName);
        }
        else{
            G.hashMap.put("name", newName);
        }
        newNumber = editNumber.getText().toString();
        if(TextUtils.isEmpty(newNumber)) {
            G.hashMap.put("phonenumber", UserNumber);
        }
        else{
            G.hashMap.put("phonenumber", newNumber);
        }
        G.hashMap.put("nickname",nickname);
        G.hashMap.put("email",UserID);
        reference.child(user.getUid()).setValue(G.hashMap);
        G.hashMap.clear();
        Toast.makeText(this, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
    }
}