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

        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "전화번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number.length()<7) { //최소 7자리
            Toast.makeText(this, "전화번호를 정확하게 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(checkPassword)) {
            Toast.makeText(userJoinPage.this, "비밀번호 불일치", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(userJoinPage.this, "비밀번호 최소 6자리 이상", Toast.LENGTH_SHORT).show();
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
                    if(check.isChecked()) // 사장님으로 가입하는경우
                    {
                        Toast.makeText(userJoinPage.this, "꽃집 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), managerJoinPage.class));}
                    else { //손님으로 가입하는경우
                        DatabaseReference reference = database.getReference("Customers");
                        reference.child(uid).setValue(G.hashMap);
                        G.hashMap.clear();
                        Toast.makeText(userJoinPage.this, "회원가입 완료 - 로그인 하세요", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), startPage.class));
                    }

                } else {
                    //에러발생
                    //textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                    Toast.makeText(userJoinPage.this, "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    //리스너에서는 토스트 사용안돼서 함수만들어서 썼어욤
    private void startToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //키보드 안올라오게 하는거
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}