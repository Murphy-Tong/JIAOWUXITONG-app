package com.example.tong.jiaowuxitong.view.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.fragment.CourseDegreeFragment;
import com.example.tong.jiaowuxitong.view.fragment.InputDegreeFragment;
import com.example.tong.jiaowuxitong.view.views.adapter.MyRecycAdapter;

public class InputDegreeActivity extends BaseActivity implements MyRecycAdapter.CallBack {

    private VOCourse course;
    private InputDegreeFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registEventbus = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_degree);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setUpToorBar(getString(R.string.input_degree));

        course = (VOCourse) getIntent().getSerializableExtra("course");
        getSupportActionBar().setTitle(course.getName());

        currentFragment = (InputDegreeFragment) Fragment.instantiate(this, InputDegreeFragment.class.getName());
        getSupportFragmentManager().beginTransaction().add(R.id.container, currentFragment, InputDegreeFragment.class.getName()).commit();

//        init(course);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (over) {
            Intent intent = new Intent();
            intent.putExtra("result", 1);
            intent.putExtra("course", course);
            setResult(1, intent);
            finishAfterTransition();
        } else if (currentFragment.getClass().getName().equals(InputDegreeFragment.class.getName())) {
            currentFragment.onBackPressed(new InputDegreeFragment.onBackConfrim() {
                @Override
                public void onConfrim() {
                    setResult(0, getIntent());
                    finishAfterTransition();
                }
            });
            return;
        }

        super.onBackPressed();
    }


    private boolean over = false;

    public void startNewActivity() {
        over = true;
        ViewTool.showAlert(this, "to see course detail?", "ok", "no", new ViewTool.CallBack() {
            @Override
            public void onPositiveChoose() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", course);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, CourseDegreeFragment.instantiate(getApplicationContext(), CourseDegreeFragment.class.getName(), bundle)).commit();
            }
        });

    }


    @Override
    public void onItemAction(RecyclerView.ViewHolder viewHolder) {

    }

}
