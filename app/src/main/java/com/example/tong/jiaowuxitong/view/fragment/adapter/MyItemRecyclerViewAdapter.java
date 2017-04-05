package com.example.tong.jiaowuxitong.view.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.view.custom.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 一个recyclerview的adapter
 * 根据USER_TAG 判断要处理的数据 Teacher or Student
 *
 * @return
 */

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private int USER_TAG;

    public MyItemRecyclerViewAdapter(RecyclerView recyclerView, Context context, OnListFragmentInteractionListener listener, int tag) {
        this.context = context;
        this.mListener = listener;
        USER_TAG = tag;
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 根据USER_TAG 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (USER_TAG == VOUser.STUDENT_TAG) {
            holder.voStdCrs = stdcs.get(position);
            holder.mView.setTag(new Integer(holder.voStdCrs.getId()));
            holder.s_teacherName.setText(holder.voStdCrs.getThrName());
            holder.s_courseName.setText(holder.voStdCrs.getName());
            if (holder.voStdCrs.getDegree() < 0) {
                holder.s_degree.setText(R.string.undegree);
            } else {
                holder.s_degree.setText(String.format(context.getResources().getString(R.string.degree), StringUtils.formatFloat(holder.voStdCrs.getDegree(), 2)));
            }
            if (holder.voStdCrs.getEvaDegree() > 0) {
                holder.s_evaluate.setText(context.getResources().getString(R.string.evaluated));
            } else if (holder.voStdCrs.getEvaDegree() <= 0) {
                holder.s_evaluate.setText(context.getResources().getString(R.string.unevaluated));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(stdcs.get(position), holder.mView);
                    }
                }
            });
        } else if (USER_TAG == VOUser.TEACHER_TAG) {
            holder.voCourse = courses.get(position);
            holder.t_courseName.setText(holder.voCourse.getName());
            holder.t_deptName.setText(holder.voCourse.getDepartmentName());
            holder.t_studentCount.setText(String.format(context.getResources().getString(R.string.student_count_in_class), holder.voCourse.getStudentCount()));
            holder.t_undegree.setText(String.format(context.getResources().getString(R.string.undegree_student_count), holder.voCourse.getUnDegreeStudentCount()));
            holder.t_undegree.setTag(new Integer(holder.voCourse.getId()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(holder.voCourse, holder.mView);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (USER_TAG == VOUser.STUDENT_TAG) {
            return stdcs == null ? 0 : stdcs.size();
        }
        if (USER_TAG == VOUser.TEACHER_TAG) {
            return courses == null ? 0 : courses.size();
        }
        return 0;
    }

    private ArrayList<VOStdCrs> stdcs;
    private ArrayList<VOCourse> courses;


    /**
     * 从服务器 取得数据后 填充数据根据USER_TAG
     *
     * @param datas
     */
    public void setData(ArrayList datas) {

        if (USER_TAG == VOUser.STUDENT_TAG) {
            if (stdcs == null)
                this.stdcs = datas;
            else stdcs.addAll(datas);
            notifyDataSetChanged();
        }

        if (USER_TAG == VOUser.TEACHER_TAG) {
            if (courses == null)
                this.courses = datas;
            else courses.addAll(datas);

            notifyDataSetChanged();
        }
    }

    /**
     * 学生评教完成后，该表主页显示的评教状态信息
     *
     * @param voStdCrs
     */
    public void notifyItemChanged(VOStdCrs voStdCrs) {
        if (USER_TAG == VOUser.STUDENT_TAG && stdcs != null) {
            View v = recyclerView.findViewWithTag(new Integer(voStdCrs.getId()));
            if (v != null) {
                TextView tv = (TextView) v.findViewWithTag("s_evaluate");
                if (tv != null && voStdCrs.getEvaDegree() > 0) {
                    tv.setText(R.string.evaed);
                }
                int position = recyclerView.getChildPosition(v);
                if (position < stdcs.size()) {
                    voStdCrs.setThrName(stdcs.get(position).getThrName());
                    voStdCrs.setName(stdcs.get(position).getName());
                    stdcs.set(position, voStdCrs);
                }
            }
        }
    }

    /**
     * 教师对学生打分后更新主页的未打分学生成绩数
     *
     * @param body
     */
    public void updateItem(VOCourse body) {
        if (body == null) return;
        View view = recyclerView.findViewWithTag(new Integer(body.getId()));
        if (view != null) {
            VOCourse v = getFronList(body);
            if (v != null) {
                v.setUnDegreeStudentCount(body.getUnDegreeStudentCount());
                v.setStudentCount(body.getStudentCount());
            }
            TextView textView = (TextView) view;
            textView.setText(String.format(context.getResources().getString(R.string.undegree_student_count), body.getUnDegreeStudentCount()));
        }

    }

    /**
     * 绑定VIEW
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView s_courseName;
        public final TextView t_courseName;
        public final TextView s_teacherName;
        public VOStdCrs voStdCrs;
        public final TextView s_degree;
        public final TextView t_studentCount;
        public final TextView t_undegree;
        public final TextView s_evaluate;
        public final TextView t_deptName;
        public VOCourse voCourse;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            t_courseName = (TextView) view.findViewById(R.id.t_course_name);
            s_courseName = (TextView) view.findViewById(R.id.s_class_name);
            s_degree = (TextView) view.findViewById(R.id.s_degree);
            s_evaluate = (TextView) view.findViewById(R.id.s_evaluation);
            s_evaluate.setTag(this);
            s_evaluate.setTag("s_evaluate");
            s_teacherName = (TextView) view.findViewById(R.id.s_teacher_name);
            t_deptName = (TextView) view.findViewById(R.id.t_dept_name);
            t_undegree = (TextView) view.findViewById(R.id.t_undegree_student_count);
            t_studentCount = (TextView) view.findViewById(R.id.t_student_count);
            if (USER_TAG == VOUser.STUDENT_TAG) {
                view.findViewById(R.id.student_item).setVisibility(View.VISIBLE);
                view.findViewById(R.id.teacher_item).setVisibility(View.INVISIBLE);
            } else {
                view.findViewById(R.id.student_item).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.teacher_item).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取list中id相同的course 更新信息
     *
     * @param v
     * @return
     */
    private VOCourse getFronList(VOCourse v) {
        if (courses != null) {
            Iterator<VOCourse> iterator = courses.iterator();
            VOCourse va;
            while (iterator != null && iterator.hasNext()) {
                va = iterator.next();
                if (va.getId() == v.getId()) {
                    return va;
                }
            }

        }
        return null;
    }
}
