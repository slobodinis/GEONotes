package com.apps.geo.notes.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private ArrayList<PointInfo> mNotes;
    private Context mContext;

    static class ViewHolder {
        TextView mainText;
        TextView description;
        ImageButton geoButton;
        ImageButton editButton;
    }

    public NoteAdapter(PointInfoDBManager manager) {
        // TODO disable demo
        mNotes = new ArrayList<>();
        mNotes.add(new PointInfo("Some text", "acabac", 0,0,0));
        mNotes.add(new PointInfo("I need more text", "acabac again", 0,0,0));
//        mNotes = manager.getAllPoints();
        mContext = manager.getContext();
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return mNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.note_list_element, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mainText = (TextView) view.findViewById(R.id.note_caption);
            viewHolder.description = (TextView) view.findViewById(R.id.note_description);
            viewHolder.geoButton = (ImageButton) view.findViewById(R.id.note_location_button);
            viewHolder.editButton = (ImageButton) view.findViewById(R.id.note_edit_button);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        TODO
//        viewHolder.editButton.setOnClickListener();
//        viewHolder.geoButton.setOnClickListener();
        viewHolder.mainText.setText(mNotes.get(i).getName());
        viewHolder.description.setText(mNotes.get(i).getDescription());
        return view;
    }

}
