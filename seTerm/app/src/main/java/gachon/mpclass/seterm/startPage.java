package gachon.mpclass.seterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class startPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        Button managerJoinButton=(Button)findViewById(R.id.managerJoin);
        Button userJoinButton=(Button)findViewById(R.id.userJoin);

        managerJoinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),managerJoinPage.class);
                startActivity(intent);
            }
        });

        userJoinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),userJoinPage.class);
                startActivity(intent);
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