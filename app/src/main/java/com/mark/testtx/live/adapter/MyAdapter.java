package com.mark.testtx.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mark.testtx.R;
import com.mark.testtx.live.bean.LiveBean;
import com.mark.testtx.live.bean.LiveInfo;

import java.util.List;

/**
 * Created by Mark.Han on 2017/4/24.
 */
public class MyAdapter extends BaseAdapter {

    public static final int TYPE_LIVE = 0;
    public static final int TYPE_REPLAY = 1;
    public static final int TYPE_REPLAY_GROUP = 2;

    private List<LiveBean> liveBeen;
    private Context context;
    private LayoutInflater inflater;

    public MyAdapter(List<LiveBean> liveBeen, Context context) {
        this.liveBeen = liveBeen;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public List<LiveBean> getLiveBeen() {
        return liveBeen;
    }

    public void clear() {
        liveBeen.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<LiveInfo> LiveInfo) {

    }

    @Override
    public int getCount() {
        return liveBeen.size();
    }

    @Override
    public LiveBean getItem(int position) {
        return liveBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LiveBean liveBean = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_live_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.big_title);
            viewHolder.second_title = (TextView) convertView.findViewById(R.id.second_title);
            viewHolder.second_title = (TextView) convertView.findViewById(R.id.second_title);
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            viewHolder.replay_btn = (TextView) convertView.findViewById(R.id.replay_btn);
            viewHolder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.subscribe_btn = (TextView) convertView.findViewById(R.id.subscribe_btn);
            viewHolder.bg = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.rl_live = (RelativeLayout) convertView.findViewById(R.id.live);
            viewHolder.rl_replay_group = (RelativeLayout) convertView.findViewById(R.id.live_group);
            viewHolder.rl_replay = (RelativeLayout) convertView.findViewById(R.id.live_replay);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int type = liveBean.getType();
        viewHolder.hide(type);

//        if (type == 0) {
//        }

        return convertView;
    }

//    @Override
//    public int getItemViewType(int position) {
////        if ()
////        if (comNames[position].length() == 1) {
////            return TYPE_TITLE;
////        } else {
////            return TYPE_COMPANY;
////        }
//        int type = liveBeen.get(position).getType();
//        if (type == 0) {
//            return TYPE_LIVE;
//        } else if (type == 1) {
//            return TYPE_REPLAY;
//        } else if (type == 2) {
//            return TYPE_REPLAY_GROUP;
//        }
//        return TYPE_REPLAY_GROUP;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }

    class ViewHolder {
        TextView title, second_title, num, replay_btn, status_tv, time_tv, subscribe_btn;
        ImageView bg;
        RelativeLayout rl_live, rl_replay_group, rl_replay;

        public void hide(int type) {
            if (type == 0) {
                rl_live.setVisibility(View.VISIBLE);
                rl_replay.setVisibility(View.INVISIBLE);
                rl_replay_group.setVisibility(View.INVISIBLE);

            } else if (type == 1) {

                rl_replay.setVisibility(View.VISIBLE);
                rl_live.setVisibility(View.INVISIBLE);
                rl_replay_group.setVisibility(View.INVISIBLE);
            } else if (type == 2) {
                rl_replay_group.setVisibility(View.VISIBLE);
                rl_replay.setVisibility(View.INVISIBLE);
                rl_live.setVisibility(View.INVISIBLE);

            }
        }
    }


//    class LiveViewHolder {
//        TextView title;
//    }
//
//    class ReplayViewHolder {
//        TextView com;
//        ImageView icon;
//    }

//    class ReplayGroupViewHolder {
//        TextView com;
//        ImageView icon;
//    }


}
