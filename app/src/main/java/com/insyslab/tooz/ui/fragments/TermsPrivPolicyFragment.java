package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.eventbus.FragmentState;

public class TermsPrivPolicyFragment extends BaseFragment {

    public static final String TAG = TermsPrivPolicyFragment.class.getSimpleName() + " ==>";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

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
//        if (getArguments() != null) {
//            Bundle bundle = getArguments().getBundle(ARG_PARAM1);
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_terms_priv_policy, container, false);

        updateFragment(new FragmentState(TAG));
//        initView(layout);
        setUpActions();

        return layout;
    }

//    private void initView(View rootView) {
//
//    }

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