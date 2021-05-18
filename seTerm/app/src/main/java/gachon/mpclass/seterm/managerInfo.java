package gachon.mpclass.seterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class managerInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_info);
        Button checkReserve = findViewById(R.id.checkReserve);
        Button Reservation = findViewById(R.id.reservation);
        Button editInfo = findViewById(R.id.editInfo);
        checkReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),checkInfo.class);
                startActivity(intent);
            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),managerEditInfo.class);
                startActivity(intent);
            }
        });
    }


}