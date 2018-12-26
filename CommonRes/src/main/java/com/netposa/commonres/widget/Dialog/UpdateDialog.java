package com.netposa.commonres.widget.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netposa.commonres.R;

public class UpdateDialog {
    /**
     * 弹出退出登录对话框.
     *
     * @param message
     * @param context
     * @param isForceUpgrade 是否强制更新
     * @return
     */
    public static Dialog showUpgradeDialog(boolean isForceUpgrade, String message,
                                           Context context,
                                           View.OnClickListener posListener,
                                           View.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogViewStyle);
        AlertDialog dialog = builder.create();
        dialog.show();
        // dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View container = LayoutInflater.from(context).inflate(R.layout.layout_upgrade_dialog, null);
        TextView messageTV = (TextView) container
                .findViewById(R.id.message);
        Button confirmBtn = (Button) container
                .findViewById(R.id.btn_confirm);
        ImageView cancelBtn = (ImageView) container
                .findViewById(R.id.btn_cacel);
        messageTV.setText(message);

        if (isForceUpgrade) {
            cancelBtn.setVisibility(View.GONE);
        }

        confirmBtn.setOnClickListener(posListener);
        cancelBtn.setOnClickListener(negativeListener);
        dialog.setContentView(container);
        dialog.setCancelable(false);
        return dialog;
    }
}
