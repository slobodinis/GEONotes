package com.apps.geo.notes.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.adapters.NoteAdapter;
import com.apps.geo.notes.fragments.adapters.NoteSelectAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class NoteListFragment extends Fragment{
    private ListView mNoteListView;
    private PointInfoDBManager mDBManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notes_fragment, container, false);
        mNoteListView = (ListView) rootView.findViewById(R.id.note_list_view);
        mDBManager = new PointInfoDBManager(getContext());
        switchToBaseForm();
        return rootView;
    }

    public void switchToBaseForm(){
        mNoteListView.setAdapter(new NoteAdapter(mDBManager) {
            @Override
            public void onSwitchForm() {
                switchToSelectForm();
            }
        });
        mNoteListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
    }

    public void switchToSelectForm(){
        mNoteListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        NoteSelectAdapter adapter = new NoteSelectAdapter(mDBManager) {
            @Override
            public void onSelect(int idx, boolean value) {
                mNoteListView.setItemChecked(idx, value);
            }
        };
        mNoteListView.setAdapter(adapter);

    }


    public void removeSelected(NoteSelectAdapter adapter){
        for(Integer id: adapter.getIdsForDeletion(mNoteListView.getCheckedItemPositions())){
            if (id != null){
                mDBManager.deletePointById(id);
            }
        }
        switchToBaseForm();
    }


}
