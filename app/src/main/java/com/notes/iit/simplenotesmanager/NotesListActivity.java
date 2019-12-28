package com.notes.iit.simplenotesmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class NotesListActivity extends AppCompatActivity {
    FloatingActionButton noteEditOpenButton;
    ListView listView;
    SqliteHelper sqliteHelper;
    CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initalizeViews();
        noteEditOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesListActivity.this, ListEditActivity.class);
                startActivity(intent);
            }
        });
        sqliteHelper = new SqliteHelper(this);
        Cursor cursor = sqliteHelper.retriveAllNotesCursor();
        cursorAdapter = new NotesListAdapter(this, cursor);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                delete(id + "");
                return false;
            }
        });
    }

    private void delete(final String id) {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to delete all the notes?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        int delete = sqliteHelper.delete(id);
                        Log.d("Clicked", delete + "");
                        cursorAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void initalizeViews() {
        noteEditOpenButton = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteAll:
                deleteAll();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to delete all the notes?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        sqliteHelper.deleteAll();
                        cursorAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}
