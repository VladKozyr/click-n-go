package com.clickandgo.ui.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.clickandgo.R;
import com.clickandgo.ui.auth.LoginFragment;
import com.clickandgo.ui.auth.SignUpFragment;


public class ProfileFragmentAdapter extends FragmentPagerAdapter {

    private final Context context;

    public ProfileFragmentAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WishListFragment();
            case 1:
                return new HistoryFragment();
            case 2:
                return new SupportUsFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.wishlist);
            case 1:
                return context.getResources().getString(R.string.history);
            case 2:
                return context.getResources().getString(R.string.support_us);
            default:
                return null;
        }
    }

}
