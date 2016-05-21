package com.dranithix.cheatsheet.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dranithix.cheatsheet.R;
import com.dranithix.cheatsheet.entities.Subcategory;
import com.dranithix.cheatsheet.events.OnSubcategoryClickEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SubcategoryListAdapter extends RecyclerView.Adapter<SubcategoryListAdapter.ViewHolder> {

    private List<Subcategory> mValues;

    public SubcategoryListAdapter(List<Subcategory> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.subcategory = mValues.get(position);
        holder.name.setText(mValues.get(position).getName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnSubcategoryClickEvent(holder.subcategory));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Subcategory subcategory;
        public final View view;
        public final TextView name;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
