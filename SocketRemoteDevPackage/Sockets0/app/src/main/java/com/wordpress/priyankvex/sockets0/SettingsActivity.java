package com.wordpress.priyankvex.sockets0;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by priyank on 7/1/15.
 * App to let the user input server number and port number
 */
public class SettingsActivity extends Activity{

    //View widgets
    EditText fieldServerName;
    EditText fieldPortNumber;
    Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //initializing view widgets
        fieldServerName =(EditText)findViewById(R.id.field_server_address);
        fieldPortNumber = (EditText)findViewById(R.id.field_port_number);
        btn_set = (Button)findViewById(R.id.btn_set);

        //Saving the settings in shared preferences
        SharedPreferences preferences = this.getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server_name = fieldServerName.getText().toString();
                String port_number = fieldPortNumber.getText().toString();

                if( !TextUtils.isEmpty(server_name) && !TextUtils.isEmpty(port_number) ){
                    editor.putString("server_name", server_name);
                    editor.putString("port_number", port_number);
                    editor.commit();
                    finish();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "Fill all the field", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Pre-populating the settings fields with data if available
        String ip_address = preferences.getString("server_name", "localhost");
        String port_number = preferences.getString("port_number", "5000");
        fieldPortNumber.setText(port_number);
        fieldServerName.setText(ip_address);

    }

}
