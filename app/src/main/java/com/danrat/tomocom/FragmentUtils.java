package com.danrat.tomocom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentUtils {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment existingFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());

        if (existingFragment != null) {
            fragmentTransaction.show(existingFragment);
        } else {
            fragmentTransaction.add(R.id.frame_layout, fragment, fragment.getClass().getName());
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment frag : fragments) {
            if (frag != existingFragment) {
                fragmentTransaction.hide(frag);
            }
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
