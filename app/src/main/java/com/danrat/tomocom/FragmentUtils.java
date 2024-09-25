package com.danrat.tomocom;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FragmentUtils {
    /* public static void replaceFragment (FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment frag : fragments) {
            fragmentTransaction.hide(frag);
        }

        if (fragmentManager.findFragmentByTag(fragment.getClass().getName()) != null) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_layout, fragment, fragment.getClass().getName());
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    } */

    /* public static void replaceFragment (FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Log.d("FragmentReplace", "Fragment replaced: " + fragment.getClass().getName());
    } */

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

    public static <T, U> void sortLists(List<T> listToSort, List<U> associatedList, Comparator<? super T> comparator) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listToSort.size(); i++) {
            indices.add(i);
        }

        indices.sort((i1, i2) -> comparator.compare(listToSort.get(i1), listToSort.get(i2)));

        List<T> sortedListToSort = new ArrayList<>();
        List<U> sortedAssociatedList = new ArrayList<>();

        for (int index : indices) {
            sortedListToSort.add(listToSort.get(index));
            sortedAssociatedList.add(associatedList.get(index));
        }

        listToSort.clear();
        listToSort.addAll(sortedListToSort);

        associatedList.clear();
        associatedList.addAll(sortedAssociatedList);
    }
}
