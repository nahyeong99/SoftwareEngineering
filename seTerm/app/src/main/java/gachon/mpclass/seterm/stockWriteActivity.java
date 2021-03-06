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
                startActivityForResult(Intent.createChooser(intent, "???????????? ???????????????."), 0);

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

                //????????????, ??????, ?????? ????????? ?????????
                uploadFile();
                if(isImg==true) {
                    finish();//?????? ???????????? ?????????????????? ?????????
                }

            }
        });

        mReference = mDatabase.getReference().child("Managers").child(uid).child("Flowers"); // ???????????? ????????? child ??????






    }
    //?????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request????????? 0?????? OK??? ???????????? data??? ????????? ?????? ?????????
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("stockWriteActivity", "uri:" + String.valueOf(filePath));
            try {
                //Uri ????????? Bitmap?????? ???????????? ImageView??? ?????? ?????????.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFile() {
        //???????????? ????????? ????????? ??????
        if (filePath != null&&name !=null&&color !=null&&num !=null) {
            //????????? ?????? Dialog ?????????
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("????????????...");
            progressDialog.show();
            isImg = true;
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique??? ???????????? ?????????.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage ????????? ?????? ???????????? ????????? ??????.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://seterm-d4ac0.appspot.com").child("flower/" + filename);
            G.fileName = filename;
            storageRef.putFile(filePath)
                    //?????????
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //????????? ???????????? ?????????????????????...
                            //????????? firebase storage??? ????????? ?????? ???????????? URL??? ????????????
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //??????????????? firebase??? ???????????? ???????????? ??????
                                    //???????????? ?????? ???????????? ??????(URL)??? ???????????? ????????????
                                    G.imgUrl = uri.toString();

                                    Toast.makeText(stockWriteActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();

                                    //firebase DB????????? ?????? ??????
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                    //????????? ?????????
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
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            isImg = false;
            if(filePath==null||name==null||color==null||num==null){Toast.makeText(getApplicationContext(), "????????? ?????? ??????, ??????, ????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();}
//            else if(filePath==null){Toast.makeText(getApplicationContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show();}
//            else if(name !=null){Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();}
//            else if(color !=null){Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();}
//            else if(num !=null){Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();}
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

    //????????? ??????????????? ?????????
    @Override
    protected  void onResume(){
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}