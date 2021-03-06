package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class userJoinPage extends AppCompatActivity {

    private FirebaseAuth uAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_join_page);

        Button userJoinPageBack = (Button) findViewById(R.id.joinInUserBack);
        Button joinInuser = (Button) findViewById(R.id.joinInUser);
        progressDialog = new ProgressDialog(this);

        uAuth = FirebaseAuth.getInstance();

        userJoinPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        joinInuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.joinInUser:
                        signUp();
                        break;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = uAuth.getCurrentUser();
    }


    private void signUp() {
        String email = ((EditText) findViewById(R.id.uID)).getText().toString();
        String password = ((EditText) findViewById(R.id.uPW)).getText().toString();
        String checkPassword = ((EditText) findViewById(R.id.uPWC)).getText().toString();
        String name = ((EditText) findViewById(R.id.uName)).getText().toString();
        String number = ((EditText) findViewById(R.id.uNumber)).getText().toString();
        CheckBox check = (CheckBox)findViewById(R.id.check);

        //email??? password??? ???????????? ???????????? ?????? ??????.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number.length()<7) { //?????? 7??????
            Toast.makeText(this, "??????????????? ???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(checkPassword)) {
            Toast.makeText(userJoinPage.this, "???????????? ?????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(userJoinPage.this, "???????????? ?????? 6?????? ??????", Toast.LENGTH_SHORT).show();
            return;
        }
//


        uAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(userJoinPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = uAuth.getCurrentUser();


                    String email = user.getEmail();
                    String uid = user.getUid();

                    G.hashMap.put("nickname", uid);
                    G.hashMap.put("email", email);
                    G.hashMap.put("name", name);
                    G.hashMap.put("phonenumber", number);


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    if(check.isChecked()) // ??????????????? ??????????????????
                    {
                        Toast.makeText(userJoinPage.this, "?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), managerJoinPage.class));}
                    else { //???????????? ??????????????????
                        DatabaseReference reference = database.getReference("Customers");
                        reference.child(uid).setValue(G.hashMap);
                        G.hashMap.clear();
                        Toast.makeText(userJoinPage.this, "???????????? ?????? - ????????? ?????????", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), startPage.class));
                    }

                } else {
                    //????????????
                    //textviewMessage.setText("????????????\n - ?????? ????????? ?????????  \n -?????? ?????? 6?????? ?????? \n - ????????????");
                    Toast.makeText(userJoinPage.this, "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    //?????????????????? ????????? ??????????????? ?????????????????? ?????????
    private void startToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //????????? ??????????????? ?????????
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}