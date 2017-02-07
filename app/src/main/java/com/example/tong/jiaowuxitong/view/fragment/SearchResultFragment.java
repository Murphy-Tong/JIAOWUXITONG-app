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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.Query;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.custom.AnimaTool;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.views.ManagerAlterStudentActivity;

import java.io.Serializable;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Call;

/**
 * Created by TONG on 2017/2/4.
 */
public class SearchResultFragment extends BaseFragment {

    private static final int QUERY_GET_FIRST = 556;
    private Query query;
    private RecyclerView recyclerView;
    private ScrollCallback scrollCallback;
    private int QUERY_GET = 555;
    private int currentpage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentpage = 1;
            Bundle bundle = getArguments();
            query = (Query) bundle.getSerializable("body");
            actionTag = bundle.getInt(LoadingWhat.ACTION);
//            if (mActivity.getActionBar() != null && query != null) {
            mActivity.setTitle(query.getKey());
//                mActivity.getActionBar().setTitle(query.getKey());
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void onloading() {
        if (myAdapter != null) {
            myAdapter.clearData();
        }
        hasData();
        pb.setVisibility(View.VISIBLE);
    }

    private Call call;

    public void load(Query query) {
        if (query != null) {
            if (call != null) {
                call.cancel();
                call = null;
            }
            call = NetUtil.asyncPost(GsonUtil.toJson(query), GlobalResource.QUERY_GET + "?page=" + currentpage++, QUERY_GET_FIRST);
            onloading();
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
        nodatatv = (TextView) view.findViewById(R.id.n_data);
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

        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        load(query);
    }

    private boolean isLoading = false;

    private void loadMore() {
        ViewTool.showSnack(mContext, recyclerView, "loading...");
        if (!isLoading) {
            isLoading = true;
            NetUtil.asyncPost(GsonUtil.toJson(query), GlobalResource.QUERY_GET + "?page=" + currentpage++, QUERY_GET);
        }
    }


    public void changeKey(Query query) {
        this.query = query;
        currentpage = 1;
        myAdapter.clearData();
        load(this.query);
    }


    private class MyAdapter extends RecyclerView.Adapter {

        public MyAdapter() {

        }

        public void clearData() {
            query = null;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(mContext, R.layout.search_result_item, null);
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

            if (position < query.mResults.size()) {
                VOCourse voManager = query.mResults.get(position);
                myHolder.serializable = voManager;
                myHolder.position = position;
                myHolder.name.setText(voManager.getName());
                myHolder.id.setText(String.valueOf(voManager.getId()));
                myHolder.type.setText(R.string.course);
                myHolder.tag = LoadingWhat.COURSES;
            } else if (position < (query.mResults.size() + query.tResults.size())) {
                VOTeacher voTeacher = query.tResults.get(position - query.mResults.size());
                myHolder.serializable = voTeacher;
                myHolder.position = position - query.mResults.size();
                myHolder.name.setText(voTeacher.getName());
                myHolder.id.setText(String.valueOf(voTeacher.getId()));
                myHolder.type.setText(R.string.thr);
                myHolder.tag = LoadingWhat.TEACHERS;
            } else if (position < (query.mResults.size() + query.tResults.size() + query.sResults.size())) {
                VOStudent voStudent = query.sResults.get(position - query.mResults.size() - query.tResults.size());
                myHolder.name.setText(voStudent.getName());
                myHolder.position = position - query.mResults.size() - query.tResults.size();
                myHolder.serializable = voStudent;
                myHolder.id.setText(String.valueOf(voStudent.getId()));
                myHolder.type.setText(R.string.std);
                myHolder.tag = LoadingWhat.STUDENTS;
            }
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlerClick(myHolder);
                }
            });

            myHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manage(myHolder);
                }
            });

        }

        private Query query;

        @Override
        public int getItemCount() {
            return query == null ? 0 : query.size();
        }

        public void changeData(Query query) {
            this.query = query;
            if (this.query == null || this.query.size() == 0) {
                noData();
            } else {
                hasData();
            }
            notifyDataSetChanged();
        }

        public void setData(Query query) {
            int t = this.query == null ? 0 : this.query.size();
            if (this.query == null) {
                this.query = query;
            } else {
                this.query.addAll(query);
            }
            if (this.query == null || this.query.size() == 0) {
                noData();
            } else {
                hasData();
            }
            notifyItemRangeInserted(t, query.size());
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        public void notifyItemAtPositionChanged(Serializable serializable) {
            if (SearchResultFragment.this.myHolder != null)
                SearchResultFragment.this.myHolder.serializable = serializable;
            if (serializable instanceof VOTeacher && query.tResults != null) {
                if (SearchResultFragment.this.myHolder != null) {
                    SearchResultFragment.this.myHolder.name.setText(((VOTeacher) serializable).getName());
                    query.tResults.set(SearchResultFragment.this.myHolder.position, (VOTeacher) serializable);
                }
            } else if (serializable instanceof VOStudent && query.sResults != null) {
                if (SearchResultFragment.this.myHolder != null) {
                    SearchResultFragment.this.myHolder.name.setText(((VOStudent) serializable).getName());
                    query.sResults.set(SearchResultFragment.this.myHolder.position, (VOStudent) serializable);
                }
            } else if (serializable instanceof VOCourse && query.mResults != null) {
                if (SearchResultFragment.this.myHolder != null) {
                    SearchResultFragment.this.myHolder.name.setText(((VOCourse) serializable).getName());
                    query.mResults.set(SearchResultFragment.this.myHolder.position, (VOCourse) serializable);
                }
            }
        }


        private class MyHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView id;
            private Button button;
            private View view;
            public int position;
            private int tag;
            private TextView type;
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
                type = (TextView) itemView.findViewById(R.id.type);
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


        //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void handlerClick(final MyHolder myHolder) {
            if (actionTag == LoadingWhat.FORRESULT) {
                ViewTool.showAlert(mContext, "add this course?", "yes", "no", new ViewTool.CallBack() {
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
            if (myHolder == null) return;
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


    }


    private MyAdapter.MyHolder myHolder;

    private void manage(MyAdapter.MyHolder myHolder) {
        this.myHolder = myHolder;

        if (myHolder != null && myHolder.serializable != null && actionTag != LoadingWhat.FORRESULT && (myHolder.serializable instanceof VOStudent || myHolder.serializable instanceof VOTeacher || myHolder.serializable instanceof VOCourse)) {
            Intent intent = new Intent(mContext, ManagerAlterStudentActivity.class);
            intent.putExtra(LoadingWhat.LOADING_WHAT, myHolder.tag);
            intent.putExtra("body", myHolder.serializable);
            startActivityForResult(intent, 1);
        }
        /* else if (myHolder != null && myHolder.serializable != null && actionTag != LoadingWhat.FORRESULT) {
            Intent intent = new Intent(mContext, AddActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, myHolder.tag);
            bundle.putInt(LoadingWhat.ACTION, LoadingWhat.ALTER);
            bundle.putSerializable("body", myHolder.serializable);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, 1);
        }
*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Serializable serializable = data.getSerializableExtra("body");
            if (serializable != null) {
                myAdapter.notifyItemAtPositionChanged(serializable);
            }
        }
    }

    private void noData() {
        nodatatv.setVisibility(View.VISIBLE);
        //
    }


    private void hasData() {
        nodatatv.setVisibility(View.INVISIBLE);
    }

    private int actionTag;

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && (message.tag == QUERY_GET || message.tag == QUERY_GET_FIRST)) {
            Query query = GsonUtil.fromJson((String) message.msg, Query.class);
            fillContent(query, message.tag);
        }
    }

    private void fillContent(Query query, int tag) {
        pb.setVisibility(View.INVISIBLE);
        if (tag == QUERY_GET) {
            myAdapter.setData(query);
        } else if (tag == QUERY_GET_FIRST) {
            myAdapter.changeData(query);
        }
    }

    public void setScrollCallback(ScrollCallback scrollCallback) {
        this.scrollCallback = scrollCallback;
    }

    public interface ScrollCallback {
        void onScroll(int scrollX, int scrollY);
    }
}
