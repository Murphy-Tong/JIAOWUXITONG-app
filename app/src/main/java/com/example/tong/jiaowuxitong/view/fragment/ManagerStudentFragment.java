package com.example.tong.jiaowuxitong.view.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VODepartment;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.custom.AnimaTool;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.views.AddActivity;
import com.example.tong.jiaowuxitong.view.views.ManagerAlterStudentActivity;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/29.
 */
public class ManagerStudentFragment extends BaseFragment {

    private static final int ALL_STUDENT_TAG = 412;
    private static final int STUDENT_PAGE = 417;
    private RecyclerView recyclerView;
    private ScrollCallback scrollCallback;

    //    private FloatingActionButton fab;
    private int mTag;
    private String url;
    private int currentpage = 1;
    private int actionTag = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            currentpage = 1;
            mTag = arguments.getInt(LoadingWhat.LOADING_WHAT);
            url = arguments.getString(LoadingWhat.LOAD_URL);
            actionTag = arguments.getInt(LoadingWhat.ACTION);

        }
    }

    private ProgressBar pb;
    private TextView nodatatv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_item_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        //        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setVisibility(View.VISIBLE);
        nodatatv = (TextView) view.findViewById(R.id.n_data);
       /* ViewGroup.MarginLayoutParams marginLayoutParams = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        marginLayoutParams.topMargin = 5;
        recyclerView.setLayoutParams(marginLayoutParams);*/
        return view;
    }

    private MyAdapter myAdapter;
    private GridLayoutManager gridLayoutManager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left = 8;
                    outRect.right = 4;
                } else {
                    outRect.right = 8;
                    outRect.left = 4;
                }
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setMeasurementCacheEnabled(true);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private int state;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                this.state = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollCallback != null && (Math.abs(dy) > Math.abs(dx))) {
                    scrollCallback.onScroll(dx, dy);
                }

                if (!recyclerView.canScrollVertically(1)) {
                    loadMore();

                }
            }
        });
       /* recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                myAdapter.notifyResetRotate();


            }

        });*/
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        NetUtil.asyncPost(url + "?page=" + currentpage++, ALL_STUDENT_TAG + mTag);

    }

    private boolean isLoading = false;

    private void loadMore() {
        ViewTool.showSnack(mContext, recyclerView, mContext.getString(R.string.loading));
        if (!isLoading) {
            isLoading = true;
            NetUtil.asyncPost(url + "?page=" + currentpage++, ALL_STUDENT_TAG + mTag);
        }
    }


    private class MyAdapter extends RecyclerView.Adapter {

        public MyAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(mContext, R.layout.m_s_card, null);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final MyHolder myHolder = (MyHolder) holder;
            myHolder.position = position;
//            myHolder.showText();
            if (rotateHolder != null && !rotateHolder.isShowingText) {
                rotate(rotateHolder);
                rotateHolder = null;
            }

            switch (mTag) {
                case LoadingWhat.STUDENTS:
                    if (lvss != null && lvss.size() > position) {
                        final VOStudent voStudent = lvss.get(position);
                        myHolder.serializable = voStudent;
                        myHolder.name.setText(voStudent.getName());
                        myHolder.id.setText(String.valueOf(voStudent.getId()));
                       /* myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                handlerClick(myHolder);
                            }
                        });*/
                    }
                    break;
                case LoadingWhat.COURSES:
                    if (position < lvsc.size()) {
                        final VOCourse voCourse = lvsc.get(position);
                        myHolder.serializable = voCourse;
                        myHolder.name.setText(voCourse.getName());
                        /*myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerClick(myHolder);
                            }
                        });*/
                    }
                    break;
                case LoadingWhat.TEACHERS:
                    if (position < lvst.size()) {
                        final VOTeacher voTeacher = lvst.get(position);
                        myHolder.serializable = voTeacher;
                        myHolder.name.setText(voTeacher.getName());
                        /*myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerClick(myHolder);
                            }
                        });*/
                    }
                    break;
                case LoadingWhat.DEPTS:
                    if (position < lvsd.size()) {
                        final VODepartment voDepartment = lvsd.get(position);
                        myHolder.serializable = voDepartment;
                        myHolder.name.setText(voDepartment.getName());
                    }
                    break;
            }

            myHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manage(myHolder);
                }
            });
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlerClick(myHolder);
                }
            });

        }

        @Override
        public int getItemCount() {
            switch (mTag) {
                case LoadingWhat.COURSES:
                    return lvsc == null ? 0 : lvsc.size();
                case LoadingWhat.TEACHERS:
                    return lvst == null ? 0 : lvst.size();
                case LoadingWhat.STUDENTS:
                    return lvss == null ? 0 : lvss.size();
                case LoadingWhat.DEPTS:
                    return lvsd == null ? 0 : lvsd.size();

            }
            return 0;
        }

        private List<VOStudent> lvss;
        private List<VOTeacher> lvst;
        private List<VOCourse> lvsc;
        private List<VODepartment> lvsd;

        public void addItem(Serializable serializable) {
            if (serializable == null) return;
            if (mTag == LoadingWhat.STUDENTS && serializable instanceof VOStudent) {
                if (lvss == null) lvss = new ArrayList<>();
                lvss.add(0, (VOStudent) serializable);
            } else if (mTag == LoadingWhat.COURSES && serializable instanceof VOCourse) {
                if (lvsc == null) lvsc = new ArrayList<>();
                lvsc.add(0, (VOCourse) serializable);
            } else if (mTag == LoadingWhat.DEPTS && serializable instanceof VODepartment) {
                if (lvsd == null) lvsd = new ArrayList<>();
                lvsd.add(0, (VODepartment) serializable);
            } else if (mTag == LoadingWhat.TEACHERS && serializable instanceof VOTeacher) {
                if (lvst == null) lvst = new ArrayList<>();
                lvst.add(0, (VOTeacher) serializable);
            }
            notifyItemInserted(0);
        }

        public void setData(List lvs) {
            if (lvs == null) return;
            Log.i("adding", mTag + "");
            switch (mTag) {
                case LoadingWhat.COURSES:
                    if (this.lvsc == null) {
                        this.lvsc = lvs;
                        notifyDataSetChanged();
                    } else {
                        int start = this.lvsc.size();
                        this.lvsc.addAll(lvs);
                        notifyItemRangeInserted(start, lvs.size());
                    }
                    if (lvsc == null || lvsc.size() == 0) {
                        noData();
                    } else {
                        hasData();
                    }
                    break;

                case LoadingWhat.DEPTS:
                    if (this.lvsd == null) {
                        this.lvsd = lvs;
                        notifyDataSetChanged();
                    } else {
                        int start = this.lvsd.size();
                        this.lvsd.addAll(lvs);
                        notifyItemRangeInserted(start, lvs.size());
                    }
                    if (lvsd == null || lvsd.size() == 0) {
                        noData();
                    } else {
                        hasData();
                    }
                    break;
                case LoadingWhat.STUDENTS:
                    if (this.lvss == null) {
                        this.lvss = lvs;
                        notifyDataSetChanged();
                    } else {
                        int start = this.lvss.size();
                        this.lvss.addAll(lvs);
                        notifyItemRangeInserted(start, lvs.size());
                    }

                    if (lvss == null || lvss.size() == 0) {
                        noData();
                    } else {
                        hasData();
                    }
                    break;
                case LoadingWhat.TEACHERS:
                    if (this.lvst == null) {
                        this.lvst = lvs;
                        notifyDataSetChanged();
                    } else {
                        int start = this.lvst.size();
                        this.lvst.addAll(lvs);
                        notifyItemRangeInserted(start, lvs.size());
                    }

                    if (lvst == null || lvst.size() == 0) {
                        noData();
                    } else {
                        hasData();
                    }
                    break;
            }
        }


   /*     @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
           *//* final Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);

            Transition transition = new Explode();
            transition.setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition transition) {
                    return rect;
                }
            });

            transition.setDuration(1000);
            TransitionManager.beginDelayedTransition(recyclerView,transition);
            recyclerView.setAdapter(null);*//*
            if (v != null) {
                Object tag = v.getTag();
                if (tag != null && tag instanceof MyHolder) {
                    MyHolder myHolder = (MyHolder) tag;
                    Intent intent = new Intent(getActivity(), ManagerAlterStudentActivity.class);
                    intent.putExtra("body", myHolder.serializable);
                    startActivityForResult(intent, 3);
                    setEnterTransition(new Explode());

                }
            }
        }*/

        public void notifyItemAtPositionChanged(Serializable serializable) {
            if (ManagerStudentFragment.this.myHolder != null)
                ManagerStudentFragment.this.myHolder.serializable = serializable;
            switch (mTag) {
                case LoadingWhat.COURSES:
                    if (serializable instanceof VOCourse) {
//                        recyclerView.findViewHolderForLayoutPosition(mPosition);
                        if (ManagerStudentFragment.this.myHolder != null) {
                            ManagerStudentFragment.this.myHolder.name.setText(((VOCourse) serializable).getName());
                            lvsc.set(ManagerStudentFragment.this.myHolder.position, (VOCourse) serializable);
                        }
                    }
                    break;
                case LoadingWhat.TEACHERS:
                    if (serializable instanceof VOTeacher) {
                        if (ManagerStudentFragment.this.myHolder != null) {
                            ManagerStudentFragment.this.myHolder.name.setText(((VOTeacher) serializable).getName());
                            lvst.set(ManagerStudentFragment.this.myHolder.position, (VOTeacher) serializable);
                        }
                    }
                    break;
                case LoadingWhat.STUDENTS:
                    if (serializable instanceof VOStudent) {
                        if (ManagerStudentFragment.this.myHolder != null) {
                            ManagerStudentFragment.this.myHolder.name.setText(((VOStudent) serializable).getName());
                            lvss.set(ManagerStudentFragment.this.myHolder.position, (VOStudent) serializable);
                        }
                    }
                    break;
                case LoadingWhat.DEPTS:
                    if (serializable instanceof VODepartment) {
                        if (ManagerStudentFragment.this.myHolder != null) {
                            ManagerStudentFragment.this.myHolder.name.setText(((VODepartment) serializable).getName());
                            lvsd.set(ManagerStudentFragment.this.myHolder.position, (VODepartment) serializable);
                        }
                    }
                    break;
            }
        }

        public void notifyDelete(int position) {
            switch (mTag) {
                case LoadingWhat.COURSES:
                    if (lvsc != null && position < lvsc.size()) {
                        lvsc.remove(position);
                    }
                    break;
                case LoadingWhat.TEACHERS:
                    if (lvst != null && position < lvst.size()) {
                        lvst.remove(position);
                    }
                    break;
                case LoadingWhat.STUDENTS:
                    if (lvss != null && position < lvss.size()) {
                        lvss.remove(position);
                    }
                    break;
            }

            myAdapter.notifyItemRemoved(position);
            myAdapter.notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Serializable serializable = data.getSerializableExtra("body");
            if (serializable != null) {
                myAdapter.notifyItemAtPositionChanged(serializable);
            }
        } else if (resultCode == 404) {
            if (myHolder != null) {
                myAdapter.notifyDelete(myHolder.position);
            }

        }
    }

    private MyHolder myHolder;

    private void manage(MyHolder myHolder) {
        this.myHolder = myHolder;

        if (myHolder != null && myHolder.serializable != null && actionTag != LoadingWhat.FORRESULT && (myHolder.serializable instanceof VOStudent || myHolder.serializable instanceof VOTeacher || myHolder.serializable instanceof VODepartment || myHolder.serializable instanceof VOCourse)) {
            Intent intent = new Intent(mContext, ManagerAlterStudentActivity.class);
            intent.putExtra(LoadingWhat.LOADING_WHAT, mTag);
            intent.putExtra("body", myHolder.serializable);

            startActivityForResult(intent, 1);
        } else if (myHolder != null && myHolder.serializable != null && actionTag != LoadingWhat.FORRESULT) {
            Intent intent = new Intent(mContext, AddActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, mTag);
            bundle.putInt(LoadingWhat.ACTION, LoadingWhat.ALTER);
            bundle.putSerializable("body", myHolder.serializable);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, 1);
        }
    }

    private void noData() {
        nodatatv.setVisibility(View.VISIBLE);
        /*if (mTag == LoadingWhat.COURSES) {
            nodatatv.setText(" no course ");
        } else if (mTag == LoadingWhat.TEACHERS) {
            nodatatv.setText(" no teacher ");
        } else if (mTag == LoadingWhat.STUDENTS) {
            nodatatv.setText(" no student ");
        }*/
    }


    private void hasData() {
        nodatatv.setVisibility(View.INVISIBLE);
    }

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handlerClick(final MyHolder myHolder) {
        if (myHolder.serializable == null) return;
        if (actionTag == LoadingWhat.FORRESULT) {
            ViewTool.showAlert(mContext, mContext.getString(R.string.confrim_choose) + myHolder.serializable.toString() + "?", "yes", "no", new ViewTool.CallBack() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPositiveChoose() {
                    Intent intent = new Intent();
                    intent.putExtra("body", myHolder.serializable);
                    getActivity().setResult(1, intent);
                    finishAfterTransition();
                }
            }, false);
        } else {
            forRotate(myHolder);
        }
    }

    private MyHolder rotateHolder;

    private void forRotate(final MyHolder myHolder) {
        rotate(myHolder);
        if (rotateHolder != null && rotateHolder != myHolder && !rotateHolder.isShowingText) {
            rotate(rotateHolder);
        }
        rotateHolder = myHolder;
    }

    private void rotate(final MyHolder myHolder) {
        if (myHolder == null || myHolder.itemView == null) return;

/*        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(myHolder.itemView,"rotationX",0,90);
        animator1.setDuration(150);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(myHolder.itemView,"rotationX",-90,0);
        animator1.setDuration(150);
        animatorSet.play(animator1).before(animator2);
        animator1.start();*/

        AnimaTool.Objrotate(myHolder.itemView, new AnimaTool.OnAnimate() {
            @Override
            public void onUpdate(float f) {
            }

            @Override
            public void onEnd() {
                if (myHolder.isShowingText) {
                    myHolder.showButton();

                } else {
                    myHolder.showText();
                }
                myHolder.itemView.setRotationX(-90);
                AnimaTool.Objrotate(myHolder.itemView, null, -90, 0, 150);
            }
        }, 0, 90, 150);
    }


    private class MyHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView id;
        private Button button;
        private View view;
        public int position;
        private boolean isShowingText = true;
        private Serializable serializable;

        public MyHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(0, 0);
            }
            itemView.setTag(this);
            layoutParams.width = GlobalResource.SCREEN_WID / 2;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.setLayoutParams(layoutParams);
            name = (TextView) itemView.findViewById(R.id.name);
            button = (Button) itemView.findViewById(R.id.button);
            view = itemView.findViewById(R.id.text);
            id = (TextView) itemView.findViewById(R.id.id);
            button.setTag(this);
        }

        private void showText() {
            button.setVisibility(View.INVISIBLE);
            view.setVisibility(View.VISIBLE);
            isShowingText = true;
        }

        private void showButton() {
            button.setVisibility(View.VISIBLE);
            isShowingText = false;
            view.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && message.tag == ALL_STUDENT_TAG + mTag) {
            isLoading = false;
            if (mTag == LoadingWhat.STUDENTS) {
                Type type = new TypeToken<List<VOStudent>>() {
                }.getType();
                List<VOStudent> lvs = GsonUtil.fromJson((String) message.msg, type);
                fillContent(lvs);
            } else if (mTag == LoadingWhat.COURSES) {
                Type type = new TypeToken<List<VOCourse>>() {
                }.getType();

                List<VOCourse> lvs = GsonUtil.fromJson((String) message.msg, type);
                fillContent(lvs);

            } else if (mTag == LoadingWhat.TEACHERS) {
                Type type = new TypeToken<List<VOTeacher>>() {
                }.getType();

                List<VOTeacher> lvs = GsonUtil.fromJson((String) message.msg, type);
                fillContent(lvs);
            } else if (mTag == LoadingWhat.DEPTS) {
                Type type = new TypeToken<List<VODepartment>>() {
                }.getType();

                List<VODepartment> lvs = GsonUtil.fromJson((String) message.msg, type);
                fillContent(lvs);
            }

        }
    }

    private void fillContent(List lvs) {
        pb.setVisibility(View.INVISIBLE);
        if (lvs == null || lvs.size() == 0) {
            ViewTool.showSnack(null, recyclerView, "好像没有了...");
        }
        myAdapter.setData(lvs);
    }

    public void setScrollCallback(ScrollCallback scrollCallback) {
        this.scrollCallback = scrollCallback;
    }

    public interface ScrollCallback {
        void onScroll(int scrollX, int scrollY);
    }

    public void addItem(Serializable serializable) {
        if (myAdapter != null) {
            myAdapter.addItem(serializable);
        }
    }
}
