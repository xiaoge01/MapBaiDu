package com.spro.mapbaidu.activity.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.spro.mapbaidu.R;
import com.spro.mapbaidu.activity.HomeActivity;
import com.spro.mapbaidu.activity.MainActivity;
import com.spro.mapbaidu.commons.ActivityUtils;
import com.spro.mapbaidu.commons.RegexUtils;
import com.spro.mapbaidu.custom.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView{


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.et_Confirm)
    EditText etConfirm;
    @BindView(R.id.btn_Register)
    Button btnRegister;
    private String password;
    private String confirm;
    private String username;
    private ActivityUtils activityUtils;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        //设置标题头 对toolbar设置监听
        setSupportActionBar(toolbar);
        //判断 SupportActionBar 是否已经设置
        if (getSupportActionBar() != null) {
            // 设置箭头 对箭头进行监听 onOptionsItemSelected
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 设置title
            getSupportActionBar().setTitle(R.string.register);
        }
        //对编辑框添加改变监听 addTextChangedListener
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etConfirm.addTextChangedListener(textWatcher);

        activityUtils = new ActivityUtils(this);
    }
    /**
     * 对输入文本内容设置监听 TextWatcher
     */
    private TextWatcher textWatcher = new TextWatcher() {


        //输入文本之前
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //输入文本中
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //输入文本之后
        @Override
        public void afterTextChanged(Editable editable) {
            // 获取文本输入的内容
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            confirm = etConfirm.getText().toString();
            //判断账号密码输入是否规范 （编辑框都不为空并且两次密码相同为 true）
            boolean canregister = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) && password.equals(confirm);
            //按钮设置成可按 true
            btnRegister.setEnabled(canregister);
        }
    };

    /**
     * 对Toolbar中的内容设置监听 必须写在return之前
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // 处理ActionBar的返回箭头事件
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击注册
     */
    @OnClick(R.id.btn_Register)
    public void onClick() {
        //判断账号是否规范
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            //弹出一个自定义对话框

            DialogFragment.getdialog(getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getSupportFragmentManager(), "username");
            return;
        }
        //判断密码是否规范
        if (RegexUtils.verifyUsername(password) != RegexUtils.VERIFY_SUCCESS) {
            //弹出一个自定义对话框
            DialogFragment.getdialog(getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getSupportFragmentManager(), "password");
            return;
        }
        // 进行注册的功能：模拟场景进行注册，业务逻辑
        new RegisterPresenter(this).register();


    }

    // 跳转页面
    public void navigationToHome() {
        activityUtils.startActivity(HomeActivity.class);
        finish();
        // 发送本地广播去关闭页面
        Intent intent = new Intent(MainActivity.MAIN_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // 显示信息
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    // 隐藏进度
    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    // 显示进度
    public void showProgress() {
        mDialog = ProgressDialog.show(this, "注册", "亲，正在注册中，请稍后~");
    }

}
