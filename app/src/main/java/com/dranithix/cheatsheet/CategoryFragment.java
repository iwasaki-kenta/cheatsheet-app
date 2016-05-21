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
import android.widget.TextView;

import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.ui.CategoryListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment {
    private CategoryListAdapter adapter;
    private List<Category> categories = new ArrayList<Category>();

    @Bind(R.id.list)
    RecyclerView list;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter = new CategoryListAdapter(categories));

        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notes");

        if (categories.size() == 0) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("Loading...");
            dialog.setMessage("Finding subjects online...");
            dialog.show();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.orderByAscending("name");
            query.whereEqualTo("parentId", "-1");

            final String[] imageUrls = new String[]{"http://static1.squarespace.com/static/50c2031de4b02a7395e3e36f/t/542db21ee4b0d845f517528a/1412280869359/Biology+Header.jpg?format=1500w", "http://www2.hull.ac.uk/pgmi2/images/english.png", "http://www.ncl.ac.uk/media/wwwnclacuk/undergraduate/images/courseprofiles/English-Literature-with-Creative-Writing-BA-QW38-crop.jpg", "https://carleton.ca/geography/wp-content/uploads/Geography-Banner.jpg", "http://www.lawrence.edu/mfhe/styles/original/mfhe/www_core_communications/Everyone/mathematics.jpg", "http://sbasse.devlums.com/sites/default/files/programs/MS%20physics.jpg"};

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> cat, ParseException e) {
                    if (e == null) {
                        int iterate = 0;
                        for (ParseObject obj : cat) {
                            Category category = new Category();
                            category.setId(obj.getObjectId());
                            category.setName(obj.getString("name"));
                            category.setImagePath(imageUrls[iterate++]);
                            category.setCount(obj.getInt("count"));
                            System.out.println(category.getName());
                            categories.add(category);
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
