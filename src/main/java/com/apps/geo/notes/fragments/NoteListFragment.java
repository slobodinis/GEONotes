package com.apps.geo.notes.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.adapters.NoteAdapter;

public class NoteListFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notes_fragment, container, false);
        fillNoteList((ListView) rootView.findViewById(R.id.note_list_view));
        return rootView;
    }

    private void fillNoteList(ListView view){
        view.setAdapter(new NoteAdapter(new PointInfoDBManager(getContext())));
    }



}
