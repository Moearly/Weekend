package com.martn.weekend.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.martn.weekend.fragment.HomeListNewFragment;
import com.martn.weekend.model.TagModel;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2016/3/21 17:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MainPagerNewAdapter extends FragmentPagerAdapter {
    private SparseArray<HomeListNewFragment> fragments;
    private List<TagModel> tags;

    public MainPagerNewAdapter(FragmentManager fm, List<TagModel> list) {
        super(fm);
        this.fragments = new SparseArray();
        this.tags = list;
    }

    public CharSequence getPageTitle(int position) {
        return ((TagModel) this.tags.get(position)).name;
    }

    public int getCount() {
        return this.tags == null ? 0 : this.tags.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (fragment != null) {
            ((HomeListNewFragment) fragment).setTagId(getFragmentId(position));
        }
        return fragment;
    }

    public Fragment getItem(int position) {
        return getFragment(position);
    }

    private HomeListNewFragment getFragment(int position) {
        HomeListNewFragment fragment;
        int tagId = getFragmentId(position);
        if (this.fragments.get(tagId) != null) {
            fragment = fragments.get(tagId);
        } else {
            fragment = HomeListNewFragment.newInstance(position, tagId);
            this.fragments.put(tagId, fragment);
        }
        fragment.setTagId(tagId);
        return fragment;
    }

    private int getFragmentId(int position) {
        return tags.get(position).id;
    }

    public void scrollTop(int position) {
        HomeListNewFragment fragment = fragments.get(getFragmentId(position));
        if (fragment != null) {
            fragment.scrollTop();
        }
    }

    public void onRefresh(List<TagModel> list) {
        for (int i = 0; i < this.fragments.size(); i++) {
            int id = getFragmentId(i);
            if (fragments.get(id) != null) {
                ((HomeListNewFragment) fragments.get(id)).onRefresh();
            }
        }
    }




}