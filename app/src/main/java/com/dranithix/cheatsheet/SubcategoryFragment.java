package com.dranithix.cheatsheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.ui.SubcategoryListAdapter;
import com.dranithix.cheatsheet.util.DebugUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubcategoryFragment extends Fragment {
    private String categoryId = null;

    @Bind(R.id.list)
    RecyclerView list;

    private List<Subcategory> subcategories = new ArrayList<Subcategory>();

    private SubcategoryListAdapter adapter;

    public static SubcategoryFragment newInstance(String categoryId) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle params = new Bundle();
        params.putString("id", categoryId);
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter = new SubcategoryListAdapter(subcategories));

        if (subcategories.size() == 0 && categoryId != null) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("Loading...");
            dialog.setMessage("Collecting topics...");
            dialog.show();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.whereEqualTo("parentId", categoryId);
            query.orderByAscending("name");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> cat, ParseException e) {
                    if (e == null) {
                        for (ParseObject obj : cat) {
                            Subcategory subcategory = new Subcategory();
                            subcategory.setId(obj.getString("objectId"));
                            subcategory.setName(obj.getString("name"));
                            subcategories.add(subcategory);
                        }
                        adapter.notifyItemRangeInserted(0, cat.size());
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
