package com.example.tong.jiaowuxitong.view.views;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.Query;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.LoadingWhat;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Call;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @ViewInject(R.id.sv)
    private View rootView;
    @ViewInject(R.id.search_search)
    private ImageView search;
    @ViewInject(R.id.search_back)
    private ImageView back;
    @ViewInject(R.id.search_ed)
    private EditText editText;
    @ViewInject(R.id.listView)
    private ListView listView;
    @ViewInject(R.id.search_layout)
    private View searchlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEnter = false;
        setShareEnter();
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
        setUpToorBar(getString(R.string.search));
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootView, (search.getRight() + search.getLeft()) / 2, (search.getTop() + search.getBottom()) / 2, 0, rootView.getWidth());
                    circularReveal.setDuration(300);
                    circularReveal.setInterpolator(new LinearInterpolator());
                    circularReveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            initView();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    circularReveal.start();

                }
            });
        } else {
            initView();
        }

    }


    private ListAdapter myAdapter;

    private void initView() {
        queryTag = getIntent().getIntExtra(LoadingWhat.LOADING_WHAT, -1);
        myAdapter = new ListAdapter();
        listView.setAdapter(myAdapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    search(s);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().trim();
                if (!TextUtils.isEmpty(s1)) {
                    searchTag++;
                    queryOnLine(s1);
                }
            }
        });
    }

    private void manage(Query query) {
        Intent data = new Intent();
        data.putExtra("body", query);
        setResult(1, data);
        onBackPressed();
    }


    private void search(String s) {
        Query query = new Query(searchTag, queryTag, s);
        query.setId(-1);
        manage(query);
    }

    private int searchTag = 1;
    private int queryTag = -1;
    private final int QUERY = 919;
    private Call call;

    private void queryOnLine(String key) {
        if (call != null && !call.isCanceled())
            call.cancel();
        call = NetUtil.asyncPost(GsonUtil.toJson(new Query(searchTag, queryTag, key)), GlobalResource.QUERY, QUERY);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && message.tag == QUERY) {
            Query query = GsonUtil.fromJson((String) message.msg, Query.class);
            if (query != null && query.getTag() == searchTag) {
                myAdapter.setData(query);
            }
        }
    }

    private class ListAdapter extends BaseAdapter {

        private Query datas;

        public void setData(Query datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas != null ? datas.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView != null && convertView.getTag() instanceof MyHolder) {
                myHolder = (MyHolder) convertView.getTag();
            } else {
                myHolder = new MyHolder();
                myHolder.view.setTag(myHolder);
            }
            VOUser voUser = null;
            VOCourse voCourse = null;
            int tag = -1;
            if (position < datas.mResults.size()) {
                voCourse = datas.mResults.get(position);
//                voUser = voManager;
                tag = LoadingWhat.COURSES;
                myHolder.text.setText(voCourse.getName());
                myHolder.textExtra.setText(R.string.course);

            } else if (position < (datas.mResults.size() + datas.tResults.size())) {
                VOTeacher voTeacher = datas.tResults.get(position - datas.mResults.size());
                myHolder.text.setText(voTeacher.getName());
                voUser = voTeacher;
                tag = LoadingWhat.TEACHERS;
                myHolder.textExtra.setText(R.string.thr);
            } else if (position < datas.size()) {
                VOStudent voStudent = datas.sResults.get(position - (datas.mResults.size() + datas.tResults.size()));
                myHolder.text.setText(voStudent.getName());
                voUser = voStudent;
                tag = LoadingWhat.STUDENTS;
                myHolder.textExtra.setText(R.string.std);
            }
            final VOUser finalVoUser = voUser;
            final VOCourse finVoCourse = voCourse;
            final int fTag = tag;
            myHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = null;
                    Query query = null;
                    if (finVoCourse != null) {
                        query = new Query(searchTag, fTag, finVoCourse.getName());
                        query.setId(finVoCourse.getId());
                    } else if (finalVoUser != null) {
                        query = new Query(searchTag, fTag, finalVoUser.getName());
                        query.setId(finalVoUser.getId());
                    }
                    if (query != null)
                        manage(query);
                }
            });
            return myHolder.view;
        }

        private class MyHolder {
            private TextView text;
            private TextView textExtra;
            private View view;


            public MyHolder() {
                this.view = View.inflate(SearchActivity.this, R.layout.search_item, null);
                ListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, searchlayout.getHeight());
                view.setLayoutParams(layoutParams);
                text = (TextView) view.findViewById(R.id.itemText);
                textExtra = (TextView) view.findViewById(R.id.text_extra);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator reveal = ViewAnimationUtils.createCircularReveal(rootView, (search.getRight() + search.getLeft()) / 2, (search.getTop() + search.getBottom()) / 2, Math.max(rootView.getWidth(), rootView.getHeight()), 0);
            reveal.setDuration(300);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    rootView.setVisibility(View.GONE);
                    SearchActivity.this.finish();
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reveal.start();
        } else {
            finish();
            overridePendingTransition(0, 0);
            super.onBackPressed();
        }
    }
}
