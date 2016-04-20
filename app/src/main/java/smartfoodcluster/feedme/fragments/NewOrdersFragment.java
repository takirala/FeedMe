package smartfoodcluster.feedme.fragments;

/**
 * Created by Srinivas on 4/18/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartfoodcluster.feedme.R;

public class NewOrdersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_order, container, false);
    }
}
