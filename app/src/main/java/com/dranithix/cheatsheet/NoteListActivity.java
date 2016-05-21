package com.dranithix.cheatsheet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.ui.NoteListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customfonts.MyTextView;

public class NoteListActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView list;

    MyTextView toolbarTitle;

    NoteListAdapter adapter;

    List<Note> notes = new ArrayList<Note>();

    @OnClick(R.id.back_button)
    public void backButton() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_fragment);
        toolbarTitle = (MyTextView) findViewById(R.id.toolbar_title);

        ButterKnife.bind(this);

        Bundle params = getIntent().getExtras();
        Subcategory subcategory;
        if (params != null) {
            subcategory = params.getParcelable("subcategory");
        } else {
            finish();
            return;
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter = new NoteListAdapter(notes));

        if (notes.size() == 0) {
            toolbarTitle.setText(subcategory.getName());

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Loading...");
            dialog.setMessage("Fetching notes...");
            dialog.show();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
            query.orderByAscending("title");
            query.whereEqualTo("categoryId", subcategory.getId());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> cat, ParseException e) {
                    if (e == null) {
                        for (ParseObject obj : cat) {
                            Note note = new Note();
                            note.setId(obj.getObjectId());
                            note.setTitle(obj.getString("title"));
                            note.setData(obj.getString("data"));
                            note.setTags(obj.<String>getList("tags"));
                            note.setUploadedBy(obj.getParseObject("uploadedBy"));
                            System.out.println(note.getTitle());
                            notes.add(note);
                        }
                        adapter.notifyItemRangeInserted(0, cat.size());
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        }
    }
}
