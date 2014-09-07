package com.selfielock.achievement;

import com.selfielock.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementAdapter extends ArrayAdapter<Achievement>{

    Context context; 
    int layoutResourceId;    
    Achievement data[] = null;
    
    public AchievementAdapter(Context context, int layoutResourceId, Achievement[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AchievementHolder holder = null;
        
        if(row == null)
        {
            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new AchievementHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtDescription);
            
            row.setTag(holder);
        }
        else
        {
            holder = (AchievementHolder)row.getTag();
        }
        
        if (position == 0 || position == 4)
        	row.setBackgroundResource(R.drawable.achievement_unlock);
        
        Achievement achievement = data[position];
        holder.txtTitle.setText(achievement.getDescription());
        holder.imgIcon.setImageResource(achievement.getImg());
        
        return row;
    }
    
    static class AchievementHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
