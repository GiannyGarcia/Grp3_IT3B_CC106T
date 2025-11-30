package com.example.sanisidropharmacy;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class BottomNavHelper {

    public static void setup(Activity activity) {

        ImageView navHome = activity.findViewById(R.id.nav_home);
        ImageView navCart = activity.findViewById(R.id.nav_cart);
        ImageView navUser = activity.findViewById(R.id.nav_user);

        if (navHome == null || navCart == null || navUser == null) return;

        // ⭐ HOME → 4 MAIN CATALOGS (CatalogActivity)
        navHome.setOnClickListener(v -> {
            Intent i = new Intent(activity, CatalogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
        });

        // ⭐ CART
        navCart.setOnClickListener(v -> {
            Intent i = new Intent(activity, CartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
        });

        // ⭐ PROFILE
        navUser.setOnClickListener(v -> {
            Intent i = new Intent(activity, UserProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
        });
    }
}
