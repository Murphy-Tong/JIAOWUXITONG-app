package com.example.tong.jiaowuxitong.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;

/**
 * 教师课程详情页面
 */
public class TeacherCourseDetailFragment extends BaseFragment {
    private ViewPager viewPager;
    private VOCourse course;
    private Bundle bundle;

    public TeacherCourseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
            course = (VOCourse) getArguments().getSerializable("course");
            if (course != null && mActivity.getActionBar() != null) {
                mActivity.getActionBar().setTitle(course.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_teacher_course_detail, container, false);
        tabLayout = (TabLayout) (getActivity().findViewById(R.id.tablayout));
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        return v;
    }

    private TabLayout tabLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new PageAdapter(getChildFragmentManager(), viewPager, tabLayout);
    }

    /**
     * viewpager adapter
     */
    private class PageAdapter extends FragmentPagerAdapter {
        private String titles[] = new String[]{"评教结果", "课程成绩"};
        private Fragment[] fragments = new Fragment[2];

        public PageAdapter(FragmentManager fm, ViewPager viewPager, TabLayout tabLayout) {
            super(fm);
            fragments[0] = Fragment.instantiate(mContext, TeacherEvalutionDetailFragment.class.getName(), bundle);
            fragments[1] = Fragment.instantiate(mContext, CourseDegreeFragment.class.getName(), bundle);
            tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
            tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
            viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            viewPager.setAdapter(this);
            tabLayout.setupWithViewPager(viewPager);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
