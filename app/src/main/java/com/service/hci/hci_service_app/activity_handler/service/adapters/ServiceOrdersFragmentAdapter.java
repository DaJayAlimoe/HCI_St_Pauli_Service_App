package com.service.hci.hci_service_app.activity_handler.service.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.fragments.AllOrdersFragment;
import com.service.hci.hci_service_app.activity_handler.service.fragments.SelectedOrdersFragment;

public class ServiceOrdersFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ServiceOrdersFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AllOrdersFragment();
        } else {
            return new SelectedOrdersFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.service_all_orders_name);
            case 1:
                return mContext.getString(R.string.service_selected_orders_name);
            default:
                return null;
        }
    }

}
