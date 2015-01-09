package com.wordpress.priyankvex.sockets0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by priyank on 23/12/14.
 * Custom adapter for list view.
 */
public class CmdListAdapter extends ArrayAdapter<Map> {



    public CmdListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CmdListAdapter(Context context, int resource, List<Map> items) {
        super(context, resource, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_row, null);

        }

        Map<String, String> cmd = getItem(position);

        if (cmd != null) {
            TextView title = (TextView) v.findViewById(R.id.text_view_title);
            TextView command = (TextView) v.findViewById(R.id.text_view_cmd);

            title.setText(cmd.get("title"));
            command.setText(cmd.get("cmd"));
        }

        return v;

    }

}