package com.example.tong.jiaowuxitong.view.views.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.TestUtil;
import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/22.
 */
public class MyRecycAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private RecyclerView recyclerView;
    private CallBack callBack;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    public MyRecycAdapter(Context context, RecyclerView recyclerView, @Nullable CallBack callback) {
        this.recyclerView = recyclerView;
        this.callBack = callback;
        this.context = context;
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setDrawingCacheEnabled(true);
        dDatas = new ArrayList<>();
        subedDatas = new ArrayList<>();
        subingDatas = new ArrayList<>();
        EventBus.getDefault().register(this);
        recyclerView.setAdapter(this);

    }

    private ArrayList<VOStdCrs> datas;
    private ArrayList<Integer> dDatas;
    private ArrayList<Integer> subedDatas;
    private ArrayList<Integer> subingDatas;

//    public ArrayList<VOStdCrs> getdDatas() {
//        return dDatas;
//    }

    public void setDatas(ArrayList<VOStdCrs> datas) {
        if (datas == null || datas.size() == 0) return;
        this.datas = datas;
        dDatas.clear();
        subedDatas.clear();
        subingDatas.clear();
        isSubmiting = false;
        time = 0;
        position = 0;
        hasCalled = false;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.degree_input_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
//        myHolder.position = position;
        myHolder.voStdCrs = null;
        myHolder.mPosition = position;
        if (datas.size() > position) {
            final VOStdCrs voStdCrs = datas.get(position);
            myHolder.name.setText(voStdCrs.getStudentName());
            myHolder.id.setText(String.format(context.getResources().getString(R.string.number), voStdCrs.getStudentId()));

            if (!subedDatas.contains(position)) {
                if (voStdCrs.getDegree() < 0) {
                    myHolder.editText.setText(null);
                } else {
                    myHolder.editText.setText(String.valueOf(voStdCrs.getDegree()));
                }

                myHolder.pb.setVisibility(View.INVISIBLE);
                myHolder.editText.setEnabled(true);
                myHolder.ok.setVisibility(View.INVISIBLE);
            } else {
                myHolder.editText.setText(String.valueOf(voStdCrs.getDegree()));
                myHolder.editText.setEnabled(false);
                myHolder.ok.setVisibility(View.VISIBLE);
            }

            if (subingDatas.contains(position)) {
                myHolder.pb.setVisibility(View.VISIBLE);
                myHolder.ok.setVisibility(View.INVISIBLE);
            }

            myHolder.voStdCrs = voStdCrs;
        }

    }


    @Override
    public long getItemId(int position) {
        return datas.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public static final int SUBMIT_TAG = 301;
    private String submitUrl = GlobalResource.UPDATE_DEGREE_ITEM;

    private int position = 0;
    private MyRecycAdapter.OnFinish onFinish;

    public void setCallBack(OnFinish callBack) {
        this.onFinish = callBack;
    }

    private boolean hasCalled = false;
    private int ddSize = 0;


    private boolean isSubmiting = false;

    public void doSubmit() {
        if (!isSubmiting) {
            isSubmiting = true;
            doSubmit(new Message(MyRecycAdapter.SUBMIT_TAG, null));
        }
    }

    private int time = 0;

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void doSubmit(Message msg) {
        if ((msg != null && msg.tag == SUBMIT_TAG)) {
            if (msg != null && msg.tag == SUBMIT_TAG) {
                if (msg.msg != null) {
                    TestUtil.log("msg", (String) msg.msg);
                    int tmp = msg.extra;
                    ActionState as = GsonUtil.fromJson((String) msg.msg, ActionState.class);
                    if (as.tag == ActionState.ACTION_SUCCESS) {
                        time++;
                        subingDatas.remove(new Integer(dDatas.get(tmp)));
                        subedDatas.add(dDatas.get(tmp));

                        notifyItemChanged(dDatas.get(tmp));


                        if (time == dDatas.size()) {
                            isSubmiting = false;
                            if (onFinish != null)
                                onFinish.onFinish();
                            if (backFinish != null)
                                backFinish.onFinish();
                        }
                        if (time == datas.size()) {

                            isSubmiting = false;
                            if (onFinish != null)
                                onFinish.onAllSubmit();
                        }
                    }
                }
            }
            if (dDatas.size() > position) {
                TestUtil.log("update", position + "");
                subingDatas.add(dDatas.get(position));
                notifyItemChanged(dDatas.get(position));
                NetUtil.asyncPost(GsonUtil.toJson(datas.get(dDatas.get(position))), submitUrl, SUBMIT_TAG, position++);
            }
            if (time == dDatas.size() && onFinish != null && !hasCalled) {
                isSubmiting = false;
                hasCalled = true;
                onFinish.onFinish();
            }
        }
    }

    public int requestBack() {
        return datas.size() - subedDatas.size();
    }

    private OnFinish backFinish;

    public void requestBackForce(OnFinish onFinish) {
        if (onFinish == null) return;
        if (!subingDatas.isEmpty()) {
            this.backFinish = onFinish;
            onFinish.onLoading(1);
        } else {
            onFinish.onAllSubmit();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private boolean ed = false;
        private View view;
        private TextView name;
        private TextView id;
        private EditText editText;
        private ProgressBar pb;
        private ImageView ok;
        private VOStdCrs voStdCrs;
        private int mPosition;

        public MyHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                this.view = itemView;
                view.setTag(this);
                pb = (ProgressBar) view.findViewById(R.id.pb);
                ok = (ImageView) view.findViewById(R.id.ok);
                pb.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.INVISIBLE);
                name = (TextView) view.findViewById(R.id.stdname);
                id = (TextView) view.findViewById(R.id.number);
                editText = (EditText) view.findViewById(R.id.ed_degree);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable v) {
                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                            float degree = Float.valueOf(editText.getText().toString().trim());
                            if (voStdCrs != null) {
                                voStdCrs.setDegree(degree);
                            } else {
                                return;
                            }
                            if (!dDatas.contains(mPosition)) {
                                dDatas.add(mPosition);
                            }
                        } else {
                            if (dDatas.contains(mPosition)) {
                                dDatas.remove(new Integer(mPosition));
                            }
                        }
                    }
                });
            }
        }
    }

    public static interface CallBack {
        void onItemAction(RecyclerView.ViewHolder viewHolder);
    }

    public static interface OnFinish {
        void onFinish();


        void onLoading(int size);

        void onAllSubmit();
    }


}
