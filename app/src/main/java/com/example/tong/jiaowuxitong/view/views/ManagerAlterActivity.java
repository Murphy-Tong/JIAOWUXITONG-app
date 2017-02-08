package com.example.tong.jiaowuxitong.view.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VODepartment;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Md5Digest;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.custom.AnimaTool;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.fragment.AlertFragment;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 管理员修改信息界面
 * 根据mTag 加载界面
 */
@ContentView(R.layout.activity_manager_alter_student)
public class ManagerAlterActivity extends BaseActivity {

    private static final int UPDATE_STUDENT = 566;
    private static final int GET_ALL_STDCRS = 561;
    private static final int DELETE_STDCRS = 565;
    private static final int ADD_STDCRS = 560;
    private static final int GET_ALL_COURSE = 569;
    private static final int GET_ALL_STD_OF_COURSE = 665;
    private static final int GET_ALL_TEACHER_OF_DEPT = 666;
    private static final int UPDATE_COURSE = 667;
    private static final int UPDATE_TEACHER = 668;
    private static final int UPDATE_DEPT = 669;
    private static final int DELETE_CRS = 670;
    private static final int DELETE_THR = 671;
    private static final int DELETE_STD = 672;
    private static final int ADD_COURSE = 673;
    private static final int ADD_NEW_STDCRS = 674;
    private static final int SELECT_DEPT_FOR_ALTER_THR = 675;
    private static final int SELECT_THR_FOR_ALTER_CRS = 676;
    private static final int DELETE_STD_ENTITY = 677;
    private static final int DELETE_CRS_ENTITY = 678;
    private static final int DELETE_THR_ENTITY = 679;
    @ViewInject(R.id.edit_name)
    private ImageView editName;
    @ViewInject(R.id.m_s_n_c)
    private TextView nocourse;
    @ViewInject(R.id.m_s_pb)
    private ProgressBar pb;
    @ViewInject(R.id.edit_stdcrs)
    private ImageView editStdrcs;
    @ViewInject(R.id.id)
    private TextView id;
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.my_c)
    private TextView my_c;
    @ViewInject(R.id.sex)
    private ImageView sex;
    @ViewInject(R.id.add)
    private TextView add;

    @ViewInject(R.id.dept)
    private View dept;
    @ViewInject(R.id.my_dept)
    private TextView deptname;
    @ViewInject(R.id.edit_dept)
    private ImageView alterDept;
    @ViewInject(R.id.toolBar)
    private Toolbar toolbar;
    @ViewInject(R.id.thr)
    private View thr;
    @ViewInject(R.id.my_thr)
    private TextView myThr;
    @ViewInject(R.id.edit_thr)
    private ImageView alterThr;

    private boolean editMode = false;
    private List addList;
    private List deleteList;
    private boolean edited = false;
    private int mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(toolbar);
        setUpToorBar(getString(R.string.alter_confg));
        addList = new ArrayList<>();
        deleteList = new ArrayList<>();
        initView(getIntent());
    }


    private MyAdapter myAdapter;
    private GridLayoutManager gridLayoutManager;

    /**
     * 根据mtag加载初始化界面
     *
     * @param intent
     */
    private void initView(Intent intent) {
        if (intent != null) {
            mTag = intent.getIntExtra(LoadingWhat.LOADING_WHAT, -1);
            mSerializable = getIntent().getSerializableExtra("body");
            if (mTag == LoadingWhat.STUDENTS) {
                if (intent.getSerializableExtra("body") instanceof VOStudent) {
                    handleStd();
                }
            } else if (mTag == LoadingWhat.TEACHERS) {
                handleThr();
            } else if (mTag == LoadingWhat.COURSES) {
                handlerCrs();
            } else if (mTag == LoadingWhat.DEPTS) {
                handleDept();
            }
        }
    }

    private VOTeacher voTeacher;
    private VOCourse voCourse;
    private VOStudent voStudent;
    private VODepartment voDepartment;
    private Serializable mSerializable;
    private Serializable newSerializable;

    private void handleThr() {
        if (mSerializable != null) {
            voTeacher = (VOTeacher) mSerializable;
            id.setText(String.valueOf(voTeacher.getId()));
            name.setText(voTeacher.getName());
            setSex();
            my_c.setText(R.string.open_crs);
            dept.setVisibility(View.VISIBLE);//不显示部门
            deptname.setText(voTeacher.getDepartmentName());
            nocourse.setText(R.string.no_crs);

            initContentView();

            NetUtil.asyncPost(GsonUtil.toJson(voTeacher), GlobalResource.GET_ALL_COURSE_OF_TEACHER, GET_ALL_COURSE);
        }
    }

    private void handlerCrs() {
        if (mSerializable != null) {
            voCourse = (VOCourse) mSerializable;
            id.setText(String.valueOf(voCourse.getId()));
            name.setText(voCourse.getName());
            my_c.setText(R.string.cls_std);
            thr.setVisibility(View.VISIBLE);
            myThr.setText(voCourse.getTeacherName());
            setSex();
            nocourse.setText(R.string.no_std);
            initContentView();

            NetUtil.asyncPost(GsonUtil.toJson(voCourse), GlobalResource.GET_ALL_STD_OF_COURSE, GET_ALL_STD_OF_COURSE);
        }
    }


    private void handleDept() {
        if (mSerializable != null) {
            voDepartment = (VODepartment) mSerializable;
//            id.setText(String.valueOf(voTeacher.getId()));
            id.setVisibility(View.GONE);
            name.setText(voDepartment.getName());
            setSex();
            my_c.setText(R.string.dept_thr);
            add.setVisibility(View.GONE);//不显示添加按钮
            nocourse.setText("no teacher");
            initContentView();

            NetUtil.asyncPost(GsonUtil.toJson(voDepartment), GlobalResource.GET_ALLTEACHER_OF_DEPT, GET_ALL_TEACHER_OF_DEPT);
        }
    }


    private void initContentView() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        nocourse.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);

        myAdapter = new MyAdapter();
//                myAdapter.setData(voStudent.getStdCrses());
        recyclerView.setAdapter(myAdapter);

    }

    private void handleStd() {
        if (mSerializable != null) {
            voStudent = (VOStudent) mSerializable;
            id.setText(String.valueOf(voStudent.getId()));
            name.setText(voStudent.getName());
            setSex();
            my_c.setText(R.string.chsed_crs);
            nocourse.setText(R.string.no_crs);
            initContentView();

            NetUtil.asyncPost(GsonUtil.toJson(voStudent), GlobalResource.GET_STDCRS_OF_STD, GET_ALL_STDCRS);
        }
    }

    private void setSex() {
        if (mTag == LoadingWhat.STUDENTS) {
            if (voStudent.getGender() % 2 == 1) {
                sex.setImageResource(R.drawable.ic_user_male);
            } else {
                sex.setImageResource(R.drawable.ic_user_female);
            }
        } else if (mTag == LoadingWhat.TEACHERS) {
            if (voTeacher.getGender() % 2 == 1) {
                sex.setImageResource(R.drawable.ic_user_male);
            } else {
                sex.setImageResource(R.drawable.ic_user_female);
            }
        } else if (mTag == LoadingWhat.DEPTS || mTag == LoadingWhat.COURSES) {
            sex.setVisibility(View.GONE);
        }
    }


    /**
     * 重置课程卡片 （不显示删除按钮）
     */
    private void resetStdCrs() {
        int c = gridLayoutManager.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = gridLayoutManager.getChildAt(i);
            if (v != null && v.getTag() != null && v.getTag() instanceof MyHolder) {
                ((MyHolder) v.getTag()).showText();
            }
        }
    }


    @Event(type = View.OnClickListener.class, value = {R.id.edit_dept, R.id.edit_thr, R.id.edit_name, R.id.add, R.id.edit_stdcrs})
    private void onClick(View view) {
        if (view.getId() == R.id.edit_thr) {//修改班级教师
            Intent intent = new Intent(this, ListSelectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.TEACHERS);
            bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
            bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALLTEACHER_PAGE);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, SELECT_THR_FOR_ALTER_CRS);

        }
        if (view.getId() == R.id.edit_dept) {//修改教师所在部门
            Intent intent = new Intent(this, ListSelectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.DEPTS);
            bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
            bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_DEPT_PAGE);
            intent.putExtra("bundle", bundle);

            startActivityForResult(intent, SELECT_DEPT_FOR_ALTER_THR);

        }
        if (view.getId() == R.id.add) {//添加
            if (mTag == LoadingWhat.STUDENTS)
                addNewStdCrs();
            else if (mTag == LoadingWhat.TEACHERS)
                addCourse();
            else if (mTag == LoadingWhat.COURSES) {
                addStudent();
            }
        } else if (view.getId() == R.id.edit_stdcrs && !editMode) {//显示删除按钮
            editMode = true;
            int c = gridLayoutManager.getChildCount();
            for (int i = 0; i < c; i++) {
                final View v = gridLayoutManager.getChildAt(i);
                if (v != null && v.getTag() != null && v.getTag() instanceof MyHolder) {
                    AnimaTool.Objrotate(v, new AnimaTool.OnAnimate() {
                        @Override
                        public void onUpdate(float f) {
                        }

                        @Override
                        public void onEnd() {
                            v.setRotationX(-90);
                            ((MyHolder) v.getTag()).showButton();
                            AnimaTool.Objrotate(v, null, -90, 0, 100);

                        }
                    }, 0, 90, 100);

                }

            }


        } else if (view.getId() == R.id.edit_name) {//弹出对话框 修改基本信息
            View v = View.inflate(this, R.layout.m_s_e_name, null);
            final EditText et = (EditText) v.findViewById(R.id.edit_s);
            final EditText pwd = (EditText) v.findViewById(R.id.edit_s_p);

            final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.e_s_rg);
            radioGroup.clearCheck();
            if (mTag == LoadingWhat.DEPTS || mTag == LoadingWhat.COURSES) {
                pwd.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
            }
            View vw = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (mTag == LoadingWhat.STUDENTS && voStudent != null) {
                et.setHint(voStudent.getName());
                vw = radioGroup.findViewWithTag(String.valueOf(voStudent.getGender() % 2));
            } else if (mTag == LoadingWhat.TEACHERS && voTeacher != null) {
                et.setHint(voTeacher.getName());
                vw = radioGroup.findViewWithTag(String.valueOf(voTeacher.getGender() % 2));

            }

            if (vw != null && vw instanceof RadioButton) {
                ((RadioButton) vw).setChecked(true);
            }
            builder.setView(v);
            builder.setPositiveButton(R.string.yes_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String s = et.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        name.setText(s);
                        if (mTag == LoadingWhat.STUDENTS) {
                            voStudent.setName(s);
                            edited = true;
                        }
                        if (mTag == LoadingWhat.DEPTS) {
                            voDepartment.setName(s);
                            edited = true;
                        }

                        if (mTag == LoadingWhat.COURSES) {
                            voCourse.setName(s);
                            edited = true;
                        }
                        if (mTag == LoadingWhat.TEACHERS) {
                            voTeacher.setName(s);
                            edited = true;
                        }
                    }
                    s = pwd.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        if (mTag == LoadingWhat.STUDENTS) {
                            voStudent.setPassword(new Md5Digest("jwxt", "MD5").encode(s));//加密
                            edited = true;
                        }
                        if (mTag == LoadingWhat.TEACHERS) {
                            voTeacher.setPassword(new Md5Digest("jwxt", "MD5").encode(s));
                            edited = true;
                        }
                    }
                    View v = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (v != null && v.getTag() != null) {
                        int sex = Integer.parseInt((String) v.getTag()) % 2;
                        edited = true;
                        if (mTag == LoadingWhat.STUDENTS) {
                            voStudent.setGender(sex);
                        }

                        if (mTag == LoadingWhat.TEACHERS) {
                            voTeacher.setGender(sex);
                        }
                        setSex();
                    }
                }
            });

            builder.show();
        }
    }

    /**
     * 位班级添加一个学生
     */
    private void addStudent() {
        Intent intent = new Intent(this, ListSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALL_STD_PAGE);
        bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.STUDENTS);
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, ADD_STDCRS);
    }

    /**
     * 位教师新增一门课程
     */
    private void addCourse() {
        Intent intent = new Intent(this, AddActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALL_COURSE_PAGE);
        bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.COURSES);
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        bundle.putSerializable("body", voTeacher);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, ADD_COURSE);
    }

    /**
     * 位学生添加一门课程
     */
    private void addNewStdCrs() {
        Intent intent = new Intent(this, ListSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALL_COURSE_PAGE);
        bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.COURSES);
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, ADD_NEW_STDCRS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_STDCRS || requestCode == ADD_NEW_STDCRS || requestCode == ADD_COURSE) && data != null) {//处理 添加 选择结构
            myAdapter.addItem(data.getSerializableExtra("body"));
            nocourse.setVisibility(View.INVISIBLE);
        } else if (mTag == LoadingWhat.TEACHERS && requestCode == SELECT_DEPT_FOR_ALTER_THR && data != null) {//修改教师的部门信息
            Serializable body = data.getSerializableExtra("body");
            if (voTeacher != null && body != null && body instanceof VODepartment) {
                VODepartment voDepartment = (VODepartment) body;
                voTeacher.setDepartmentId(voDepartment.getId());
                voTeacher.setDepartmentName(voDepartment.getName());
                deptname.setText(voTeacher.getDepartmentName());
                edited = true;
            }
        } else if (mTag == LoadingWhat.COURSES && requestCode == SELECT_THR_FOR_ALTER_CRS && data != null) {//修改班级的教师
            Serializable body = data.getSerializableExtra("body");
            if (voCourse != null && body != null && body instanceof VOTeacher) {
                VOTeacher voTeacher = (VOTeacher) body;
                voCourse.setTeacherId(voTeacher.getId());
                voCourse.setTeacherName(voTeacher.getName());
                voCourse.setDepartmentId(voTeacher.getDepartmentId());
                myThr.setText(voTeacher.getName());
                edited = true;
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {


        public MyAdapter() {

        }

        private List<VOStdCrs> mlvss;//学生
        private List<VOCourse> mlvst;//教师
        private List<VOTeacher> mlvsd;//部门
        private List<VOStudent> mlvsc;//课程

        /**
         * 装载数据
         *
         * @param lvs
         */
        public void setData(List lvs) {
            if (lvs == null) return;
            if (mTag == LoadingWhat.STUDENTS) {
                if (mlvss == null) {
                    this.mlvss = lvs;
                    notifyDataSetChanged();
                } else {
                    int tmp = this.mlvss.size();
                    this.mlvss.addAll(lvs);
                    notifyItemRangeInserted(tmp, lvs.size());
                }
                if (mlvss == null || mlvss.size() == 0) {
                    nodata();
                }
            } else if (LoadingWhat.TEACHERS == mTag) {
                if (mlvst == null) {
                    this.mlvst = lvs;
                    notifyDataSetChanged();
                } else {
                    int tmp = this.mlvst.size();
                    this.mlvst.addAll(lvs);
                    notifyItemRangeInserted(tmp, lvs.size());
                }
                if (mlvst == null || mlvst.size() == 0) {
                    nodata();
                }
            } else if (LoadingWhat.COURSES == mTag) {
                if (mlvsc == null) {
                    this.mlvsc = lvs;
                    notifyDataSetChanged();
                } else {
                    int tmp = this.mlvsc.size();
                    this.mlvsc.addAll(lvs);
                    notifyItemRangeInserted(tmp, lvs.size());
                }
                if (mlvsc == null || mlvsc.size() == 0) {
                    nodata();
                }
            } else if (LoadingWhat.DEPTS == mTag) {
                if (mlvsd == null) {
                    this.mlvsd = lvs;
                    notifyDataSetChanged();
                } else {
                    int tmp = this.mlvsd.size();
                    this.mlvsd.addAll(lvs);
                    notifyItemRangeInserted(tmp, lvs.size());
                }
                if (mlvsd == null || mlvsd.size() == 0) {
                    nodata();
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(ManagerAlterActivity.this, R.layout.m_s_e_card, null);
            return new MyHolder(v);
        }

        /**
         * 按 mTag（当前显示的界面） 显示数据
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (LoadingWhat.COURSES == mTag) {
                if (position < mlvsc.size()) {
                    MyHolder myHolder = (MyHolder) holder;
                    if (myHolder != null) {
                        final VOStudent voStudent = mlvsc.get(position);
                        myHolder.name.setText(voStudent.getName());
                        if (editMode) {
                            myHolder.showButton();
                        } else {
                            myHolder.showText();
                        }
                        myHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mlvsc.remove(voStudent);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());

                                VOStdCrs voStdCrs = new VOStdCrs();
                                voStdCrs.setCourseId(voCourse.getId());
                                voStdCrs.setStudentId(voStudent.getId());

                                if (addList.contains(voStdCrs)) {
                                    addList.remove(voStdCrs);
                                }

                                if (!deleteList.contains(voStdCrs)) {
                                    deleteList.add(voStdCrs);
                                }
                            }
                        });
                    }
                }

            } else if (mTag == LoadingWhat.DEPTS) {
                if (position < mlvsd.size()) {
                    MyHolder myHolder = (MyHolder) holder;
                    if (myHolder != null) {
                        final VOTeacher voTeacher = mlvsd.get(position);
                        myHolder.name.setText(voTeacher.getName());
                        if (editMode) {
                            myHolder.showButton();
                        } else {
                            myHolder.showText();
                        }
                        myHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mlvsd.remove(voTeacher);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());

                                if (!deleteList.contains(voTeacher))
                                    deleteList.add(voTeacher);
                            }
                        });
                    }
                }
            } else if (mTag == LoadingWhat.STUDENTS) {
                if (position < mlvss.size()) {
                    MyHolder myHolder = (MyHolder) holder;
                    if (myHolder != null) {
                        final VOStdCrs voStdCrs = mlvss.get(position);
                        myHolder.name.setText(voStdCrs.getName());
                        if (editMode) {
                            myHolder.showButton();
                        } else {
                            myHolder.showText();
                        }
                        myHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mlvss.remove(voStdCrs);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                                if (addList.contains(voStdCrs)) {
                                    addList.remove(voStdCrs);
                                }
                                if (!deleteList.contains(voStdCrs))
                                    deleteList.add(voStdCrs);
                            }
                        });
                    }
                }
            } else if (LoadingWhat.TEACHERS == mTag) {
                if (position < mlvst.size()) {
                    MyHolder myHolder = (MyHolder) holder;
                    if (myHolder != null) {
                        final VOCourse voCourse = mlvst.get(position);
                        myHolder.name.setText(voCourse.getName());
                        if (editMode) {
                            myHolder.showButton();
                        } else {
                            myHolder.showText();
                        }
                        myHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mlvst.remove(voCourse);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                                /*if (addList.contains(voCourse)) {
                                    addList.remove(voCourse);
                                }*/
                                if (!deleteList.contains(voCourse))
                                    deleteList.add(voCourse);
                            }
                        });
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mTag == LoadingWhat.STUDENTS) {
                return mlvss == null ? 0 : mlvss.size();
            } else if (mTag == LoadingWhat.TEACHERS) {
                return mlvst == null ? 0 : mlvst.size();
            } else if (mTag == LoadingWhat.DEPTS) {
                return mlvsd == null ? 0 : mlvsd.size();
            } else if (mTag == LoadingWhat.COURSES) {
                return mlvsc == null ? 0 : mlvsc.size();
            }
            return 0;
        }


        public void addItem(Serializable body) {
            if (body == null) return;
            if (mTag == LoadingWhat.STUDENTS) {
                if (body instanceof VOCourse) {
                    if (mlvss == null) {
                        mlvss = new ArrayList<>();
                    }
                    int tmp = mlvss.size();
                    VOCourse voCourse = (VOCourse) body;
                    VOStdCrs voStdCrs = new VOStdCrs();
                    voStdCrs.setCourseId(voCourse.getId());
                    if (mlvss == null) mlvss = new ArrayList<>();
                    if (!mlvss.contains(voStdCrs)) {
                        voStdCrs.setName(voCourse.getName());
                        voStdCrs.setStudentId(voStudent.getId());
                        voStdCrs.setThrId(voCourse.getTeacherId());
                        voStdCrs.setDeptId(voCourse.getDepartmentId());
                        mlvss.add(voStdCrs);
                        addList.add(voStdCrs);
                        notifyItemInserted(tmp);
                    }
                }
            } else if (mTag == LoadingWhat.COURSES) {
                if (body instanceof VOStudent) {
                    if (mlvsc == null) mlvsc = new ArrayList<>();
                    if (mlvsc.contains(body)) return;
                    VOStudent student = (VOStudent) body;
                    VOStdCrs voStdCrs = new VOStdCrs();
                    voStdCrs.setCourseId(voCourse.getId());
                    voStdCrs.setStudentId(student.getId());
                    voStdCrs.setThrId(voCourse.getTeacherId());
                    voStdCrs.setDeptId(voCourse.getDepartmentId());
                    addList.add(voStdCrs);
                    mlvsc.add(student);
                    notifyItemInserted(mlvsc.size() - 1);
                }

            } else if (mTag == LoadingWhat.TEACHERS) {
                if (body instanceof VOCourse) {
                    if (mlvst == null) mlvst = new ArrayList<>();
                    if (mlvst.contains(body)) return;
                    mlvst.add((VOCourse) body);
                    notifyItemInserted(mlvst.size() - 1);
                }

            }

        }
    }


    private static class MyHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView delete;
        private boolean isShowingText = true;

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
            delete = (ImageView) itemView.findViewById(R.id.img_delete_this_stdcrs);
            delete.setVisibility(View.INVISIBLE);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        private void showText() {
            delete.setVisibility(View.INVISIBLE);
            isShowingText = true;
        }

        private void showButton() {
            delete.setVisibility(View.VISIBLE);
            isShowingText = false;
        }
    }


    private void nodata() {
        nocourse.setVisibility(View.VISIBLE);
    }

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (editMode) {//若正在显示删除按钮   取消显示
            editMode = false;
            resetStdCrs();
            return;
        }
        if (newSerializable != null) {//设置添加结果  退出
            Intent intent = new Intent();
            intent.putExtra("body", newSerializable);
            setResult(1, intent);
        }
        finishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_s_menu, menu);
        if (menu != null) {
            MenuItem item = menu.getItem(0);
            if (item != null) {
                if (mTag == LoadingWhat.DEPTS)//部门界面没有删除按钮
                    item.setVisible(false);
                else item.setVisible(true);

            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            if (mTag == LoadingWhat.DEPTS) return true;//不删除部门
            ViewTool.showAlert(this, getString(R.string.delete_text), getString(R.string.yes_button_text), getString(R.string.no_button_text), new ViewTool.CallBack() {
                @Override
                public void onPositiveChoose() {//对话框确认删除

                    if (mTag == LoadingWhat.STUDENTS) {
                        NetUtil.asyncPost(GsonUtil.toJson(voStudent), GlobalResource.DELETE_STD, DELETE_STD_ENTITY);
                    } else if (mTag == LoadingWhat.COURSES) {
                        NetUtil.asyncPost(GsonUtil.toJson(voCourse), GlobalResource.DELETE_COURSE, DELETE_CRS_ENTITY);
                    } else if (mTag == LoadingWhat.TEACHERS) {
                        NetUtil.asyncPost(GsonUtil.toJson(voTeacher), GlobalResource.DELETE_TEACHER, DELETE_THR_ENTITY);
                    }

                    ViewTool.getAlertFragmentInstance(getSupportFragmentManager(), ManagerAlterActivity.this, AlertFragment.MODE_WITH_TEXT, false);
                }
            });
        } else if (item.getItemId() == R.id.submit) {//提交修改 弹出对话框显示进度
            if (edited == true || (deleteList != null && deleteList.size() > 0) || (addList != null && addList.size() > 0)) {
                ViewTool.getAlertFragmentInstance(getSupportFragmentManager(), this, AlertFragment.MODE_WITH_TEXT, false);
            }

            if (edited) {//修改过 则提交
                if (mTag == LoadingWhat.STUDENTS) {
                    NetUtil.asyncPost(GsonUtil.toJson(voStudent), GlobalResource.UPDATE_STD, UPDATE_STUDENT);
                } else if (mTag == LoadingWhat.COURSES) {
                    NetUtil.asyncPost(GsonUtil.toJson(voCourse), GlobalResource.UPDATE_COURSE, UPDATE_COURSE);
                } else if (mTag == LoadingWhat.DEPTS) {
                    NetUtil.asyncPost(GsonUtil.toJson(voDepartment), GlobalResource.UPDATE_DEPT, UPDATE_DEPT);
                } else if (mTag == LoadingWhat.TEACHERS) {
                    NetUtil.asyncPost(GsonUtil.toJson(voTeacher), GlobalResource.UPDATE_TEACHER, UPDATE_TEACHER);
                }
            }
            if (deleteList != null && deleteList.size() != 0) {//删除列表不为空 则提交
                Type type = null;
                if (mTag == LoadingWhat.STUDENTS) {
                    type = new TypeToken<List<VOStdCrs>>() {
                    }.getType();
                    NetUtil.asyncPost(GsonUtil.toJson(deleteList, type), GlobalResource.DELETE_STDCRS_LIST, DELETE_STDCRS);
                } else if (mTag == LoadingWhat.COURSES) {
                    type = new TypeToken<List<VOStdCrs>>() {
                    }.getType();
                    NetUtil.asyncPost(GsonUtil.toJson(deleteList, type), GlobalResource.DELETE_STDCRS_LIST, DELETE_STD);
                } else if (mTag == LoadingWhat.DEPTS) {
                    type = new TypeToken<List<VOTeacher>>() {
                    }.getType();
                    NetUtil.asyncPost(GsonUtil.toJson(deleteList, type), GlobalResource.DELETE_TEACHER_LIST, DELETE_THR);
                } else if (mTag == LoadingWhat.TEACHERS) {
                    type = new TypeToken<List<VOCourse>>() {
                    }.getType();
                    NetUtil.asyncPost(GsonUtil.toJson(deleteList, type), GlobalResource.DELETE_COURSE_LIST, DELETE_CRS);
                }
            }
            if (addList != null && addList.size() != 0) {//添加列表不为空 则提交
                Type type = null;
                if (mTag == LoadingWhat.STUDENTS || mTag == LoadingWhat.COURSES) {
                    type = new TypeToken<List<VOStdCrs>>() {
                    }.getType();
                    NetUtil.asyncPost(GsonUtil.toJson(addList, type), GlobalResource.ADD_STDCRS_LIST, ADD_STDCRS);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && (message.tag == DELETE_STD_ENTITY || message.tag == DELETE_CRS_ENTITY || message.tag == DELETE_THR_ENTITY)) {
            ViewTool.setmAlertFragmentText(getString(R.string.delete_succ));//删除成功  提示 然后退出
            ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                @Override
                public void onDismiss() {
                    setResult(404);
                    finishAfterTransition();
                }
            });
            ViewTool.showAlertFragmentText(2000);
            return;
        }
        int dtag = -1;
        int editTag = -1;
        int aTag = -1;//以下项目 有一个操作成功 则提示成功
        if (message != null && message.tag == DELETE_STDCRS) {
            dtag = ViewTool.handlerAction(this, message);
        } else if (message != null && message.tag == DELETE_CRS) {
            dtag = ViewTool.handlerAction(this, message);
        } else if (message != null && message.tag == DELETE_THR) {
            dtag = ViewTool.handlerAction(this, message);
        } else if (message != null && message.tag == DELETE_STD) {
            dtag = ViewTool.handlerAction(this, message);
        } else if (message != null && message.tag == UPDATE_STUDENT) {
            editTag = ViewTool.handlerAction(this, message);
            if (editTag == ActionState.ACTION_SUCCESS) {
                newSerializable = voStudent;
            }
        } else if (message != null && message.tag == UPDATE_TEACHER) {
            editTag = ViewTool.handlerAction(this, message);
            if (editTag == ActionState.ACTION_SUCCESS) {
                newSerializable = voTeacher;
            }
        } else if (message != null && message.tag == UPDATE_DEPT) {
            editTag = ViewTool.handlerAction(this, message);
            if (editTag == ActionState.ACTION_SUCCESS) {
                newSerializable = voDepartment;
            }
        } else if (message != null && message.tag == UPDATE_COURSE) {
            editTag = ViewTool.handlerAction(this, message);
            if (editTag == ActionState.ACTION_SUCCESS) {
                newSerializable = voCourse;
            }
        } else if (message != null && message.tag == GET_ALL_STDCRS) {
            Type type = new TypeToken<List<VOStdCrs>>() {
            }.getType();
            List<VOStdCrs> lvs = GsonUtil.fromJson((String) message.msg, type);
            pb.setVisibility(View.INVISIBLE);
            if (lvs == null || lvs.size() == 0) {
                nocourse.setVisibility(View.VISIBLE);
            } else {
                myAdapter.setData(lvs);
            }
        } else if (message != null && message.tag == GET_ALL_COURSE) {
            Type type = new TypeToken<List<VOCourse>>() {
            }.getType();
            List<VOCourse> lvs = GsonUtil.fromJson((String) message.msg, type);
            pb.setVisibility(View.INVISIBLE);
            if (lvs == null || lvs.size() == 0) {
                nocourse.setVisibility(View.VISIBLE);
            } else {
                myAdapter.setData(lvs);
            }
        } else if (message != null && message.tag == GET_ALL_TEACHER_OF_DEPT) {
            Type type = new TypeToken<List<VOTeacher>>() {
            }.getType();
            List<VOTeacher> lvs = GsonUtil.fromJson((String) message.msg, type);
            pb.setVisibility(View.INVISIBLE);
            if (lvs == null || lvs.size() == 0) {
                nocourse.setVisibility(View.VISIBLE);
            } else {
                myAdapter.setData(lvs);
            }
        } else if (message != null && message.tag == GET_ALL_STD_OF_COURSE) {
            Type type = new TypeToken<List<VOStudent>>() {
            }.getType();
            List<VOStudent> lvs = GsonUtil.fromJson((String) message.msg, type);
            pb.setVisibility(View.INVISIBLE);
            if (lvs == null || lvs.size() == 0) {
                nocourse.setVisibility(View.VISIBLE);
            } else {
                myAdapter.setData(lvs);
            }
        } else if (message != null && message.tag == ADD_STDCRS) {
            aTag = ViewTool.handlerAction(this, message);
        }

        if (aTag == ActionState.ACTION_SUCCESS) {
            addList.clear();
        }
        if (dtag == ActionState.ACTION_SUCCESS) {
            deleteList.clear();
        }
        if (editTag == ActionState.ACTION_SUCCESS) {
            edited = false;
        }

        if (aTag == ActionState.ACTION_SUCCESS || dtag == ActionState.ACTION_SUCCESS || editTag == ActionState.ACTION_SUCCESS) {
            ViewTool.setmAlertFragmentText(getString(R.string.action_succ));
            ViewTool.showAlertFragmentText(2000);
        }
    }

}
