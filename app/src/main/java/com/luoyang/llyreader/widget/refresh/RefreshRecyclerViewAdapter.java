package com.luoyang.llyreader.widget.refresh;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luoyang.llyreader.R;


/**
 *
 *
 * @author luoyang
 * @date 2023/3/01
 */
public abstract class RefreshRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int LOAD_MORE_TYPE = 2001;

    private Handler handler;
    /**
     * 0是未执行网络请求  1是正在下拉刷新  2是正在加载更多
     */
    private int isRequesting = 0;
    private Boolean needLoadMore = false;
    /**
     * 判断是否还有更多
     */
    private Boolean isAll = false;
    private Boolean loadMoreError = false;

    private OnClickTryAgainListener clickTryAgainListener;

    public interface OnClickTryAgainListener {
        void loadMoreErrorTryAgain();
    }

    public RefreshRecyclerViewAdapter(Boolean needLoadMore) {
        this.needLoadMore = needLoadMore;
        handler = new Handler();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_TYPE) {
            return new LoadMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_refresh_loadmore, parent, false));
        } else {
            return onCreateViewholder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateViewholder(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LOAD_MORE_TYPE) {
            if (!loadMoreError) {
                ((LoadMoreViewHolder) holder).tvLoadMore.setText("正在加载...");
            } else {
                ((LoadMoreViewHolder) holder).tvLoadMore.setText("加载失败,点击重试");
            }
            ((LoadMoreViewHolder) holder).tvLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != clickTryAgainListener && loadMoreError) {
                        clickTryAgainListener.loadMoreErrorTryAgain();
                        loadMoreError = false;
                        ((LoadMoreViewHolder) holder).tvLoadMore.setText("正在加载...");
                    }
                }
            });
        } else
            onBindViewholder(holder, position);
    }

    public abstract void onBindViewholder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (needLoadMore && isRequesting != 1 && !isAll && getItemcount() > 0) {
            return getItemcount() + 1;
        } else
            return getItemcount();
    }

    public abstract int getItemcount();


    public int getIsRequesting() {
        return isRequesting;
    }

    public void setIsRequesting(int isRequesting, Boolean needNoti) {
        this.isRequesting = isRequesting;
        if (this.isRequesting == 1) {
            isAll = false;
        }
        if (needNoti) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                notifyItemRangeChanged(getItemCount(), getItemCount() - getItemcount());
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (needLoadMore && isRequesting != 1 && !isAll && position == getItemCount() - 1 && getItemcount() > 0) {
            return LOAD_MORE_TYPE;
        } else {
            return getItemViewtype(position);
        }
    }

    public abstract int getItemViewtype(int position);


    public void setIsAll(Boolean isAll, Boolean needNoti) {
        this.isAll = isAll;
        if (needNoti) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (getItemCount() > getItemcount()) {
                    notifyItemRangeChanged(getItemCount(), getItemCount() - getItemcount());
                } else {
                    notifyItemRemoved(getItemCount() + 1);
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }


    class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        FrameLayout llLoadMore;
        TextView tvLoadMore;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            llLoadMore = itemView.findViewById(R.id.ll_loadmore);
            tvLoadMore = itemView.findViewById(R.id.tv_loadmore);
        }
    }

    public Boolean canLoadMore() {
        return needLoadMore && isRequesting == 0 && !isAll && getItemcount() > 0;
    }

    public OnClickTryAgainListener getClickTryAgainListener() {
        return clickTryAgainListener;
    }

    public void setClickTryAgainListener(OnClickTryAgainListener clickTryAgainListener) {
        this.clickTryAgainListener = clickTryAgainListener;

    }

    public Boolean getLoadMoreError() {
        return loadMoreError;
    }

    public void setLoadMoreError(Boolean loadMoreError, Boolean needNoti) {
        this.isRequesting = 0;
        this.loadMoreError = loadMoreError;
        if (needNoti) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                notifyDataSetChanged();
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
