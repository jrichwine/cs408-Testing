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
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremiah on 2/17/16.
 */
public class BuildingListAdapter extends BaseAdapter {

    public Context ctx;
    public int filt;
    public ArrayList<Building> build;

    private CheckInTask mCheckInTask = null;

    private CheckOutTask mCheckOutTask = null;
    private RefreshCapacityTask mRefreshCapacityTask = null;

    public BuildingListAdapter(Context ctx, int filt) {
        this.ctx = ctx;
        this.filt = filt;
        this.build = new ArrayList<Building>();
        for (Building a : BoilerCheck.loadedBuildings.Buildings) {
            if (a.Category.equals(ctx.getString(filt)))
                this.build.add(a);
        }

        BoilerCheck.locationService = new LocationService(BoilerCheck.me);
        BoilerCheck.locationService.connect();
    }

    public class ViewHolder {
        public TextView name;
        public LinearLayout expanded;
        public TextView distance;
        public TextView currCap;
        public TextView totCap;
        public Button checkIn;
        public Button checkOut;
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
            holder.distance = (TextView) view.findViewById(R.id.tot_dis);
            holder.checkIn = (Button) view.findViewById(R.id.check_in);
            holder.checkOut = (Button) view.findViewById(R.id.check_out);
            view.setTag(holder);
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

                    String currentBuilding = build.get(final_position).BuildingName;
                    Toast.makeText(view.getContext(), "Trying to Check-in to:" + currentBuilding, Toast.LENGTH_SHORT).show();

                    //Send building to see if it is close enough to user to be checked in
                    //If not close enough, return message saying so and keep trying until can check in?
                    Building closestBuilding = BoilerCheck.loadedBuildings.nearestBuilding();
                    if(closestBuilding != null && currentBuilding.equals(closestBuilding.BuildingName)) {
                        //If close enough, send building to check-in Route
                        if(BoilerCheck.CurrentBuilding == null) {
                            BoilerCheck.CurrentBuilding = currentBuilding;
                            mCheckInTask = new CheckInTask(currentBuilding, view.getContext());
                            mCheckInTask.execute((Void) null);
                            //Test Refresh
                            mRefreshCapacityTask = new RefreshCapacityTask(view.getContext(), 1);
                            mRefreshCapacityTask.execute((Void) null);
                        }
                        else if(BoilerCheck.CurrentBuilding.equals(currentBuilding)) {
                            Toast.makeText(view.getContext(), "Already checked-in to:" + currentBuilding, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mCheckOutTask = new CheckOutTask(view.getContext());
                            mCheckOutTask.execute((Void) null);

                            BoilerCheck.CurrentBuilding = currentBuilding;

                            mCheckInTask = new CheckInTask(currentBuilding, view.getContext());
                            mCheckInTask.execute((Void) null);
                            //Test Refresh
                            mRefreshCapacityTask = new RefreshCapacityTask(view.getContext(), 1);
                            mRefreshCapacityTask.execute((Void) null);

                        }

                }
                    else if(BoilerCheck.CurrentBuilding != null) {
                        Toast.makeText(view.getContext(), "Already Checked Into: " + currentBuilding + ". Please Checkout First.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(view.getContext(), "Not Close Enough To: " + currentBuilding + " To Check-in.", Toast.LENGTH_LONG).show();
                    }

                    try{
                        Thread.sleep(500);
                    } catch (Exception e)
                    {

                    }
                }
            });

            holder.checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(view.getContext(), "Trying to Checkout of Building.",Toast.LENGTH_SHORT).show();

                    // check if user is checked-in, checkout if they are, otherwise return error
                    if(BoilerCheck.CurrentBuilding != null) {
                        mCheckOutTask = new CheckOutTask(view.getContext());
                        mCheckOutTask.execute((Void) null);
                        //Test Refresh
                        mRefreshCapacityTask = new RefreshCapacityTask(view.getContext(), 1);
                        mRefreshCapacityTask.execute((Void) null);

                        Toast.makeText(view.getContext(), "Successfully checked out of: " + BoilerCheck.CurrentBuilding, Toast.LENGTH_SHORT).show();
                        BoilerCheck.CurrentBuilding = null;

                    } else {
                        Toast.makeText(view.getContext(), "Not Checked Into Any Building. Please Check-in First.", Toast.LENGTH_LONG).show();
                    }
                    try{
                        Thread.sleep(500);
                    } catch (Exception e)
                    {

                    }

                }
            });
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.name.setText(build.get(position).BuildingName);
        holder.currCap.setText(Integer.toString(build.get(position).CurrentCapacity));
        holder.totCap.setText(Integer.toString(build.get(position).TotalCapacity));
        holder.distance.setText(Double.toString(build.get(position).distance) + " m");
        view.setTag(holder);
        return view;
    }
}
