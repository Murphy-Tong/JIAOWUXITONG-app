package com.example.tong.jiaowuxitong.view.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOEvaluation;
import com.example.tong.jiaowuxitong.entity.VOOpinion;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.custom.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 教师评教详情页面
 */
public class TeacherEvalutionDetailFragment extends BaseFragment {


    private VOOpinion voOpinion;

    public TeacherEvalutionDetailFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private VOCourse course;

    private final int EVA_GET = 315;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (VOCourse) getArguments().getSerializable("course");
        }
    }


    /**
     * 填充数据
     *
     * @param message
     */
    private void fillCintent(Message message) {
        Type type = new TypeToken<List<VOEvaluation>>() {
        }.getType();
        List<VOEvaluation> os = GsonUtil.fromJson((String) message.msg, type);
        if (os != null) {
            opts = new ArrayList<>();

            for (VOEvaluation evaluation : os
                    ) {
                opts.add(evaluation.getDiscribe());
            }
        }
        //opts 和 opinion都不为空时
        if (opts != null && voOpinion != null) {
            allOk();
        }


    }


    /**
     * 初始化页面
     */
    private void initView() {
        TextView textView = (TextView) getActivity().findViewById(R.id.stdcount);
        textView.setText(String.format(getResources().getString(R.string.t_eva_std_count), course.getStudentCount(), voOpinion.getUnEvaedStdCount()));
        recyclerView.addItemDecoration(new MyDecorate(10));
        new RecycAdapter(recyclerView);
    }


    private ArrayList<String> opts;

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && message.tag == EVA_GET) {
            IOUtil.writeString(mContext, (String) message.msg, GlobalResource.EVALUATIONS, GlobalResource.EVALUATIONS, null);
            fillCintent(message);
        } else if (message != null && message.tag == EVA_DEGREE_GET) {
            pb.setVisibility(View.INVISIBLE);
            voOpinion = GsonUtil.fromJson((String) message.msg, VOOpinion.class);
            if (opts != null && voOpinion != null) {
                allOk();
            }
        }
    }

    private void allOk() {
        initView();
    }

    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_evalution_detail, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        pb.setVisibility(View.INVISIBLE);
        return view;
    }

    private final int EVA_DEGREE_GET = 540;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String s = IOUtil.readString(mContext, GlobalResource.EVALUATIONS, GlobalResource.EVALUATIONS);
        NetUtil.asyncPost(GsonUtil.toJson(course), GlobalResource.GET_EVA_DETAIL_OF_COURSE, EVA_DEGREE_GET);
        if (TextUtils.isEmpty(s)) {
            NetUtil.asyncPost(GlobalResource.GET_ALL_EVA_ITEMS, EVA_GET);
        } else {
            Message message = new Message(Message.EVA, s);
            fillCintent(message);
        }
    }


    private class MyDecorate extends RecyclerView.ItemDecoration {
        private int space;

        public MyDecorate(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = space;
            outRect.right = space;
            outRect.left = space;
        }
    }

    private class RecycAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public RecycAdapter(RecyclerView recyclerView) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
            recyclerView.setAdapter(this);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.teacher_valuation_detail_item, null);
            return new MyHolder(view);
        }

        MyHolder myHolder;

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            myHolder = (MyHolder) holder;
            if (opts != null && position < opts.size())
                myHolder.evaItem.setText(opts.get(position));
            if (voOpinion != null) {
                myHolder.evaItem1.setText(String.format(getResources().getString(R.string.t_degree_total), StringUtils.formatFloat(voOpinion.getTotal(position), 1)));
                myHolder.evaItem2.setText(String.format(getResources().getString(R.string.t_degree_dept_get), StringUtils.formatFloat(voOpinion.getAv(position), 2)));
                myHolder.evaItem3.setText(String.format(getResources().getString(R.string.t_degree_get), StringUtils.formatFloat(voOpinion.getOpt(position), 2)));
            }
        }

        @Override
        public int getItemCount() {
            return opts == null ? 0 : opts.size();
        }

        private class MyHolder extends RecyclerView.ViewHolder {
            private TextView evaItem;
            private TextView evaItem1;
            private TextView evaItem2;
            private TextView evaItem3;

            public MyHolder(View itemView) {
                super(itemView);
                evaItem = (TextView) itemView.findViewById(R.id.eva_item);
                evaItem1 = (TextView) itemView.findViewById(R.id.eva_sub_item1);
                evaItem2 = (TextView) itemView.findViewById(R.id.eva_sub_item2);
                evaItem3 = (TextView) itemView.findViewById(R.id.eva_sub_item3);
            }
        }

    }
}
