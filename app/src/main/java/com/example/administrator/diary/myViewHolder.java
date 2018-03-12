package com.example.administrator.diary;

/**
 * Created by lenovo on 2017/12/27.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class myViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    public myViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView=itemView;
        mViews=new SparseArray<View>();
    }
    public static myViewHolder get(Context context, ViewGroup parent, int layoutId)
    {
        View itemView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        myViewHolder holder=new myViewHolder(context,itemView,parent);
        return  holder;
    }
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if(view==null)
        {
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}

