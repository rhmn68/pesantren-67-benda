package com.madrasahdigital.walisantri.ppi67benda.view.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SppFragment extends Fragment {

    private final String TAG = SppFragment.class.getSimpleName();
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabs;
    private SharedPrefManager sharedPrefManager;

    public SppFragment() {
        // Required empty public constructor
        Log.w(TAG, " onCreateView - 543212345");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spp, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("SPP");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        viewPager = v.findViewById(R.id.viewPager);
        tabs = v.findViewById(R.id.tabs);

        setUpViewPager();
        return v;
    }

    private void setUpViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new SpecificFragment(), getResources().getString(R.string.title_specific));
        adapter.addFragment(new InvoiceFragment(), getResources().getString(R.string.title_invoice));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(viewPager);

        tabs.setTabTextColors(getResources().getColor(R.color.white),
                getResources().getColor(R.color.silverlow2));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
