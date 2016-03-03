package com.cs408.team13.BoilerCheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jeremiah on 2/17/16.
 */
public class BuildingListAdapter extends BaseAdapter {

    public Context ctx;
    public int filt;
    public ArrayList<Building> build;

    public BuildingListAdapter(Context ctx, int filt) {
        this.ctx = ctx;
        this.filt = filt;
        this.build = new ArrayList<Building>();
        for (Building a : BoilerCheck.loadedBuildings.Buildings) {
            if (a.Category.equals(ctx.getString(filt)))
                this.build.add(a);
        }
    }

    public class ViewHolder {
        public TextView name;
        public LinearLayout expanded;
        public TextView currCap;
        public TextView totCap;
        public Button checkIn;
    }

    @Override
    public int getCount() {
        return build.size();
    }

    @Override
    public Object getItem(int position) {
        return build.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder holder;
        if (convertView == null) {
            view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.building_list_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.expanded = (LinearLayout) view.findViewById(R.id.stuff);
            holder.currCap = (TextView) view.findViewById(R.id.curr_cap);
            holder.totCap = (TextView) view.findViewById(R.id.tot_cap);
            holder.checkIn = (Button) view.findViewById(R.id.check_in);
            final ViewHolder final_holder = holder;
            final int final_position = position;
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
            holder.checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(view.getContext(), build.get(final_position).BuildingName, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        holder.name.setText(build.get(position).BuildingName);
        holder.currCap.setText(Integer.toString(build.get(position).CurrentCapacity));
        holder.totCap.setText(Integer.toString(build.get(position).TotalCapacity));
        view.setTag(holder);
        return view;
    }
}
