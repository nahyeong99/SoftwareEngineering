package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class startPage extends AppCompatActivity {

    Button userJoinButton;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignin;
    FirebaseAuth firebaseAuth;
    private String[] key;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        userJoinButton=(Button)findViewById(R.id.userJoin);
        editTextEmail = (EditText) findViewById(R.id.loginID);
        editTextPassword = (EditText) findViewById(R.id.loginPW);
        buttonSignin = (Button) findViewById(R.id.login);

        userJoinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(),userJoinPage.class);
                startActivity(intent);
            }
        });

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }
    //firebase userLogin method
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }



        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        if(task.isSuccessful()) {

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Managers").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                        Toast.makeText(startPage.this, "Error", Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        if(String.valueOf(task.getResult().getValue()).contains("license")) //사업자 번호가 있으면 사장님
                                        {
                                            Intent intent = new Intent(getApplicationContext(),managerInfo.class);
                                            DatabaseReference mReference;
                                            FirebaseDatabase mDatabase;
                                            mDatabase = FirebaseDatabase.getInstance();
                                            mReference = mDatabase.getReference("Managers");

                                            String uid = user.getUid();
                                            intent.putExtra("uid",uid);
                                            startActivity(intent);
                                        }
                                        else
                                        {

                                            Intent intent = new Intent(getApplicationContext(),userInfo.class);
                                            DatabaseReference mReference;
                                            FirebaseDatabase mDatabase;
                                            mDatabase = FirebaseDatabase.getInstance();
                                            mReference = mDatabase.getReference("Customers");

                                            String uid = user.getUid();
                                            intent.putExtra("uid",uid);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });





                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                            //textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
                    }
                });
    }
    //키보드 안올라오게 하는거
    @Override
    protected  void onResume(){
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}