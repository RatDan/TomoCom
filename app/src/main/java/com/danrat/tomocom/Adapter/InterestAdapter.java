package com.danrat.tomocom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.danrat.tomocom.Model.Interest;
import com.danrat.tomocom.View.InterestSelectorActivity;

import java.util.List;
import java.util.Map;


public class InterestAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<Interest> interestCategories;

    public InterestAdapter(Context context, List<Interest> interestCategories) {
        this.context = context;
        this.interestCategories = interestCategories;
    }

    @Override
    public int getGroupCount() {
        return interestCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return interestCategories.get(groupPosition).getSubcategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return interestCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return interestCategories.get(groupPosition).getSubcategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String categoryName = ((Interest) getGroup(groupPosition)).getCategory();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(categoryName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String category = interestCategories.get(groupPosition).getCategory();
        String subcategory = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_single_choice, parent, false);
        }

        CheckedTextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(subcategory);

        Map<String, String> selectedMap = ((InterestSelectorActivity) context).getInterestsViewModel().getSelectedCategoriesWithSubinterests().getValue();
        textView.setChecked(selectedMap != null && subcategory.equals(selectedMap.get(category)));

        textView.setOnClickListener(v -> {
            boolean isChecked = !textView.isChecked();
            ((InterestSelectorActivity) context).getInterestsViewModel().toggleInterest(category, subcategory, isChecked);
            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


