package com.apps.geo.notes.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;

import java.util.ArrayList;

public abstract class NoteAdapter extends BaseAdapter implements Switchable {

    private PointInfoDBManager manager;
    private ArrayList<PointInfo> mNotes;
    private Context mContext;

    static class ViewHolder {
        TextView mainText;
        TextView description;
        SwitchCompat activeSwitch;
        ImageButton geoButton;
        ImageButton editButton;
    }

    public NoteAdapter(PointInfoDBManager manager) {
        this.manager = manager;
        mContext = manager.getContext();
        update();
    }

    public void update() {
        mNotes = manager.getAllPoints();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.note_list_element, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mainText = (TextView) view.findViewById(R.id.note_caption);
            viewHolder.description = (TextView) view.findViewById(R.id.note_description);
            viewHolder.activeSwitch = (SwitchCompat) view.findViewById(R.id.note_switch);
            viewHolder.geoButton = (ImageButton) view.findViewById(R.id.note_location_button);
            viewHolder.editButton = (ImageButton) view.findViewById(R.id.note_edit_button);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        TODO
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditPointInfo(mNotes.get(i));
            }
        });
        viewHolder.geoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoveToLocation(mNotes.get(i));
            }
        });
        viewHolder.mainText.setText(mNotes.get(i).getName());
        viewHolder.description.setText(mNotes.get(i).getDescription());
        viewHolder.activeSwitch.setChecked(mNotes.get(i).isActive());
        Log.wtf("fasdf",mNotes.get(i).isActive()+"");
        View.OnClickListener textListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowVerbose(mNotes.get(i));
            }
        };
        viewHolder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNotes.get(i).setActive(isChecked);
                manager.updatePointById(mNotes.get(i));
                onUpdateMap();
            }
        });

        viewHolder.description.setOnClickListener(textListener);
//        viewHolder.mainText.setOnClickListener(textListener);
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onSwitchForm();
                return true;
            }
        };

        view.setOnLongClickListener(longClickListener);
        viewHolder.description.setOnLongClickListener(longClickListener);
        return view;
    }

    protected abstract void onMoveToLocation(PointInfo info);
    protected abstract void onShowVerbose(PointInfo info);
    protected abstract void onEditPointInfo(PointInfo info);
    protected abstract void onUpdateMap();
}
