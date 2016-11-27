package com.managesystem.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.managesystem.R;
import com.managesystem.tools.ImageUtils;
import com.managesystem.tools.MediaScanner;
import com.managesystem.widegt.HackyViewPager;
import com.wksc.framwork.util.StringUtils;
import com.wksc.framwork.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片放大
 * Author: puhua
 * Date: 2016/4/8 10:43
 */
public class ImageActivity extends ImageBaseActivity {
    @Bind(R.id.view_pager)
    HackyViewPager viewPager;
    @Bind(R.id.tv_current)
    TextView tvCurrent;
    @Bind(R.id.btn_down)
    ImageView btnDown;
    @Bind(R.id.content)
    View content;
    private ArrayList<String> imageList = new ArrayList<>();
    private int position = 0;
    private static final String STATE_POSITION = "STATE_POSITION";
    private ProgressDialog mSaveDialog;

    private File PHOTO_DIR = null;

    private File mCurrentPhotoFile;

    private MediaScanner mMediaScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        ButterKnife.bind(this);
        /*tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);*/
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            imageList = bd.getStringArrayList("list");
            position = bd.getInt("position");
            if (imageList != null && imageList.size() > 0) {
                if (imageList.size() == 1) {
                    tvCurrent.setVisibility(View.GONE);
                } else {
                    tvCurrent.setVisibility(View.VISIBLE);
                    tvCurrent.setText((position + 1) + "/" + imageList.size());
                }
                if (savedInstanceState != null) {
                    position = savedInstanceState.getInt(STATE_POSITION);
                }
                ImagePagerAdapter pageAdapter = new ImagePagerAdapter(imageList, this);
                viewPager.setAdapter(pageAdapter);
                viewPager.setCurrentItem(position);
                viewPager.setOnPageChangeListener(pagerChange);
            }
        }

    }

    private ViewPager.OnPageChangeListener pagerChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int arg) {
            tvCurrent.setText((arg + 1) + "/" + imageList.size());
            position = arg;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSaveDialog.dismiss();
            switch (msg.what) {
                case 0:
//                    showMessage("保存失败");
                    dissmisProgress();
                    break;
                case 1:
                    dissmisProgress();
                    updateGallery(mCurrentPhotoFile.getAbsolutePath());
//                    showMessage("保存成功");
                    break;
            }
        }
    };

    private void dissmisProgress() {
        if (mSaveDialog != null && mSaveDialog.isShowing()) {
            mSaveDialog.dismiss();
            mSaveDialog = null;
        }
    }

    @OnClick(R.id.btn_down)
    void downLoade() {
        String imgUrl = imageList.get(position);
        // 初始化图片保存路径
        String photo_dir = ImageUtils.getFullImageDownPathDir();
        if (StringUtils.isEmpty(photo_dir)) {
            ToastUtil.showShortMessage(this,"存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }

        //获取文件的名称
        String mFileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

        mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
        if (!mCurrentPhotoFile.exists()) {
            mSaveDialog = ProgressDialog.show(ImageActivity.this,
                    "保存图片", "图片正在保存，请稍后...");
//            DownLoadUtil.downLoad(imgUrl, mCurrentPhotoFile.getAbsolutePath(), handler);
        } else {
//            showMessage("您已保存此图片");
        }

    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {

        if (mMediaScanner == null) {
            mMediaScanner = new MediaScanner(this);
        }
        mMediaScanner.scanFile(filePath, "image/jpeg");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<String> images = null;
        private LayoutInflater inflater;
        private Context mContext = null;

        public ImagePagerAdapter(List<String> images,
                                 Activity context) {
            this.images = images;
            inflater = context.getLayoutInflater();
            this.mContext = context;
        }

        @Override
        public int getCount() {
            if (images != null && images.size() > 0)
                return images.size();
            else
                return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_image_pager,
                    container,
                    false);
            PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.photo_display);
            imageView.setOnPhotoTapListener(photoTapListener);
            if (!StringUtils.isEmpty(images.get(position))) {
                Glide.with(mContext)
                        .load(images.get(position))
                        .placeholder(R.drawable.ic_default_image)
                        .thumbnail(0.1f)
                        .error(R.drawable.ic_default_image)
                        .crossFade()
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.ic_default_image);
            }
            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        PhotoViewAttacher.OnPhotoTapListener photoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        };
    }
}
