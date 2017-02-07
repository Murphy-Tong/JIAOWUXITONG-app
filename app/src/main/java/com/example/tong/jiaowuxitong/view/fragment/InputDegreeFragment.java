package com.example.tong.jiaowuxitong.view.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.views.InputDegreeActivity;
import com.example.tong.jiaowuxitong.view.views.adapter.MyRecycAdapter;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/22.
 */
public class InputDegreeFragment extends BaseFragment implements MyRecycAdapter.CallBack {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_input_degree, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDegree(view);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        course = (VOCourse) getActivity().getIntent().getSerializableExtra("course");
        init();
    }

    private void init() {
        adapter = new MyRecycAdapter(mContext, recyclerView, this);
        NetUtil.asyncPost(GsonUtil.toJson(course), GlobalResource.GET_UNDEGREE_STD_OF_COURSE, GET_UNDEGREED_STD_TAG);


    }

    private final int GET_UNDEGREED_STD_TAG = 645;
    private final int GET_UNDEGREED_STD_TAG_CHECK = 655;
    private VOCourse course;
    private final int DEGREE_SUBMIT_TAG = 203;
    private MyRecycAdapter adapter;


    private void submitDegree(View view) {
       /* ArrayList<VOStdCrs> voStdCrses = adapter.getdDatas();
        if (voStdCrses == null || voStdCrses.size() == 0) {
            Snackbar.make(view, "has not degree any one", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
            return;
        }

        NetUtil.asyncPost(GsonUtil.toJson(voStdCrses, new TypeToken<ArrayList<VOStdCrs>>() {
        }.getType()), GlobalResource.host + "sc-updateDegree.do", DEGREE_SUBMIT_TAG);*/
//        InputManager inputManager = (InputManager) mContext.getSystemService(Context.INPUT_SERVICE);
//            fab.setClickable(false);
        adapter.setCallBack(new MyRecycAdapter.OnFinish() {
            @Override
            public void onFinish() {

//                NetUtil.asyncPost(GsonUtil.toJson(course), GlobalResource.host + "sc-getUndgreedStudentOfCourse.do", GET_UNDEGREED_STD_TAG_CHECK);
//                fab.setClickable(true);
            }

            @Override
            public void onLoading(int size) {

            }

            @Override
            public void onAllSubmit() {
//                ViewTool.showAlert(mContext, "to see detail?", "yes", "no", new ViewTool.CallBack() {
//                    @Override
//                    public void onPositiveChoose() {
                NetUtil.asyncPost(GsonUtil.toJson(course), GlobalResource.GET_UNDEGREE_STD_OF_COURSE, GET_UNDEGREED_STD_TAG_CHECK);
//                    }
//                });
            }
        });
        adapter.doSubmit();
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && message.tag == GET_UNDEGREED_STD_TAG) {
            ArrayList<VOStdCrs> ls = GsonUtil.fromJson((String) message.msg, new TypeToken<ArrayList<VOStdCrs>>() {
            }.getType());
            if (ls != null) {
                adapter.setDatas(ls);
            }
        } else if (message != null && message.tag == DEGREE_SUBMIT_TAG) {
            ActionState actionState = GsonUtil.fromJson((String) message.msg, ActionState.class);
            if (actionState != null) {
                if (actionState.tag == ActionState.ACTION_SUCCESS) {
                    ViewTool.showAlert(mContext, "ok");
                } else if (actionState.tag == ActionState.ACTION_FAILED) {
                    ViewTool.showAlert(mContext, "error");
                }
            }
        } else if (message != null && message.tag == GET_UNDEGREED_STD_TAG_CHECK) {
            if (message.msg == null) {
                startNewActivity();
            } else {
                ArrayList<VOStdCrs> ls = GsonUtil.fromJson((String) message.msg, new TypeToken<ArrayList<VOStdCrs>>() {
                }.getType());
                if (ls == null || ls.size() == 0) {
                    startNewActivity();
                } else {
                    ViewTool.showAlert(mContext, ls.size() + mContext.getString(R.string.c_std_not_degree), mContext.getString(R.string.yes_button_text), null, null);
                    adapter.setDatas(ls);
                }
            }
        }
    }

    private void startNewActivity() {
        ((InputDegreeActivity) getActivity()).startNewActivity();
    }


    @Override
    public void onItemAction(RecyclerView.ViewHolder viewHolder) {

    }



    public void onBackPressed(final onBackConfrim onBackConfrim) {
        int i = adapter.requestBack();
        if (i != 0) {
            ViewTool.showAlert(mContext, "还没有全部评分，要现在退出吗？", "yes", "no", new ViewTool.CallBack() {
                @Override
                public void onPositiveChoose() {
                    adapter.requestBackForce(new MyRecycAdapter.OnFinish() {

                        @Override
                        public void onFinish() {
                            if(onBackConfrim!=null){
                                onBackConfrim.onConfrim();
                            }
                        }

                        @Override
                        public void onLoading(int size) {
                            ViewTool.showAlert(mContext, "waiting...", true);
                        }

                        @Override
                        public void onAllSubmit() {

                            if(onBackConfrim!=null){
                                onBackConfrim.onConfrim();
                            }

                        }
                    });
                }
            });

        }
    }

    public interface onBackConfrim{
        void onConfrim();
    }
}
