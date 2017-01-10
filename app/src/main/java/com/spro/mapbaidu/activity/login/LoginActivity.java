package com.spro.mapbaidu.activity.login;

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
import android.widget.TextView;

import com.spro.mapbaidu.R;
import com.spro.mapbaidu.activity.HomeActivity;
import com.spro.mapbaidu.activity.MainActivity;
import com.spro.mapbaidu.commons.ActivityUtils;
import com.spro.mapbaidu.commons.RegexUtils;
import com.spro.mapbaidu.custom.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.btn_Login)
    Button btnLogin;

    private ActivityUtils activityUtils;
    private ProgressDialog mDialog;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.login));
        }
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }
    /**
     * 对输入文本内容设置监听 TextWatcher
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // 获取文本输入的内容
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            //判断账号密码输入是否规范 （编辑框都不为空并且两次密码相同为 true）
            boolean canregister = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
            //按钮设置成可按 true
            btnLogin.setEnabled(canregister);
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

    @OnClick(R.id.btn_Login)
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
        new LoginPresenter(this).login();
    }

    // 跳转页面
    public void navigationToHome() {
        activityUtils.startActivity(HomeActivity.class);
        finish();
        Intent intent=new Intent(MainActivity.MAIN_ACTION);
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
        mDialog = ProgressDialog.show(this, "登录", "亲，正在登录中，请稍后~");
    }


}
