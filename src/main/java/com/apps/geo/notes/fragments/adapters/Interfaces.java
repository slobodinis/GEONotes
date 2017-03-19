package com.apps.geo.notes.fragments.adapters;


interface Switchable {
    void onSwitchForm();
}

interface Selectable{
    void onSelect(int idx, boolean value);
}