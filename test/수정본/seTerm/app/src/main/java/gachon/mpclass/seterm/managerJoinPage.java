package gachon.mpclass.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class managerJoinPage extends AppCompatActivity {

    private FirebaseAuth uAuth;
    ProgressDialog progressDialog;
    private String[] timeSpinner;
    private String[] minSpinner;
    String op_time,op_min,cl_time,cl_min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_join_page);

        Button managerJoinPageBack = (Button) findViewById(R.id.joinInManagerBack);
        Button joinInmanager = (Button) findViewById(R.id.joinInManager);
        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
        timeSpinner = getResources().getStringArray(R.array.hour);
        minSpinner = getResources().getStringArray(R.array.min);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,timeSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                String item = (String)adapterView.getItemAtPosition(position);
                op_time = item;
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){
                op_time="";
            }
        });
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                String item = (String)adapterView.getItemAtPosition(position);
                cl_time = item;
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){
                cl_time="";
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,minSpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                String item = (String)adapterView.getItemAtPosition(position);
                op_min = item;
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){
                op_min="";
            }
        });

        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                String item = (String)adapterView.getItemAtPosition(position);
                cl_min = item;
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){
                cl_min="";
            }
        });

        progressDialog = new ProgressDialog(this);

        uAuth = FirebaseAuth.getInstance();

        managerJoinPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        joinInmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.joinInManager:
                        flowerShopInfo();
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


    private void flowerShopInfo() {
        String license = ((EditText) findViewById(R.id.license)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String time = op_time+":"+op_min+"~"+cl_time+":"+cl_min;

        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(license)) {
            Toast.makeText(this, "사업자 등록번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "주소를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "꽃집 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

//


        FirebaseUser user = uAuth.getCurrentUser();


        String email = user.getEmail();
        String uid = user.getUid();



        G.hashMap.put("license", license);
        G.hashMap.put("name",name);
        G.hashMap.put("address", address);
        G.hashMap.put("time", time);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Managers");
        reference.child(uid).setValue(G.hashMap);
        G.hashMap.clear();
        Toast.makeText(managerJoinPage.this, "회원가입 완료 - 로그인 하세요", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), startPage.class));
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