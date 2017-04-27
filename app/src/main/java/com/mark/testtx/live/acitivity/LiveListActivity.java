package com.mark.testtx.live.acitivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mark.testtx.R;
import com.mark.testtx.live.adapter.MyAdapter;
import com.mark.testtx.live.bean.LiveBean;
import com.mark.testtx.live.bean.LiveInfo;
import com.mark.testtx.live.logic.LiveListMgr;

import java.util.ArrayList;
import java.util.List;

import cn.appsdream.nestrefresh.base.AbsRefreshLayout;
import cn.appsdream.nestrefresh.base.OnPullListener;
import cn.appsdream.nestrefresh.normalstyle.NestRefreshLayout;

public class LiveListActivity extends AppCompatActivity implements OnPullListener {

    private ListView listView;
    private MyAdapter myAdapter;
    private ViewFlipper flipper;
//    private MainAutoScrollUpView flipper;

    private ImageView[] imageViews;
    private NestRefreshLayout loader;

    private int mDataType = LiveListMgr.LIST_TYPE_VOD;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        initImageViews();
        initView();
        initData();

        loader = new NestRefreshLayout(listView);
        loader.setPullLoadEnable(true);
        loader.setPullRefreshEnable(true);
        loader.setOnLoadingListener(this);
        mEmptyView = new View(this);
    }

    private void initData() {

        myAdapter = new MyAdapter(getLiveBeanList(), this);
        listView.setAdapter(myAdapter);
//        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,));
        new MyAsckTask().execute("1");
    }


    private List<LiveBean> getLiveBeanList() {
        List<LiveBean> liveBeens = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            LiveBean liveBean = new LiveBean();
            liveBean.setType(i % 3);
            liveBeens.add(liveBean);
        }
        return liveBeens;
    }

    private void initView() {
//        mRecyclerView = (RecyclerView) findViewById(R.id.live_list);
        listView = (ListView) findViewById(R.id.listView);
        listView.addHeaderView(getHeadView());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    view.performClick();
                } else {
                    Intent intent = new Intent(LiveListActivity.this, PlayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initImageViews() {
        imageViews = new ImageView[3];
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.view_bg);
        imageViews[0] = imageView1;
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.view_bg);
        imageViews[1] = imageView2;
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.view_bg);
        imageViews[2] = imageView3;
    }

    private View getHeadView() {
        View view = getLayoutInflater().inflate(R.layout.head_live_list, null);
//        flipper = (MainAutoScrollUpView) view.findViewById(R.id.vf);
        view.findViewById(R.id.banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveListActivity.this, "你点到我了，我是广告位", Toast.LENGTH_SHORT).show();
            }
        });

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ImageView view = imageViews[position];
                container.removeView(view);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                try {
                    if (imageViews[position].getParent() == null) {
                        ((ViewPager) container).addView(imageViews[position], 0);
                    } else {
                        ((ViewGroup) (imageViews[position].getParent()))
                                .removeView(imageViews[position]);
                    }
                } catch (Exception e) {
                }

                imageViews[position].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LiveListActivity.this, "你点到我了", Toast.LENGTH_SHORT).show();
                    }
                });

                return imageViews[position % imageViews.length];
            }


        });
        flipper = (ViewFlipper) view.findViewById(R.id.vf);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        flipper.stop();
    }

    @Override
    public void onRefresh(AbsRefreshLayout listLoader) {
//        getData(true);
        reloadLiveList();
    }

    @Override
    public void onLoading(AbsRefreshLayout listLoader) {
//        getData(false);
    }

    private void getData(boolean refresh) {
        if (refresh) {
            myAdapter.getLiveBeen().clear();
        }

        int count = myAdapter.getCount();
        for (int i = 0; i < 20; i++) {
            LiveBean liveBean = new LiveBean();
            liveBean.setType(i % 3);
            liveBean.setTitle("第" + i + count + "条广告位");
            myAdapter.getLiveBeen().add(liveBean);
        }
        myAdapter.notifyDataSetChanged();

        if (myAdapter.getCount() == 100) {
            loader.onLoadAll();
        } else {
            loader.onLoadFinished();
        }
    }

    class MyAsckTask extends AsyncTask<String, String, List<LiveBean>> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(LiveListActivity.this, "正在加载", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(List<LiveBean> list) {
            for (int i = 0; i < list.size(); i++) {
                TextView textView = new TextView(LiveListActivity.this);
                textView.setText(list.get(i).getTitle());
                textView.setTextSize(30);
                flipper.addView(textView);
            }
//            flipper.setData((ArrayList<LiveBean>) list);
//            flipper.setTextSize(20);
//            flipper.setTimer(2000);
//            flipper.start();
        }

        @Override
        protected List<LiveBean> doInBackground(String... params) {
            List<LiveBean> strings = new ArrayList<>();
            try {
                Thread.sleep(1000);
                for (int i = 0; i < 10; i++) {
                    LiveBean liveBean = new LiveBean();
                    liveBean.setTitle("第" + i + "条广告位");
                    strings.add(liveBean);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return strings;
        }
    }

    /**
     * 重新加载直播列表
     */
    private boolean reloadLiveList() {
        switch (mDataType) {
            case LiveListMgr.LIST_TYPE_LIVE:
                return LiveListMgr.getInstance(this).reloadLiveList(mDataType, mLiveListener);
            case LiveListMgr.LIST_TYPE_VOD:
                return LiveListMgr.getInstance(this).reloadLiveList(mDataType, mVodListener);
            case LiveListMgr.LIST_TYPE_UGC:
//                return LiveListMgr.getInstance().reloadLiveList(mDataType, mUGCListener);
        }
        return false;
    }


    LiveListMgr.Listener mLiveListener = new LiveListMgr.Listener() {

        @Override
        public void onLiveList(int retCode, ArrayList<LiveInfo> result, boolean refresh) {
            if (mDataType == LiveListMgr.LIST_TYPE_LIVE) {
                onGetListData(retCode, result, refresh);
                mEmptyView.setVisibility(myAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
            }
            loader.onLoadFinished();
        }
    };

    LiveListMgr.Listener mVodListener = new LiveListMgr.Listener() {

        @Override
        public void onLiveList(int retCode, ArrayList<LiveInfo> result, boolean refresh) {
            if (mDataType == LiveListMgr.LIST_TYPE_VOD) {
                onGetListData(retCode, result, refresh);
                mEmptyView.setVisibility(myAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
            }
            loader.onLoadFinished();
        }
    };

    private void onGetListData(int retCode, ArrayList<LiveInfo> result, boolean refresh) {
        if (retCode == 0) {
            myAdapter.clear();
            if (result != null) {
                myAdapter.addAll((ArrayList<LiveInfo>) result.clone());
            }
            if (refresh) {
                myAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "刷新列表失败", Toast.LENGTH_LONG).show();
        }
    }

}
