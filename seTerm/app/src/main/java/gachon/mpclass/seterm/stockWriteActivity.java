package gachon.mpclass.seterm;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class stockWriteActivity extends AppCompatActivity {
    private Button sendbt;
    private EditText flowername;
    private EditText flowercolor;
    private EditText flowernumber;
    private ImageButton imagebt;
    public String name;
    public String color;
    public String img;
    public String uid;
    public String num;
    public String userid;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private DatabaseReference mRef;
    private ListView listView;
    List<Object> Array = new ArrayList<Object>();
    private ImageView ivPreview;
    private Uri filePath;
    private boolean isImg = true;
    int share=0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int[] cnt = {0};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_write);
        flowername = (EditText)findViewById(R.id.flowername);
        flowercolor = (EditText)findViewById(R.id.flowercolor);
        flowernumber = (EditText)findViewById(R.id.flowernumber);
        sendbt = (Button) findViewById(R.id.upload);
        imagebt = (ImageButton) findViewById(R.id.imageUploadButton);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        uid = user.getUid();

        initDatabase();

        imagebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });

        sendbt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name = flowername.getText().toString();
                if(name==null) {
                    name = "name";
                }
                color = flowercolor.getText().toString();
                if(color==null) {
                    color = "color";
                }
                num = flowernumber.getText().toString();
                if(num==null) {
                    num = "num";
                }
                img = G.imgUrl;
                if(img==null){
                    img="imgUrl";
                }
                uid = user.getUid();
                if(uid==null){
                    uid="";
                }

                //사진주소, 제목, 내용 한번에 업로드
                uploadFile();
                if(isImg==true) {
                    finish();//글을 등록하면 메인화면으로 돌아감
                }

            }
        });

        mReference = mDatabase.getReference().child("Managers").child(uid).child("Flowers"); // 변경값을 확인할 child 이름






    }
    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("stockWriteActivity", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null&&name !=null&&color !=null&&num !=null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            isImg = true;
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://seterm-d4ac0.appspot.com").child("flower/" + filename);
            G.fileName = filename;
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //이미지 업로드가 성공되었으므로...
                            //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //파라미터로 firebase의 저장소에 저장되어 있는
                                    //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
                                    G.imgUrl = uri.toString();

                                    Toast.makeText(stockWriteActivity.this, "업로드 완료", Toast.LENGTH_SHORT).show();

                                    //firebase DB관리자 객체 소환
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                    //여기서 업로드
                                    FlowerListViewItem list=new FlowerListViewItem(name,color,G.imgUrl,num,uid,G.fileName);
                                    databaseReference.child("Managers").child(uid).child("Flowers").push().setValue(list);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            isImg = false;
            if(filePath==null||name==null||color==null||num==null){Toast.makeText(getApplicationContext(), "사진과 꽃의 종류, 색상, 수량을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();}
//            else if(filePath==null){Toast.makeText(getApplicationContext(), "이미지를 선택하세요.", Toast.LENGTH_SHORT).show();}
//            else if(name !=null){Toast.makeText(getApplicationContext(), "꽃의 종류를 입력해주세요.", Toast.LENGTH_SHORT).show();}
//            else if(color !=null){Toast.makeText(getApplicationContext(), "꽃의 색상을 입력해주세요.", Toast.LENGTH_SHORT).show();}
//            else if(num !=null){Toast.makeText(getApplicationContext(), "꽃의 수량을 입력해주세요.", Toast.LENGTH_SHORT).show();}
        }

        //saveData() ..
    }

    private void initDatabase() {

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
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

    //키보드 안올라오게 하는거
    @Override
    protected  void onResume(){
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}