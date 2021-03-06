/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.wkfg.fragment.trending;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import org.wkfg.R;
import org.wkfg.activity.LookThroughActivity;
import org.wkfg.activity.MainActivity;
import com.xuexiang.templateproject.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.templateproject.adapter.base.delegate.SingleDelegateAdapter;
import org.wkfg.adapter.entity.NewInfo;
import com.xuexiang.templateproject.core.BaseFragment;
import org.wkfg.utils.AnonymousColor;
import org.wkfg.utils.ExchangeInfosWithAli;
import org.wkfg.utils.HTR_RGBA;
import org.wkfg.utils.MyHandler;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * 首页动态
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class TrendingFragment extends BaseFragment {
    //    @BindView(R.id.thread)
//    SuperTextView thread;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SimpleDelegateAdapter<NewInfo> mNewsAdapter;



    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trending;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);


//        //轮播条
//        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
//                banner.setSource(DemoDataProvider.getBannerList())
//                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
//            }
//        };

//        //九宫格菜单
//        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
//        gridLayoutHelper.setPadding(0, 16, 0, 0);
//        gridLayoutHelper.setVGap(10);
//        gridLayoutHelper.setHGap(0);
//        SimpleDelegateAdapter<AdapterItem> commonAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, DemoDataProvider.getGridItems(getContext())) {
//            @Override
//            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
//                if (item != null) {
//                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
//                    imageView.setCircle(true);
//                    ImageLoader.get().loadImage(imageView, item.getIcon());
//                    holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
//                    holder.text(R.id.tv_sub_title, item.getTitle());
//
//                    holder.click(R.id.ll_container, v -> XToastUtils.toast("点击了：" + item.getTitle()));
//                }
//            }
//        };
        //仪表盘图
//        SingleDelegateAdapter pictureAdapter = new SingleDelegateAdapter(R.layout.include_echarts_image) {
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//                WebView webView = holder.findViewById(R.id.gaugeChart);
//                webView.getSettings().setAllowFileAccess(true);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("file:///android_asset/Echarts_gauge.html");
//            }
//        };
        //资讯的标题
        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "十大热帖");
//                holder.text(R.id.tv_action, "更多");
//                holder.click(R.id.tv_action, v -> XToastUtils.toast("更多"));
            }
        };

        //资讯
        mNewsAdapter = new SimpleDelegateAdapter<NewInfo>(R.layout.adapter_hotnews_card_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
                if (model != null) {
                    Integer strlen = model.getThreadID().length();
                    Integer zerolen = 6 - strlen;
                    String jingThreadID = " #";
                    for (int i = 0; i < zerolen; i++) {
                        jingThreadID = jingThreadID + "0";
                    }
//                    Log.d("position", "" + position);
                    TextView hot_number = holder.findViewById(R.id.tv_hot_number);
                    GradientDrawable background = (GradientDrawable) hot_number.getBackground();
                    if(position == 0){
//                        red
                        background.setColor(Color.argb(200, 240,70,10));
                        hot_number.setTextColor(Color.argb(200,255,255,255));
                    }
                    else if(position == 1){
//                        orange
                        background.setColor(Color.argb(200, 255,153,0));
                        hot_number.setTextColor(Color.argb(200,255,255,255));
                    }
                    else if(position == 2){
//                        orange
                        background.setColor(Color.argb(150, 204,122,0));
                        hot_number.setTextColor(Color.argb(200,255,255,255));
                    }
                    else {
                        background.setColor(Color.argb(0, 255,  255,255));
                        hot_number.setTextColor(Color.argb(200,130 ,130,130));
                    }
                    hot_number.setText(String.valueOf(position+1));
                    jingThreadID = jingThreadID + model.getThreadID();
                    holder.text(R.id.tv_thread_id, jingThreadID);
                    holder.text(R.id.tv_tag, model.getTag());
                    holder.text(R.id.tv_title, model.getTitle());
                    holder.text(R.id.tv_summary, model.getSummary());
                    holder.text(R.id.tv_praise, model.getPraise() == 0 ? "0" : String.valueOf(model.getPraise()));
                    holder.text(R.id.tv_comment, model.getComment() == 0 ? "0" : String.valueOf(model.getComment()));
                    holder.text(R.id.tv_read, "阅读量 " + model.getRead());

                    ImageView hat_view = holder.findViewById(R.id.iv_hat_back);
                    GradientDrawable background_avatar = (GradientDrawable) hat_view.getBackground();
                    AnonymousColor AC = new AnonymousColor();
                    List<HTR_RGBA> colorlist = AC.getcolorlist("xui_v1_dark", Integer.parseInt(model.getThreadID()));
                    HTR_RGBA avatar_color_title = colorlist.get(0 % colorlist.size()); //和楼主一致
                    background_avatar.setColor(Color.argb(avatar_color_title.A, avatar_color_title.R, avatar_color_title.G, avatar_color_title.B));
//                    holder.image(R.id.iv_image, model.getImageUrl());

                    //holder.click(R.id.card_view, v -> Utils.goWeb(getContext(), model.getDetailUrl()));
                    holder.click(R.id.card_view, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), LookThroughActivity.class);
                            intent.putExtra("ThreadID", model.getThreadID());
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        //delegateAdapter.addAdapter(bannerAdapter);
        //delegateAdapter.addAdapter(commonAdapter);
//        delegateAdapter.addAdapter(pictureAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(mNewsAdapter);

        recyclerView.setAdapter(delegateAdapter);

    }

    @Override
    protected void initListeners() {

        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
//                try {
//                    ExchangeInfosWithAli.LastSeenHotThreadID = "NULL";
//                    mNewsAdapter.refresh(ExchangeInfosWithAli.HottestThread_json());
//                } catch (JSONException | IOException e) {
//                    e.printStackTrace();
//                }
//                refreshLayout.finishRefresh();
                ExchangeInfosWithAli.LastSeenHotThreadID = "NULL";
                MainActivity htr = (MainActivity) getActivity();
                Handler handler = new MyHandler.ThreadRefreshHandler(mNewsAdapter, refreshLayout, htr);
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        try {
                            msg.arg1 = 0;
                            msg.obj  =ExchangeInfosWithAli.HottestThread_json();
                            if (msg.obj == null){
                                msg.arg1 = -1;
                            }
                        } catch (JSONException | IOException e) {
                            msg.arg1 = 1;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }, 0);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
//                try {
//                    mNewsAdapter.loadMore(ExchangeInfosWithAli.HottestThread_json());
//                } catch (JSONException | IOException e) {
//                    e.printStackTrace();
//                }
//                refreshLayout.finishLoadMore();
                MainActivity htr = (MainActivity) getActivity();
                Handler handler = new MyHandler.ThreadLoadMoreHandler(mNewsAdapter, refreshLayout, htr);
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        try {
                            msg.arg1 = 0;
                            msg.obj  = ExchangeInfosWithAli.HottestThread_json();
                            if (msg.obj == null){
                                msg.arg1 = -1;
                            }
                        } catch (JSONException | IOException e) {
                            msg.arg1 = 1;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }, 0);
        });

        refreshLayout.autoRefresh(0, 0, 0,false);//第一次进入触发自动刷新，演示效果

        //thread.setOnSuperTextViewClickListener(this);
    }

//    @SingleClick
//    public void onClick(SuperTextView view) {
//        switch(view.getId()) {
//            case R.id.thread:
//                openNewPage(ThreadFragment.class);
//                break;
//            default:
//                break;
//        }
//    }
}
