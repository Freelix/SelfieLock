package com.selfielock.achievement;

import java.util.Iterator;

import com.selfielock.R;
import com.selfielock.database.AchievementCollection;
import com.selfielock.database.AchievementTransactions;

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

    private Context context; 
    private int layoutResourceId;    
    private Achievement data[] = null;
    
    // Request to database
    private AchievementTransactions at;
    private AchievementCollection ac;
    
    public AchievementAdapter(Context context, int layoutResourceId, Achievement[] data, AchievementCollection ac) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        
        this.at = new AchievementTransactions(this.context);
        this.ac = ac;
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
        
        int test = ac.getListAchievements().size();
        
        // Put green on unlocked achievements        
        if (ac.getListAchievements().get(position).isUnlocked())
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
