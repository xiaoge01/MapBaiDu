package com.spro.mapbaidu.fragment;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileDescriptor;

/**
 * 使用 MediaPlay 媒体播放器播放
 * 视屏播放 SurfaceView 与 TextureView
 * 这里使用 TextureView ,fragment全屏显示播放视频的控件
 *
 * 这个Fragment 是直接展示视屏View 不是布局View
 */

public class PlayMp4Fragment extends Fragment implements TextureView.SurfaceTextureListener{

    private MediaPlayer mp;
    private TextureView textureView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 初始化MediaPlay
         mp = new MediaPlayer();
        //初始化 TextureView 视频控件
        textureView = new TextureView(getContext());
        return textureView;
    }

    /**
     * 手动重写 onViewCreated 方法
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置监听：因为播放显示内容需要SurfaceTexture控制器，所以设置监听判断SurfaceTexture有没有准备好或有没有变化等
        textureView.setSurfaceTextureListener(this);
    }
    //已经准备好了
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, int i, int i1) {

        try {
            //获取Assets文件夹下的MP4 资源
            AssetFileDescriptor fd = getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fileDescriptor = fd.getFileDescriptor();
            mp.reset();
            //设置资源(需要FileDescriptor资源)
            mp.setDataSource(fileDescriptor,fd.getStartOffset(),fd.getLength());
            mp.prepareAsync();//准备
            mp.setLooping(true);//是否循环播放
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //需要 SurfaceTexture与mp 建立连接
                    Surface surface=new Surface(surfaceTexture);
                    mp.setSurface(surface);//建立mp与控制器之间的联系
                    mp.start();//播放
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAL","异常");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }
    //销毁
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


}
