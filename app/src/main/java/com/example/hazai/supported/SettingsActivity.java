package com.example.hazai.supported;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SettingsActivity extends Activity implements View.OnClickListener {

    EditText contact1;
//    EditText contact2;
//    EditText contact3;
    public static String filename = "MySharedString";
    SharedPreferences someData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupVariables();
        someData = getSharedPreferences(filename, 0);
        Toast.makeText(getApplicationContext(), contact1.getText().toString(),
                Toast.LENGTH_SHORT).show();
    }

    private void setupVariables() {
        Button save = (Button) findViewById(R.id.save);
        contact1 = (EditText) findViewById(R.id.phone1);
//        contact2 = (EditText) findViewById(R.id.phone2);
//        contact3 = (EditText) findViewById(R.id.phone3);

        save.setOnClickListener(this);
    }


    public void onClick(View v) {
        if(v.getId()==R.id.save) {
            String stringData1 = contact1.getText().toString();
            SharedPreferences.Editor editor = someData.edit();
            editor.putString("sharedString", stringData1);
            editor.commit();
            Toast.makeText(getApplicationContext(), stringData1,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
