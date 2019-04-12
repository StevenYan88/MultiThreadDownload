package com.steven.download.widget.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class CommonViewHolder extends RecyclerView.ViewHolder {
    //用于缓存View
    private SparseArray<View> mView;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mView = new SparseArray<>();
    }


    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }


    //通用的setText进行封装

    /**
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 本地图片
     *
     * @param viewId
     * @param resourceId
     * @return
     */
    public CommonViewHolder setImageResoucrce(int viewId, int resourceId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resourceId);
        return this;
    }




    /**
     * 网络图片处理
     *
     * @param viewId
     * @param imageLoader
     * @return
     */
    public CommonViewHolder setImagePath(int viewId, HolderImageLoader imageLoader) {
        ImageView iv = getView(viewId);
        imageLoader.loadImage(iv, imageLoader.getPath());
        return this;
    }

    abstract static class HolderImageLoader {
        private String path;

        public HolderImageLoader(String path) {
            this.path = path;
        }

        /**
         * 加载图片
         *
         * @param imageView
         * @param path
         */
        abstract void loadImage(ImageView imageView, String path);

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
