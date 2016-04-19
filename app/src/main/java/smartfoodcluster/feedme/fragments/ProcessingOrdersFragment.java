package smartfoodcluster.feedme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartfoodcluster.feedme.R;

/**
 * Created by Srinivas on 4/18/2016.
 */

public class ProcessingOrdersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_processing_order, container, false);
    }
}