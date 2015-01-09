package com.wordpress.priyankvex.sockets0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private ListView list_view_cmds;
    private ImageView image_view_empty;

    DatabaseHandler mDatabaseHandler;
    SQLiteDatabase db;

    SocketConnection mSocketConnection;

    String ip_address;
    int port;

    List<Map> cmds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHandler = new DatabaseHandler(this);
        db = mDatabaseHandler.getWritableDatabase();

        list_view_cmds = (ListView)findViewById(R.id.listview_cmds);
        image_view_empty = (ImageView)findViewById(R.id.empty_image_view);

        mSocketConnection = new SocketConnection();

        cmds = new ArrayList<Map>();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        ip_address = preferences.getString("server_name", "localhost");
        String temp_port = preferences.getString("port_number", "5000");
        port = Integer.parseInt(temp_port);

        //Long click item to delete it from the database
        list_view_cmds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cmd = cmds.get(position);
                String command = cmd.get("cmd");
                mDatabaseHandler.deleteCommand(command);
                fillList();
                return false;
            }
        });

        //click item to execute the respective command on the server
        list_view_cmds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cmd = cmds.get(position);
                String command = cmd.get("cmd");
                new OpenConnection().execute(command);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onStart();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        ip_address = preferences.getString("server_name", "localhost");
        String temp_port = preferences.getString("port_number", "5000");
        port = Integer.parseInt(temp_port);
        //fill the list from DB by calling the utility function
        fillList();

        if( mDatabaseHandler.isEmptyTableFavourites(db)){
            image_view_empty.setVisibility(View.VISIBLE);
        }
        else{
            fillList();
            image_view_empty.setVisibility(View.INVISIBLE);
        }
    }

    //Utility function to fill the list with data from the database
    void fillList(){

        cmds = mDatabaseHandler.readCommands(db);

        CmdListAdapter adapter = new CmdListAdapter(MainActivity.this, R.layout.list_item_row, cmds);
        list_view_cmds.setAdapter(adapter);
    }


    //Check flag for connection
    int flag = -1;
    private class OpenConnection extends AsyncTask<String, String, Void>{
        @Override
        protected Void doInBackground(String... params) {
            String command = params[0];
            flag = mSocketConnection.client(ip_address, port, command);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(flag == -1){
                Toast.makeText(MainActivity.this, "Failed to connect to the server. Try changing settings or restart the server", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if( id == R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        else if( id == R.id.action_add){
            Intent i = new Intent(this, AddCommandActivity.class);
            startActivity(i);
        }
        else if (id == R.id.action_help){
            Intent i= new Intent(this, HelpActivity.class);
            startActivity(i);
        }

        return true;
    }
}
