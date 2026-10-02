package com.fyp.prosafetyapps;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.drawerlayout.widget.DrawerLayout;

/** Shared appearance and selected destination for the side navigation. */
public final class DrawerUi {
    private DrawerUi() { }
    public static void configure(DrawerLayout drawer, int selectedId) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawer.setScrimColor(0x99162D44);
        int[] rows={R.id.navHome, R.id.navContacts, R.id.navDirections, R.id.navNews,
                R.id.navEmergency, R.id.navProfile, R.id.navSupport, R.id.navShare, R.id.navLogout};
        for (int id:rows) {
            View row=drawer.findViewById(id);
            if (row==null) continue;
            boolean selected=id==selectedId;
            row.setSelected(selected);
            if(row instanceof ViewGroup) {
                ViewGroup group=(ViewGroup)row;
                for(int i=0;i<group.getChildCount();i++) {
                    if(group.getChildAt(i) instanceof TextView) {
                        ((TextView)group.getChildAt(i)).setTypeface(null,
                                selected ? Typeface.BOLD : Typeface.NORMAL);
                        break;
                    }
                }
            }
        }
    }
}
