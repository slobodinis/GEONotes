package com.apps.geo.notes.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.adapters.NoteAdapter;
import com.apps.geo.notes.fragments.adapters.NoteSelectAdapter;

public class NoteListFragment extends Fragment{
    private ListView mNoteListView;
    private FloatingActionButton mFAButton;
    private PointInfoDBManager mDBManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notes_fragment, container, false);
        mNoteListView = (ListView) rootView.findViewById(R.id.note_list_view);
        mFAButton = (FloatingActionButton) rootView.findViewById(R.id.fa_button);
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
        makeAdditionButton();
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
        makeDeletionButton(adapter);
    }


    public void removeSelected(NoteSelectAdapter adapter){
        for(Integer id: adapter.getIdsForDeletion(mNoteListView.getCheckedItemPositions())){
            if (id != null){
                mDBManager.deletePointById(id);
            }
        }
        switchToBaseForm();
    }

    private void makeAdditionButton(){
        mFAButton.setImageResource(R.drawable.ic_add);
//        mFAButton.setOnClickListener();
    }

    private void makeDeletionButton(final NoteSelectAdapter adapter){
        mFAButton.setImageResource(R.drawable.ic_delete);
        mFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelected(adapter);
            }
        });
    }
}
