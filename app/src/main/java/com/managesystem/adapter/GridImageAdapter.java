package com.managesystem.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.listener.UploadListener;
import com.lzy.okhttpserver.task.ExecutorWithListener;
import com.lzy.okhttpserver.upload.UploadInfo;
import com.lzy.okhttpserver.upload.UploadManager;
import com.managesystem.R;
import com.managesystem.config.Urls;
import com.managesystem.widegt.ProgressPieView;

import java.io.File;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/29.
 */
public class GridImageAdapter extends BaseListAdapter<ImageItem> implements ExecutorWithListener.OnAllTaskEndListener {
    public Boolean isAll=false;
    public StringBuilder sb = new StringBuilder();
    private UploadManager uploadManager;

    public void setImagePicker(ImagePicker imagePicker) {
        this.imagePicker = imagePicker;
    }

    private ImagePicker imagePicker
            ;

    public GridImageAdapter(Activity context) {
        super(context);
        uploadManager = UploadManager.getInstance(context);
        uploadManager.getThreadPool().setCorePoolSize(3);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_upload_manager, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        imagePicker.getImageLoader().displayImage(mContext, mList.get(position).path, holder.imageView,150,150);
        return convertView;
    }

    @Override
    public void onAllTaskEnd() {
        Toast.makeText(mContext, "所有上传任务完成", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView tvProgress;
        private ProgressPieView civ;
        private View mask;
        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            imageView.setLayoutParams(params);
            tvProgress = (TextView) convertView.findViewById(R.id.tvProgress);
            mask = convertView.findViewById(R.id.mask);
            civ = (ProgressPieView) convertView.findViewById(R.id.civ);
            tvProgress.setText("请上传");
            civ.setText("请上传");
        }

        public void refresh(UploadInfo uploadInfo) {
            if (uploadInfo.getState() == DownloadManager.NONE) {
                tvProgress.setText("请上传");
                civ.setText("请上传");
            } else if (uploadInfo.getState() == UploadManager.ERROR) {
                tvProgress.setText("上传出错");
                civ.setText("错误");
            } else if (uploadInfo.getState() == UploadManager.WAITING) {
                tvProgress.setText("等待中");
                civ.setText("等待");
            } else if (uploadInfo.getState() == UploadManager.FINISH) {
                tvProgress.setText("上传成功");
                civ.setText("成功");
            } else if (uploadInfo.getState() == UploadManager.UPLOADING) {
                tvProgress.setText("上传中");
                civ.setProgress((int) (uploadInfo.getProgress() * 100));
                civ.setText((Math.round(uploadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
            }
        }

        public void finish() {
            tvProgress.setText("上传成功");
            civ.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);
        }
    }


    private class MyUploadListener extends UploadListener<String> {

        private ViewHolder holder;

        @Override
        public void onProgress(UploadInfo uploadInfo) {
            Log.e("MyUploadListener", "onProgress:" + uploadInfo.getTotalLength() + " " + uploadInfo.getUploadLength() + " " + uploadInfo.getProgress());
            holder = (ViewHolder) ((View) getUserTag()).getTag();
            holder.refresh(uploadInfo);
        }

        @Override
        public void onFinish(String s) {
            Log.e("MyUploadListener", "finish:" + s);
            holder.finish();
            sb.append(s).append(",");
        }

        @Override
        public void onError(UploadInfo uploadInfo, String errorMsg, Exception e) {
            Log.e("MyUploadListener", "onError:" + errorMsg);
        }

        @Override
        public String parseNetworkResponse(Response response) throws Exception {
            Log.e("MyUploadListener", "parseNetworkResponse");
            return response.body().string();
        }
    }

    public void upload(GridView gridView){
        for (int i = 0; i < mList.size(); i++) {
            MyUploadListener listener = new MyUploadListener();
            listener.setUserTag(gridView.getChildAt(i));
            uploadManager.addTask(Urls.UPLOAD,new File(mList.get(i).path), "file", listener);
        }
    }

    public void excute(){
        uploadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(this);
    }

    public void remove(){
        uploadManager.getThreadPool().getExecutor().removeOnAllTaskEndListener(this);
    }
}
