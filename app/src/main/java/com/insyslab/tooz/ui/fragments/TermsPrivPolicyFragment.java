package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;

/**
 * Created by TaNMay on 26/09/16.
 */

public class TermsPrivPolicyFragment extends BaseFragment {

    public static final String TAG = "TermsPrivPolicyFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;

    public TermsPrivPolicyFragment() {

    }

    public static TermsPrivPolicyFragment newInstance(Bundle bundle) {
        TermsPrivPolicyFragment fragment = new TermsPrivPolicyFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments().getBundle(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_terms_priv_policy, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        return layout;
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.ftpp_content);
    }

    private void setUpActions() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        updateFragment(new FragmentState(SettingsFragment.TAG));
        super.onDetach();
    }
}