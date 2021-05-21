package gachon.mpclass.seterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class userInfo extends AppCompatActivity {
TextView uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Button editInfoBtn = findViewById(R.id.editUserInfoBtn);
        Button reservationBtn = findViewById(R.id.reservation);
        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),userEditInfo.class);
                startActivity(intent);
            }
        });

        Button userLocation =(Button)findViewById(R.id.setLocation);

        userLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(getApplicationContext(),userLocation.class);
                startActivity(intent);
            }
        });
        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),userReservation.class);
                startActivity(intent);
            }
        });

    }
}