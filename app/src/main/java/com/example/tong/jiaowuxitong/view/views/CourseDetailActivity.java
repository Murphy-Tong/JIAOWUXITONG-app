package com.example.tong.jiaowuxitong.view.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.view.fragment.TeacherCourseDetailFragment;

public class CourseDetailActivity extends BaseActivity {

    private VOCourse voCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        voCourse = (VOCourse) getIntent().getSerializableExtra("course");

        if (voCourse == null) finishAfterTransition();
        if (voCourse != null) {
            setUpToorBar(voCourse.getName());
        }
        getSupportActionBar().setTitle(voCourse.getName());


        Bundle bundle = new Bundle();
        bundle.putSerializable("course", voCourse);
        getSupportFragmentManager().beginTransaction().add(R.id.container, Fragment.instantiate(this, TeacherCourseDetailFragment.class.getName(), bundle)).commit();

    }

}
