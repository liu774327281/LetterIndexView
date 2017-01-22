package org.mobiletrain.letterindexview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangsong on 2016/4/25.
 */
public class MyAdapter extends BaseAdapter implements SectionIndexer {
    private List<UserEntity> list;
    private Context context;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<UserEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.showLetter = (TextView) convertView.findViewById(R.id.show_letter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserEntity userEntity = list.get(position);
        holder.username.setText(userEntity.getUsername());
        //获得当前item的分组信息
        int sectionForPosition = getSectionForPosition(position);
        //获得当前item所在分组的第一个item的position
        int positionForSection = getPositionForSection(sectionForPosition);
        if (positionForSection == position) {
            holder.showLetter.setVisibility(View.VISIBLE);
            holder.showLetter.setText(userEntity.getFirstLetter());
        }else{
            holder.showLetter.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    //根据分组的名称，返回该组中第一个item的position
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFirstLetter().charAt(0) == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    //根据position获得该item的分组信息
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getFirstLetter().charAt(0);
    }

    class ViewHolder {
        TextView username, showLetter;
    }
}
