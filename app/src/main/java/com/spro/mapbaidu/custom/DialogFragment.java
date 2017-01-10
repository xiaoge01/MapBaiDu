package com.spro.mapbaidu.custom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * 自定义弹出框
 */

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    //先调用 getdialog 方法，再创建 DialogFragment
    public static DialogFragment getdialog(String title, String msg) {

        DialogFragment dialogFragment = new DialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_MESSAGE, msg);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @NonNull
    @Override
    //重写 onCreateDialog 方法
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 拿到数据
        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);
        //建一个弹出框
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("确定",null)
                .create();
    }
}
