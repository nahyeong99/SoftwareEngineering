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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class managerEditInfo extends AppCompatActivity {
    String newTime;
    String newName;
    String newAddress;
    String newLicense;
    String newNumber;
    String managerID, managerLicense,managerAddress,managerName,managerNumber,managerTime;
    EditText editID;
    EditText editLicense;
    EditText editAddress;
    EditText editName;
    EditText editNumber;
    TextView viewID;
    TextView viewLicense;
    TextView viewAddress;
    TextView viewName;
    TextView viewNumber;
    TextView viewTime;
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String[] timeSpinner;
    private String[] minSpinner;
    String op_time,op_min,cl_time,cl_min;
    FirebaseUser user;
    Spinner edit_spinner1,edit_spinner2, edit_spinner3,edit_spinner4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edit_info);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        editLicense = findViewById(R.id.editLicense);
        editAddress = findViewById(R.id.editAddress);
        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);
        viewID = findViewById(R.id.viewID);
        viewLicense = findViewById(R.id.viewLicense);
        viewAddress = findViewById(R.id.viewAddress);
        viewName = findViewById(R.id.viewName);
        viewNumber = findViewById(R.id.viewNumber);
        viewTime = findViewById(R.id.viewTime);
         edit_spinner1 = (Spinner)findViewById(R.id.edit_spinner1);
         edit_spinner2 = (Spinner)findViewById(R.id.edit_spinner2);
         edit_spinner3 = (Spinner)findViewById(R.id.edit_spinner3);
         edit_spinner4 = (Spinner)findViewById(R.id.edit_spinner4);
        timeSpinner = getResources().getStringArray(R.array.hour);
        minSpinner = getResources().getStringArray(R.array.min);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,timeSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner1.setAdapter(adapter);
        edit_spinner3.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,minSpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner2.setAdapter(adapter2);
        edit_spinner4.setAdapter(adapter2);

        edit_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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
        edit_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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


        edit_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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


        edit_spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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




        user=firebaseAuth.getCurrentUser();
        mDatabase.child("Managers").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    managerID = task.getResult().child("email").getValue().toString();
                    viewID.setText(managerID);
                    managerLicense = task.getResult().child("license").getValue().toString();
                    viewLicense.setText(managerLicense);
                    managerAddress = task.getResult().child("address").getValue().toString();
                    viewAddress.setText(managerAddress);
                     managerName = task.getResult().child("name").getValue().toString();
                    viewName.setText(managerName);
                     managerNumber = task.getResult().child("phonenumber").getValue().toString();
                    viewNumber.setText(managerNumber);
                     managerTime = task.getResult().child("time").getValue().toString();
                    viewTime.setText(managerTime);
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
        DatabaseReference reference = database.getReference("Managers");
       newName = editName.getText().toString();
       if(TextUtils.isEmpty(newName)){
           G.hashMap.put("name", managerName);
       }
       else{
           G.hashMap.put("name", newName);
       }
        newNumber = editNumber.getText().toString();
       if(TextUtils.isEmpty(newNumber)) {
           G.hashMap.put("phonenumber", managerNumber);
       }
       else{
           G.hashMap.put("phonenumber", newNumber);
       }
       newLicense = editLicense.getText().toString();
        if(TextUtils.isEmpty(newLicense)) {
            G.hashMap.put("license", managerLicense);
        }
        else{
            G.hashMap.put("license", newLicense);
        }
         newAddress = editAddress.getText().toString();
        if(TextUtils.isEmpty(newAddress)) {
            G.hashMap.put("address", managerAddress);
        }
        else{
            G.hashMap.put("address", newAddress);
        }

        newTime = op_time+":"+op_min+"~"+cl_time+":"+cl_min;


        G.hashMap.put("email",managerID);
        G.hashMap.put("time", newTime);
        reference.child(user.getUid()).setValue(G.hashMap);
        G.hashMap.clear();
        Toast.makeText(this, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
    }

}
