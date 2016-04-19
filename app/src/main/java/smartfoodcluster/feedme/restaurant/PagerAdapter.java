package smartfoodcluster.feedme.restaurant;

/**
 * Created by Srinivas on 4/18/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import smartfoodcluster.feedme.fragments.CompletedOrdersFragment;
import smartfoodcluster.feedme.fragments.NewOrdersFragment;
import smartfoodcluster.feedme.fragments.ProcessingOrdersFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewOrdersFragment newOrdersFragment = new NewOrdersFragment();
                return newOrdersFragment;
            case 1:
                ProcessingOrdersFragment processingOrdersFragment = new ProcessingOrdersFragment();
                return processingOrdersFragment;
            case 2:
                CompletedOrdersFragment completedOrdersFragment = new CompletedOrdersFragment();
                return completedOrdersFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
