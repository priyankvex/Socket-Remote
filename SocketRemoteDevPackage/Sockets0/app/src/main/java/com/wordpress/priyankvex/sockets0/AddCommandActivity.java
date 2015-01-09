package com.wordpress.priyankvex.sockets0;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by priyank on 8/1/15.
 * Activity to let the user add commands to execute via the list provided.
 */
public class AddCommandActivity extends ActionBarActivity {

    EditText field_title;
    EditText field_cmd;
    Button btn_add;

    Bundle b;

    DatabaseHandler mDatabaseHandler;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_command);

        mDatabaseHandler = new DatabaseHandler(this);
        db = mDatabaseHandler.getWritableDatabase();

        //initializing the view widgets
        field_title = (EditText)findViewById(R.id.field_title);
        field_cmd = (EditText)findViewById(R.id.field_command);
        btn_add = (Button)findViewById(R.id.btn_add_command);

        //Bundle to wrap the title and command together
        b = new Bundle();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = field_title.getText().toString();
                String cmd = field_cmd.getText().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(cmd)){
                    b.putString("title", title);
                    b.putString("cmd", cmd);
                    mDatabaseHandler.addCommand(db, b);
                    Toast.makeText(AddCommandActivity.this, "Command added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(AddCommandActivity.this, "Fill all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
