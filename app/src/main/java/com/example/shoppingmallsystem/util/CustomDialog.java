package com.example.shoppingmallsystem.util;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {
    IBeforeDismiss iBeforeDismiss;

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss(){
        iBeforeDismiss.onBeforeDismiss();
    }

    // Действительно закрыть диалог
    public void myDismiss() {
        super.dismiss(); // диалог исчезает
    }

    // Выполняется перед закрытием
    interface IBeforeDismiss {
        void onBeforeDismiss();
    }

    public void setBeforeDismiss(IBeforeDismiss iBeforeDismiss) {
        this.iBeforeDismiss = iBeforeDismiss;
    }
}
