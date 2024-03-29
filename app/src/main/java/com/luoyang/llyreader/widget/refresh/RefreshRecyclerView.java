package com.luoyang.llyreader.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luoyang.llyreader.R;

/**
 * 刷新Recyclerview
 *
 * @author luoyang
 * @date 2023/2/28
 */
public class RefreshRecyclerView extends FrameLayout {

    private View view;
    private RecyclerView recyclerView;
    private RefreshProgressBar rpb;

    private View noDataView;
    private View refreshErrorView;


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        view = LayoutInflater.from(context).inflate(R.layout.view_refresh_recyclerview, this, false);
        rpb = (RefreshProgressBar) view.findViewById(R.id.rpb);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshProgressBar);
        rpb.setSpeed(a.getDimensionPixelSize(R.styleable.RefreshProgressBar_speed, rpb.getSpeed()));
        rpb.setMaxProgress(a.getInt(R.styleable.RefreshProgressBar_max_progress, rpb.getMaxProgress()));
        rpb.setSecondMaxProgress(a.getDimensionPixelSize(R.styleable.RefreshProgressBar_second_max_progress, rpb.getSecondMaxProgress()));
        rpb.setBgColor(a.getColor(R.styleable.RefreshProgressBar_bg_color, rpb.getBgColor()));
        rpb.setSecondColor(a.getColor(R.styleable.RefreshProgressBar_second_color, rpb.getSecondColor()));
        rpb.setFontColor(a.getColor(R.styleable.RefreshProgressBar_font_color, rpb.getFontColor()));
        a.recycle();

        bindEvent();

        addView(view);
    }

    private float durTouchY = -1000000;
    private BaseRefreshListener baseRefreshListener;

    public void setBaseRefreshListener(BaseRefreshListener baseRefreshListener) {
        this.baseRefreshListener = baseRefreshListener;
    }

    private OnLoadMoreListener loadMoreListener;

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private void bindEvent() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).canLoadMore() && (recyclerView.getAdapter()).getItemCount() - 1 == ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition()) {
                    if (!((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getLoadMoreError()) {
                        if (null != loadMoreListener) {
                            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(2, false);
                            loadMoreListener.startLoadmore();
                        }
                    }
                }
            }
        });

        recyclerView.setOnTouchListener(refreshTouchListener);
    }

    public RefreshProgressBar getRpb() {
        return rpb;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void refreshError() {
        rpb.setIsAutoLoading(false);
        rpb.clean();
        ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(0, true);
        if (noDataView != null) {
            noDataView.setVisibility(GONE);
        }
        if (refreshErrorView != null) {
            refreshErrorView.setVisibility(VISIBLE);
        }
    }

    public void startRefresh() {
        if (baseRefreshListener != null && baseRefreshListener instanceof OnRefreshWithProgressListener) {
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsAll(false, false);
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(1, false);
            rpb.setSecondDurProgress(rpb.getSecondMaxProgress());
            rpb.setMaxProgress(((OnRefreshWithProgressListener) baseRefreshListener).getMaxProgress());
        } else {
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(1, true);
            rpb.setIsAutoLoading(true);
            if (noDataView != null) {
                noDataView.setVisibility(GONE);
            }
            if (refreshErrorView != null) {
                refreshErrorView.setVisibility(GONE);
            }
        }
    }

    public void finishRefresh(Boolean needNoti) {
        finishRefresh(((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getItemcount() == 0, needNoti);
    }

    public void finishRefresh(Boolean isAll, Boolean needNoti) {
        rpb.setDurProgress(0);
        if (isAll) {
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(0, false);
            rpb.setIsAutoLoading(false);
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsAll(isAll, needNoti);
        } else {
            rpb.setIsAutoLoading(false);
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(0, needNoti);
        }

        if (isAll) {
            if (noDataView != null) {
                if (((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getItemcount() == 0) {
                    noDataView.setVisibility(VISIBLE);
                } else {
                    noDataView.setVisibility(GONE);
                }
            }
            if (refreshErrorView != null) {
                refreshErrorView.setVisibility(GONE);
            }
        }
    }

    public void finishLoadMore(Boolean isAll, Boolean needNoti) {
        if (isAll) {
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(0, false);
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsAll(isAll, needNoti);
        } else {
            ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(0, needNoti);
        }

        if (noDataView != null) {
            noDataView.setVisibility(GONE);
        }
        if (refreshErrorView != null) {
            refreshErrorView.setVisibility(GONE);
        }
    }

    public void setRefreshRecyclerViewAdapter(RefreshRecyclerViewAdapter refreshRecyclerViewAdapter, RecyclerView.LayoutManager layoutManager) {
        if (refreshRecyclerViewAdapter == null) {
            return;
        }
        refreshRecyclerViewAdapter.setClickTryAgainListener(new RefreshRecyclerViewAdapter.OnClickTryAgainListener() {
            @Override
            public void loadMoreErrorTryAgain() {
                if (loadMoreListener != null) {
                    loadMoreListener.loadMoreErrorTryAgain();
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(refreshRecyclerViewAdapter);
    }

    public void loadMoreError() {
        rpb.setIsAutoLoading(false);
        rpb.clean();
        ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setLoadMoreError(true, true);
    }

    public void setNoDataAndRefreshErrorView(View noData, View refreshError) {
        if (noData != null) {
            noDataView = noData;
            noDataView.setVisibility(GONE);
            addView(noDataView, getChildCount() - 1);

        }
        if (refreshError != null) {
            refreshErrorView = refreshError;
            addView(refreshErrorView, 2);
            refreshErrorView.setVisibility(GONE);
        }
    }

    private OnTouchListener refreshTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    durTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (durTouchY == -1000000) {
                        durTouchY = event.getY();
                    }
                    float dY = event.getY() - durTouchY;  //>0下拉
                    durTouchY = event.getY();
                    if (baseRefreshListener != null && ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getIsRequesting() == 0 && rpb.getSecondDurProgress() == rpb.getSecondFinalProgress()) {
                        if (rpb.getVisibility() != View.VISIBLE) {
                            rpb.setVisibility(View.VISIBLE);
                        }
                        if (recyclerView.getAdapter().getItemCount() > 0) {
                            if (0 == ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition()) {
                                rpb.setSecondDurProgress((int) (rpb.getSecondDurProgress() + dY));
                            }
                        } else {
                            rpb.setSecondDurProgress((int) (rpb.getSecondDurProgress() + dY));
                        }
                        if (rpb.getSecondDurProgress() <= 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (baseRefreshListener != null && rpb.getSecondMaxProgress() > 0 && rpb.getSecondDurProgress() > 0) {
                        if (rpb.getSecondDurProgress() >= rpb.getSecondMaxProgress() && ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getIsRequesting() == 0) {
                            if (baseRefreshListener instanceof OnRefreshWithProgressListener) {
                                //带有进度的
                                //执行刷新响应
                                ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsAll(false, false);
                                ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(1, true);
                                rpb.setMaxProgress(((OnRefreshWithProgressListener) baseRefreshListener).getMaxProgress());
                                baseRefreshListener.startRefresh();
                                if (noDataView != null) {
                                    noDataView.setVisibility(GONE);
                                }
                                if (refreshErrorView != null) {
                                    refreshErrorView.setVisibility(GONE);
                                }
                            } else {
                                //不带进度的
                                ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsAll(false, false);
                                ((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).setIsRequesting(1, true);
                                baseRefreshListener.startRefresh();
                                if (noDataView != null) {
                                    noDataView.setVisibility(GONE);
                                }
                                if (refreshErrorView != null) {
                                    refreshErrorView.setVisibility(GONE);
                                }
                                rpb.setIsAutoLoading(true);
                            }
                        } else {
                            if (((RefreshRecyclerViewAdapter) recyclerView.getAdapter()).getIsRequesting() != 1) {
                                rpb.setSecondDurProgressWithAnim(0);
                            }
                        }
                    }
                    durTouchY = -1000000;
                    break;
                default:
                    break;
            }
            return false;
        }
    };
}
