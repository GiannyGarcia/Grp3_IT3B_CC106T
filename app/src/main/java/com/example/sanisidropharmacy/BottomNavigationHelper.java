package com.example.sanisidropharmacy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class BottomNavigationHelper {

    public static void attach(Activity activity) {
        ImageView navHome = activity.findViewById(R.id.nav_home);
        ImageView navCart = activity.findViewById(R.id.nav_cart);
        ImageView navUser = activity.findViewById(R.id.nav_user);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, MainActivity.class));
            });
        }

        if (navCart != null) {
            navCart.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, CartActivity.class));
            });
        }

        if (navUser != null) {
            navUser.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, UserProfileActivity.class));
            });
        }
    }
}
