package com.luoyang.llyreader.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luoyang.llyreader.R;

/**
 * package: com.luoyang.llreader.view.mdialog
 * created by luoyang
 * QQ:1845313665
 * on 2018/11/7
 */
public class MProgressView extends LinearLayout {

    private Context context;

    public MProgressView(Context context) {
        this(context, null);
    }

    public MProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);
    }

    /**
     * 转圈的载入
     *
     * @param text 展示的文字
     */
    public void showLoading(String text) {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.mprogress_dialog_loading, this, true);
        TextView msgTv = (TextView) findViewById(R.id.msg_tv);
        if (text != null && text.length() > 0) {
            msgTv.setText(text);
        }

        ProgressBar rlLoading = (ProgressBar) findViewById(R.id.rl_loading);
        rlLoading.setVisibility(View.VISIBLE);
    }

    /**
     * 单个按钮的信息提示框
     *
     * @param msg
     * @param listener
     */
    public void showInfo(String msg, final OnClickListener listener) {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.mprogress_dialog_infor, this, true);
        TextView msgTv = (TextView) findViewById(R.id.msg_tv);
        msgTv.setText(msg);
        TextView tvClose = (TextView) findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }


    /**
     * 单个按钮的信息提示框
     *
     * @param msg
     * @param btnText
     * @param listener
     */
    public void showInfo(String msg, String btnText, final OnClickListener listener) {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.mprogress_dialog_infor, this, true);
        TextView msgTv = (TextView) findViewById(R.id.msg_tv);
        msgTv.setText(msg);
        TextView tvClose = (TextView) findViewById(R.id.tv_close);
        tvClose.setText(btnText);
        tvClose.setOnClickListener(listener);
    }

    /**
     * 两个不同等级的按钮
     *
     * @param msg
     * @param b_f
     * @param c_f
     * @param b_s
     * @param c_s
     */
    public void showTwoButton(String msg, String b_f, OnClickListener c_f, String b_s, OnClickListener c_s) {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.mprogress_dialog_two, this, true);
        TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        TextView tvDone = (TextView) findViewById(R.id.tv_done);
        tvMsg.setText(msg);
        tvCancel.setText(b_f);
        tvCancel.setOnClickListener(c_f);
        tvDone.setText(b_s);
        tvDone.setOnClickListener(c_s);
    }


    /**
     * 离线章节选择
     *
     * @param startIndex
     * @param endIndex
     * @param all
     * @param clickDownload
     * @param cancel
     */
    public void showDownloadList(int startIndex, int endIndex, final int all, final MProgressHUD.OnClickDownload clickDownload, OnClickListener cancel){
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.mprogress_dialog_downloadchoice, this, true);
        final EditText edtStart = (EditText) findViewById(R.id.edt_start);
        final EditText edtEnd = (EditText) findViewById(R.id.edt_end);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(cancel);
        TextView tvDownload = (TextView) findViewById(R.id.tv_download);
        edtStart.setText(String.valueOf(startIndex+1));
        edtEnd.setText(String.valueOf(endIndex+1));
        edtStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtStart.getText().length()>0){
                    try{
                        int temp = Integer.parseInt(edtStart.getText().toString().trim());
                        if(temp>all){
                            edtStart.setText(String.valueOf(all));
                            edtStart.setSelection(edtStart.getText().length());
                            Toast.makeText(context,"超过总章节",Toast.LENGTH_SHORT).show();
                        }else if(temp<=0){
                            edtStart.setText(String.valueOf(1));
                            edtStart.setSelection(edtStart.getText().length());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        edtEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtEnd.getText().length()>0){
                    try{
                        int temp = Integer.parseInt(edtEnd.getText().toString().trim());
                        if(temp>all){
                            edtEnd.setText(String.valueOf(all));
                            edtEnd.setSelection(edtEnd.getText().length());
                            Toast.makeText(context,"超过总章节",Toast.LENGTH_SHORT).show();
                        }else if(temp<=0){
                            edtEnd.setText(String.valueOf(1));
                            edtEnd.setSelection(edtEnd.getText().length());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        tvDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtStart.getText().length()>0 && edtEnd.getText().length()>0){
                    if(Integer.parseInt(edtStart.getText().toString())>Integer.parseInt(edtEnd.getText().toString())){
                        Toast.makeText(context,"输入错误",Toast.LENGTH_SHORT).show();
                    }else{
                        if(clickDownload!=null){
                            clickDownload.download(Integer.parseInt(edtStart.getText().toString())-1,Integer.parseInt(edtEnd.getText().toString())-1);
                        }
                    }
                }else{
                    Toast.makeText(context,"请输入要离线的章节",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
