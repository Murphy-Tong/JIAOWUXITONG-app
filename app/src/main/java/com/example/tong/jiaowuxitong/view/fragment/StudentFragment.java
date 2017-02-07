package com.example.tong.jiaowuxitong.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.fragment.adapter.MyItemRecyclerViewAdapter;
import com.example.tong.jiaowuxitong.view.fragment.adapter.OnListFragmentInteractionListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StudentFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int UPDATEITEM = 221;
    private static final int UPDATEITEM_TEACHER = 222;
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StudentFragment newInstance(int columnCount) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    private int USER_TAG;
    private String BODY;
    private String URL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            BODY = bundle.getString("user");
            USER_TAG = bundle.getInt("tag");
            if (USER_TAG == VOUser.STUDENT_TAG)
                URL = GlobalResource.GET_STDCRS_OF_STD;
            else if (USER_TAG == VOUser.TEACHER_TAG)
                URL = GlobalResource.GET_ALL_COURSE_OF_TEACHER;

            /*
            if (!TextUtils.isEmpty(BODY)) {
                Message message = new Message();
                message.tag = Message.CHANGE_TITLE;
                message.msg = student.getName();
                EventBus.getDefault().post(message);
                EventBus.getDefault().register(this);

            }*/
        }
    }

    private MyItemRecyclerViewAdapter adapter;
    private ProgressBar pb;
    private TextView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        nodata = (TextView) view.findViewById(R.id.n_data);
        nodata.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyItemRecyclerViewAdapter(recyclerView, mContext, (OnListFragmentInteractionListener) getActivity(), USER_TAG);
        NetUtil.asyncPost(BODY, URL, Message.COURSE_ALL);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Message message = EventBus.getDefault().getStickyEvent(Message.class);
        if (message != null) {
            if (message.tag == Message.NOTIFYCHANGED) {
                VOStdCrs voStdCrs = (VOStdCrs) message.msg;
                adapter.notifyItemChanged(voStdCrs);
            }

        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        if (msg != null && msg.tag == UPDATEITEM) {
            adapter.updateItem(GsonUtil.fromJson((String) msg.msg, VOCourse.class));
        }
        if (msg != null && msg.tag == UPDATEITEM_TEACHER) {
            adapter.updateItem(GsonUtil.fromJson((String) msg.msg, VOCourse.class));
        }
        if (msg != null && msg.tag == Message.COURSE_ALL) {

            if (USER_TAG == VOUser.STUDENT_TAG) {
                Type type = new TypeToken<ArrayList<VOStdCrs>>() {
                }.getType();
                ArrayList<VOStdCrs> classes = new Gson().fromJson((String) msg.msg, type);
                onData(classes);
                if (adapter != null) {
                    adapter.setData(classes);
                }

            }
            if (USER_TAG == VOUser.TEACHER_TAG) {
                Type type = new TypeToken<ArrayList<VOCourse>>() {
                }.getType();
                ArrayList<VOCourse> classes = new Gson().fromJson((String) msg.msg, type);
                onData(classes);
                if (adapter != null) {
                    adapter.setData(classes);
                }

            }
        }
    }

    private void onData(ArrayList classes) {
        pb.setVisibility(View.INVISIBLE);
        if (classes == null || classes.size() == 0) {
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            NetUtil.asyncPost(GsonUtil.toJson((VOCourse) data.getSerializableExtra("course")), GlobalResource.FIND_COURSE, UPDATEITEM);
        } else if (resultCode == 0) {
            if (data != null) {
                NetUtil.asyncPost(GsonUtil.toJson((VOCourse) data.getSerializableExtra("course")), GlobalResource.FIND_COURSE, UPDATEITEM_TEACHER);
            }
        }
    }
}
