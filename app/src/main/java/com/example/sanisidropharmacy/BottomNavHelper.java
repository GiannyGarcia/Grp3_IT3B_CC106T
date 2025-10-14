package com.example.sanisidropharmacy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class BottomNavHelper {

    public static void setup(Activity activity) {
        ImageView navHome = activity.findViewById(R.id.nav_home);
        ImageView navCart = activity.findViewById(R.id.nav_cart);
        ImageView navUser = activity.findViewById(R.id.nav_user);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CatalogActivity.class); // Always go to catalog
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            });
        }

        if (navCart != null) {
            navCart.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            });
        }

        if (navUser != null) {
            navUser.setOnClickListener(v -> {
                Intent intent = new Intent(activity, UserProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            });
        }
    }
}
