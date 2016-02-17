package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Jeremiah on 2/17/16.
 */
public class BuildingListAdapter extends BaseAdapter {

    public Context ctx;

    public BuildingListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public class ViewHolder {
        public TextView name;
        public TextView expanded;
    }

    @Override
    public int getCount() {
        return BoilerCheck.loadedBuildings.Buildings.length;
    }

    @Override
    public Object getItem(int position) {
        return BoilerCheck.loadedBuildings.Buildings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.building_list_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.expanded = (TextView) view.findViewById(R.id.hidden);
            final ViewHolder final_holder = holder;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (final_holder.expanded.getVisibility() == View.GONE) {
                        final_holder.expanded.setVisibility(View.VISIBLE);
                    } else {
                        final_holder.expanded.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        holder.name.setText(BoilerCheck.loadedBuildings.Buildings[position].BuildingName);
        view.setTag(holder);
        return view;
    }
}
