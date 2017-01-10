package com.spro.mapbaidu.activity.register;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2017/1/3.
 */

public class RegisterPresenter {

    private RegisterView registerView;

    /**
     * 视图的交互怎么处理？
     * 1. RegisterActivity
     * 2. 接口回调
     * 接口的实例化和接口方法的具体实现
     * 让Activity实现视图接口
     */
    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }


    public void register(){
        /**
         * 3个泛型：
         * 3. 1. 启动任务输入的参数类型：请求的地址、上传的数据等类型
         * 3. 2. 后台任务执行的进度：一般是Integer类型(int的包装类)
         * 3. 3. 后台返回的结果类型：比如String类型、Void等
         * 模拟注册，三个泛型都不需要的时候都可以设置成Void
         */
        new AsyncTask<Void, Integer, Void>() {

            // 可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // UI的处理：进度条的展示
                registerView.showProgress();
            }

            // 后台执行，比较耗时的操作都可以放在这里,后台线程，不可以做UI的更新
            @Override
            protected Void doInBackground(Void... params) {

                // 后台线程，做网络请求
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            // 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // 拿到数据，做UI更新
                // 注册成功之后的处理
                registerView.hideProgress();
                registerView.showMessage("注册成功");
                registerView.navigationToHome();
            }
        }.execute();

    }
}
