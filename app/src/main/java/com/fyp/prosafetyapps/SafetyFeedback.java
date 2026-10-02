package com.fyp.prosafetyapps;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;

/** Brief feedback stays non-blocking; confirmations and errors use SafetyDialog. */
public final class SafetyFeedback {
    private SafetyFeedback() { }
    public static void show(Context context, CharSequence text) {
        if(context instanceof Activity) {
            Activity activity=(Activity)context;
            if(activity.isFinishing()) return;
            View anchor=activity.findViewById(android.R.id.content);
            Snackbar bar=Snackbar.make(anchor,text,Snackbar.LENGTH_LONG);
            bar.setBackgroundTint(ContextCompat.getColor(context,R.color.safety_navy));
            bar.setTextColor(ContextCompat.getColor(context,R.color.white));
            bar.setActionTextColor(0xFFBDE8DF);
            bar.setAction("Dismiss", v -> bar.dismiss());
            bar.getView().setBackgroundResource(R.drawable.safety_hero);
            android.widget.TextView message=bar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            message.setMaxLines(3);
            bar.show();
        } else android.widget.Toast.makeText(context,text,android.widget.Toast.LENGTH_LONG).show();
    }
}
