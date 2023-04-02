package com.luoyang.llyreader.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luoyang.llyreader.R;
import com.luoyang.llyreader.bean.BookShelfBean;
import com.luoyang.llyreader.widget.refresh.RefreshRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * @author luoyang
 * @date 2023/3/1
 */
public class BookShelfAdapter extends RefreshRecyclerViewAdapter {
    private final int TYPE_LATEST = 1;
    private final int TYPE_OTHER = 2;

    /**
     * item动画启动间隔
     */
    private final long DURATION_ITEM = 130;

    private final List<BookShelfBean> books;

    private Boolean needAnim = true;

    private OnItemClickListener itemClickListener;


    public BookShelfAdapter() {
        super(false);
        books = new ArrayList<>();
    }

    public interface OnItemClickListener {
        /**
         * 去搜索
         */
        void toSearch();

        /**
         * 点击
         *
         * @param bookShelfBean
         * @param index
         */
        void onClick(BookShelfBean bookShelfBean, int index);

        /**
         * 长按
         *
         * @param view
         * @param bookShelfBean
         * @param index
         */
        void onLongClick(View view, BookShelfBean bookShelfBean, int index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewholder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LATEST) {
            return new LatestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookshelf_lastest, parent, false));
        } else {
            return new OtherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookshelf_other, parent, false));
        }
    }

    @Override
    public void onBindViewholder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_LATEST) {
            bindLastestViewHolder((LatestViewHolder) holder, position);
        } else {
            bindOtherViewHolder((OtherViewHolder) holder, position - 1);
        }

    }

    private void bindLastestViewHolder(final LatestViewHolder holder, final int index) {
        if (books.size() == 0) {
            holder.tvWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        itemClickListener.toSearch();
                    }
                }
            });
            holder.ivCover.setImageResource(R.mipmap.img_cover_default);
            holder.flLastestTip.setVisibility(View.INVISIBLE);
            holder.tvName.setText("最近阅读的书在这里");
            holder.tvDurprogress.setText("");
            holder.llDurcursor.setVisibility(View.INVISIBLE);
//            holder.mpbDurprogress.setVisibility(View.INVISIBLE);
//            holder.mpbDurprogress.setProgressListener(null);
            holder.tvWatch.setText("去选书");
        } else {
            Glide.with(holder.ivCover.getContext()).load(books.get(index).getBookInfoBean().getCoverUrl())
                    .dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop().placeholder(R.mipmap.img_cover_default).into(holder.ivCover);

            holder.flLastestTip.setVisibility(View.VISIBLE);

            holder.tvName.setText(String.format(holder.tvName.getContext().getString(R.string.tv_book_name),
                    books.get(index).getBookInfoBean().getName()));

            if (null != books.get(index).getBookInfoBean() && null != books.get(index).getBookInfoBean().getChapterList() && books.get(index).getBookInfoBean().getChapterList().size() > books.get(index).getDurChapter()) {
                holder.tvDurprogress.setText(String.format(holder.tvDurprogress.getContext().getString(R.string.tv_read_durprogress), books.get(index).getBookInfoBean().getChapterList().get(books.get(index).getDurChapter()).getDurChapterName()));
            }
            holder.llDurcursor.setVisibility(View.VISIBLE);
//            holder.mpbDurprogress.setVisibility(View.VISIBLE);

            holder.tvWatch.setText("继续阅读");
            holder.tvWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        itemClickListener.onClick(books.get(index), index);
                    }
                }
            });
        }
    }

    private void bindOtherViewHolder(final OtherViewHolder holder, int index) {
        final int index_1 = index * 3;
        if (needAnim) {
            final Animation animation = AnimationUtils.loadAnimation(holder.flContent_1.getContext(), R.anim.anim_bookshelf_item);
            animation.setAnimationListener(new AnimationStartListener() {
                @Override
                void onAnimStart(Animation animation) {
                    needAnim = false;
                    holder.flContent_1.setVisibility(View.VISIBLE);
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != holder) {
                        holder.flContent_1.startAnimation(animation);
                    }
                }
            }, index_1 * DURATION_ITEM);
        } else {
            holder.flContent_1.setVisibility(View.VISIBLE);
        }
        Glide.with(holder.ivCover_1.getContext()).load(books.get(index_1).getBookInfoBean()
                        .getCoverUrl()).dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop().placeholder(R.mipmap.img_cover_default).into(holder.ivCover_1);
        holder.tvName_1.setText(books.get(index_1).getBookInfoBean().getName());

        holder.ibContent_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(books.get(index_1), index_1);
                }
            }
        });
        holder.ibContent_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onLongClick(holder.ivCover_1, books.get(index_1), index_1);
                    return true;
                } else {
                    return false;
                }
            }
        });

        final int index_2 = index_1 + 1;
        if (index_2 < books.size()) {
            if (needAnim) {
                final Animation animation = AnimationUtils.loadAnimation(holder.flContent_2.getContext(), R.anim.anim_bookshelf_item);
                animation.setAnimationListener(new AnimationStartListener() {
                    @Override
                    void onAnimStart(Animation animation) {
                        needAnim = false;
                        holder.flContent_2.setVisibility(View.VISIBLE);
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != holder) {
                            holder.flContent_2.startAnimation(animation);
                        }
                    }
                }, index_2 * DURATION_ITEM);
            } else {
                holder.flContent_2.setVisibility(View.VISIBLE);
            }
            Glide.with(holder.ivCover_2.getContext()).load(books.get(index_2).getBookInfoBean().getCoverUrl())
                    .dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .placeholder(R.mipmap.img_cover_default).into(holder.ivCover_2);
            holder.tvName_2.setText(books.get(index_2).getBookInfoBean().getName());

            holder.ibContent_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(books.get(index_2), index_2);
                    }
                }
            });
            holder.ibContent_2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        if (itemClickListener != null) {
                            itemClickListener.onLongClick(holder.ivCover_2, books.get(index_2), index_2);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            final int index_3 = index_2 + 1;
            if (index_3 < books.size()) {
                if (needAnim) {
                    final Animation animation = AnimationUtils.loadAnimation(holder.flContent_3.getContext(), R.anim.anim_bookshelf_item);
                    animation.setAnimationListener(new AnimationStartListener() {
                        @Override
                        void onAnimStart(Animation animation) {
                            needAnim = false;
                            holder.flContent_3.setVisibility(View.VISIBLE);
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != holder) {
                                holder.flContent_3.startAnimation(animation);
                            }
                        }
                    }, index_3 * DURATION_ITEM);
                } else {
                    holder.flContent_3.setVisibility(View.VISIBLE);
                }
                Glide.with(holder.ivCover_3.getContext()).load(books.get(index_3).getBookInfoBean().getCoverUrl())
                        .dontAnimate()
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .centerCrop()
                        .placeholder(R.mipmap.img_cover_default).into(holder.ivCover_3);
                holder.tvName_3.setText(books.get(index_3).getBookInfoBean().getName());

                holder.ibContent_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onClick(books.get(index_3), index_3);
                        }
                    }
                });
                holder.ibContent_3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (itemClickListener != null) {
                            if (itemClickListener != null) {
                                itemClickListener.onLongClick(holder.ivCover_3, books.get(index_3), index_3);
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            } else {
                holder.flContent_3.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.flContent_2.setVisibility(View.INVISIBLE);
            holder.flContent_3.setVisibility(View.INVISIBLE);
        }
    }

    abstract class AnimationStartListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            onAnimStart(animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        abstract void onAnimStart(Animation animation);
    }


    @Override
    public int getItemcount() {
        if (books.size() == 0) {
            return 1;
        } else {
            if (books.size() % 3 == 0) {
                return 1 + books.size() / 3;
            } else {
                return 1 + (books.size() / 3 + 1);
            }
        }
    }

    public int getRealItemCount() {
        return books.size();
    }

    @Override
    public int getItemViewtype(int position) {
        if (position == 0) {
            return TYPE_LATEST;
        } else {
            return TYPE_OTHER;
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Boolean getNeedAnim() {
        return needAnim;
    }

    public void setNeedAnim(Boolean needAnim) {
        this.needAnim = needAnim;
    }

    public synchronized void replaceAll(List<BookShelfBean> newDatas) {
        books.clear();
        if (null != newDatas && newDatas.size() > 0) {
            books.addAll(newDatas);
        }
        order();

        notifyDataSetChanged();
    }

    private void order() {
        if (books != null && books.size() > 0) {
            for (int i = 0; i < books.size(); i++) {
                int temp = i;
                for (int j = i + 1; j < books.size(); j++) {
                    if (books.get(temp).getFinalDate() < books.get(j).getFinalDate()) {
                        temp = j;
                    }
                }
                BookShelfBean tempBookShelfBean = books.get(i);
                books.set(i, books.get(temp));
                books.set(temp, tempBookShelfBean);
            }
        }
    }

    public List<BookShelfBean> getBooks() {
        return books;
    }


    class LatestViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        FrameLayout flLastestTip;
        AutofitTextView tvName;
        AutofitTextView tvDurprogress;
        LinearLayout llDurcursor;
        //        ProgressBar mpbDurprogress;
        TextView tvWatch;

        public LatestViewHolder(View itemView) {
            super(itemView);
            ivCover =itemView.findViewById(R.id.iv_cover);
            flLastestTip = itemView.findViewById(R.id.fl_lastest_tip);
            tvName =  itemView.findViewById(R.id.tv_name);
            tvDurprogress =  itemView.findViewById(R.id.tv_durprogress);
            llDurcursor =  itemView.findViewById(R.id.ll_durcursor);
//            mpbDurprogress = (ProgressBar) itemView.findViewById(R.id.mpb_durprogress);
            tvWatch = itemView.findViewById(R.id.tv_watch);
        }
    }

    class OtherViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flContent_1;
        ImageView ivCover_1;
        AutofitTextView tvName_1;
        ImageButton ibContent_1;

        FrameLayout flContent_2;
        ImageView ivCover_2;
        AutofitTextView tvName_2;
        ImageButton ibContent_2;

        FrameLayout flContent_3;
        ImageView ivCover_3;
        AutofitTextView tvName_3;
        ImageButton ibContent_3;

        public OtherViewHolder(View itemView) {
            super(itemView);
            flContent_1 = itemView.findViewById(R.id.fl_content_1);
            ivCover_1 = itemView.findViewById(R.id.iv_cover_1);
            tvName_1 = itemView.findViewById(R.id.tv_name_1);
            ibContent_1 = itemView.findViewById(R.id.ib_content_1);

            flContent_2 = itemView.findViewById(R.id.fl_content_2);
            ivCover_2 = itemView.findViewById(R.id.iv_cover_2);
            tvName_2 = itemView.findViewById(R.id.tv_name_2);
            ibContent_2 = itemView.findViewById(R.id.ib_content_2);

            flContent_3 = itemView.findViewById(R.id.fl_content_3);
            ivCover_3 = itemView.findViewById(R.id.iv_cover_3);
            tvName_3 = itemView.findViewById(R.id.tv_name_3);
            ibContent_3 = itemView.findViewById(R.id.ib_content_3);
        }


    }
}

