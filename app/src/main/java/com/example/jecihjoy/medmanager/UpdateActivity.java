package com.example.jecihjoy.medmanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.adapters.DatabaseAdapter;

public class UpdateActivity extends AppCompatActivity {
    private EditText pname;
    private EditText gname;
    private EditText pgname;
    private  EditText email;
    private Button mUpdate;
    private String myId;

    DatabaseAdapter databaseAdapter;
    Cursor muUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        pname = (EditText) findViewById(R.id.edit_pname);
        gname = (EditText) findViewById(R.id.edit_givenName);
        pgname = (EditText) findViewById(R.id.edit_pgname);
        email = (EditText) findViewById(R.id.edit_email);
        mUpdate = (Button) findViewById(R.id.update);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
        muUser = databaseAdapter.getUserInfo();

        if (muUser.getCount() > 0) {
            if (muUser.moveToFirst()) {

                pname.setText(muUser.getString(1));
                gname.setText(muUser.getString(2));
                pgname.setText(muUser.getString(3));
                email.setText(muUser.getString(4));
                myId = muUser.getString(0);
            }
        }else {
            Toast.makeText(this, "You dont have any existing data", Toast.LENGTH_SHORT).show();

        }
            databaseAdapter.close();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseAdapter.open();

                databaseAdapter.updateProfile(myId, pname.getText().toString(),
                        gname.getText().toString(), pgname.getText().toString(),
                        email.getText().toString());

                databaseAdapter.close();

                Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();


            }
        });

    }
}
