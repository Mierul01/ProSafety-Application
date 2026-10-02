package com.fyp.prosafetyapps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;

/** Consistent app-owned dialogs with explicit actions and accessible touch targets. */
public final class SafetyDialog extends Dialog {
    private final Builder options;
    private SafetyDialog(Builder options) { super(options.context); this.options=options; }
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.safety_dialog);
        setCancelable(options.cancelable);
        setCanceledOnTouchOutside(options.cancelable);
        ((TextView)findViewById(R.id.dialogTitle)).setText(options.title);
        TextView message=findViewById(R.id.dialogMessage);
        message.setText(options.message);
        message.setVisibility(options.message==null ? View.GONE : View.VISIBLE);
        ((TextView)findViewById(R.id.dialogEyebrow)).setText(options.eyebrow);
        ImageView icon=findViewById(R.id.dialogIcon);
        icon.setImageResource(options.icon);
        int accent=ContextCompat.getColor(getContext(), options.destructive ? R.color.safety_red : R.color.safety_teal);
        icon.setColorFilter(accent);
        findViewById(R.id.dialogIconSurface).setBackgroundResource(options.destructive ?
                R.drawable.dialog_icon_danger : R.drawable.safety_soft);
        FrameLayout body=findViewById(R.id.dialogContent);
        if(options.content!=null) body.addView(options.content);
        else body.setVisibility(View.GONE);
        Button primary=findViewById(R.id.dialogPrimary);
        primary.setText(options.positive);
        primary.setBackgroundResource(options.destructive ? R.drawable.dialog_danger_button : R.drawable.safety_button);
        primary.setOnClickListener(v -> {
            if(options.content!=null && !validInputs(options.content)) return;
            if(options.onPositive!=null) options.onPositive.onClick(this, DialogInterface.BUTTON_POSITIVE);
            dismiss();
        });
        Button secondary=findViewById(R.id.dialogSecondary);
        secondary.setText(options.negative);
        secondary.setVisibility(options.negative==null ? View.GONE : View.VISIBLE);
        secondary.setOnClickListener(v -> { if(options.onNegative!=null) options.onNegative.onClick(this, DialogInterface.BUTTON_NEGATIVE); dismiss(); });
        Window window=getWindow();
        if(window!=null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams params=window.getAttributes();
            params.dimAmount=0.60f;
            window.setAttributes(params);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }
    private boolean validInputs(View view) {
        if(view instanceof android.widget.EditText) {
            android.widget.EditText field=(android.widget.EditText)view;
            if(field.getText().toString().trim().isEmpty()) {
                field.setError("Please enter a value");
                field.requestFocus();
                return false;
            }
        }
        if(view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group=(android.view.ViewGroup)view;
            for(int i=0;i<group.getChildCount();i++) if(!validInputs(group.getChildAt(i))) return false;
        }
        return true;
    }
    @Override public void show() {
        if(options.context instanceof Activity) {
            Activity activity=(Activity)options.context;
            if(activity.isFinishing() || (android.os.Build.VERSION.SDK_INT>=17 && activity.isDestroyed())) return;
        }
        super.show();
        Window window=getWindow();
        if(window!=null) {
            int margin=(int)(24*getContext().getResources().getDisplayMetrics().density);
            int max=(int)(400*getContext().getResources().getDisplayMetrics().density);
            window.setLayout(Math.min(max,getContext().getResources().getDisplayMetrics().widthPixels-margin*2), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
    public static void confirmLogout(Activity activity) {
        logout(activity, () -> {
            FirebaseAuth.getInstance().signOut();
            activity.startActivity(new Intent(activity, LogOutHandler.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
    }
    public static void logout(Context context, Runnable action) {
        new Builder(context).setEyebrow("YOUR ACCOUNT").setIcon(R.drawable.ic_logout).setDestructive(true)
                .setTitle("Log out of ProSafety?")
                .setMessage("You will need to sign in again to access your account. You can come back whenever you are ready.")
                .setNegativeButton("Stay signed in", null)
                .setPositiveButton("Log out", (dialog, which) -> action.run()).show();
    }
    public static final class Builder {
        private final Context context;
        private CharSequence title="ProSafety", message, positive="Got it", negative, eyebrow="PROSAFETY";
        private boolean cancelable=true, destructive=false;
        private int icon=R.drawable.ic_info;
        private View content;
        private DialogInterface.OnClickListener onPositive,onNegative;
        public Builder(Context context) { this.context=context; }
        public Builder setTitle(CharSequence value) { title=value; return this; }
        public Builder setMessage(CharSequence value) { message=value; return this; }
        public Builder setEyebrow(CharSequence value) { eyebrow=value; return this; }
        public Builder setIcon(int value) { icon=value; return this; }
        public Builder setDestructive(boolean value) { destructive=value; return this; }
        public Builder setCancelable(boolean value) { cancelable=value; return this; }
        public Builder setView(View value) { content=value; return this; }
        public Builder setNegativeButton(CharSequence value, DialogInterface.OnClickListener listener) { negative=value; onNegative=listener; return this; }
        public Builder setPositiveButton(CharSequence value, DialogInterface.OnClickListener listener) { positive=value; onPositive=listener; return this; }
        public SafetyDialog create() { return new SafetyDialog(this); }
        public SafetyDialog show() { SafetyDialog dialog=create(); dialog.show(); return dialog; }
    }
}
