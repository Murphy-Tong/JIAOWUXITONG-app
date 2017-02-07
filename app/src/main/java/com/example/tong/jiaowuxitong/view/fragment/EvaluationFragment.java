package com.example.tong.jiaowuxitong.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.TestUtil;
import com.example.tong.jiaowuxitong.entity.VOEvaluation;
import com.example.tong.jiaowuxitong.entity.VOOpinion;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.custom.MyViewPager;
import com.example.tong.jiaowuxitong.view.views.CourseEvaluationActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class EvaluationFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    private VOStdCrs voStdCrs;
    private PageViewAdapter pageViewAdapter;

    public EvaluationFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void readFile() {
        String s = IOUtil.readString(mContext, GlobalResource.EVALUATIONS, GlobalResource.EVALUATIONS);
        String tmp[] = IOUtil.readStringSet(mContext, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voStdCrs.getId());

        if (TextUtils.isEmpty(s)) {
            NetUtil.asyncPost(GsonUtil.toJson(voStdCrs), GlobalResource.GET_ALL_EVA_ITEMS, Message.EVA);
        } else {
            Message message = new Message(Message.EVA, s);
            parseContent(message, tmp);
        }
    }


    private MenuItem menuItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_eva, menu);
        menuItem = menu.findItem(R.id.eva_title);
        readFile();
    }

    private ArrayList<String> totals;
    private ArrayList<String> opts;
    private String[] choose;

    private boolean ever_choose_flag = false;


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet2(EvaluationOption evaluationOption) {
        if (clearFlag) return;
        if (evaluationOption != null) {
            TestUtil.log("degreechoose:  ", evaluationOption.position + "");
            choose[evaluationOption.position] = evaluationOption.choose;
            viewPager.setCurrentItem(evaluationOption.position + 1, true);
//            if (!ever_choose_flag) {
            viewPager.setMaxScrollablePosition(evaluationOption.position + 1);
//            } else {
//                viewPager.setMaxScrollablePosition(opts.size());
//            }

            if (menuItem != null) {
                menuItem.setTitle(String.format(mContext.getResources().getString(R.string.eva_c), (evaluationOption.position + 1), opts.size()));
            }


            if (reviewFlag || evaluationOption.position == opts.size() - 1) {

                if (choose.length != 11) {
                    return;
                }
                int c = checkChoose();

                if (c == -1 && !submit) {
                    parseResult();
                } else {
                    reviewFlag = true;
                    viewPager.setCurrentItem(c, true);
                }
            }
        }
    }

    private boolean reviewFlag = false;

    private void parseResult() {

//        IOUtil.writeStringSet(mContext, choose, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voStdCrs.getId(), null);
        VOOpinion voOpinion = new VOOpinion();

        voOpinion.setOpt1(Float.valueOf(choose[0]) * 1.f * Integer.parseInt(totals.get(0)));
        voOpinion.setOpt2(Integer.parseInt(totals.get(1)) * 1.f * Float.valueOf(choose[1]));
        voOpinion.setOpt3(Integer.parseInt(totals.get(2)) * 1.f * Float.valueOf(choose[2]));
        voOpinion.setOpt4(Integer.parseInt(totals.get(3)) * 1.f * Float.valueOf(choose[3]));
        voOpinion.setOpt5(Integer.parseInt(totals.get(4)) * 1.f * Float.valueOf(choose[4]));
        voOpinion.setOpt6(Integer.parseInt(totals.get(5)) * 1.f * Float.valueOf(choose[5]));
        voOpinion.setOpt7(Integer.parseInt(totals.get(6)) * 1.f * Float.valueOf(choose[6]));
        voOpinion.setOpt8(Integer.parseInt(totals.get(7)) * 1.f * Float.valueOf(choose[7]));
        voOpinion.setOpt9(Integer.parseInt(totals.get(8)) * 1.f * Float.valueOf(choose[8]));
        voOpinion.setOpt10(Integer.parseInt(totals.get(9)) * 1.f * Float.valueOf(choose[9]));
        voOpinion.setOpt11(Integer.parseInt(totals.get(10)) * 1.f * Float.valueOf(choose[10]));
        voOpinion.setCourseId(voStdCrs.getCourseId());
        voOpinion.setStdCrsId(voStdCrs.getId());

        Message msg = new Message();
        msg.tag = CourseEvaluationActivity.CHANGE_CONTENT_TAG;
        msg.msg = voOpinion;
        EventBus.getDefault().post(msg);
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        if (msg.tag == Message.EVA) {
            IOUtil.writeString(mContext, (String) msg.msg, GlobalResource.EVALUATIONS, GlobalResource.EVALUATIONS, null);
            parseContent(msg, null);
        }
    }

    private void parseContent(Message msg, @Nullable String[] tmp) {
        Type type = new TypeToken<List<VOEvaluation>>() {
        }.getType();

        List<VOEvaluation> os = GsonUtil.fromJson((String) msg.msg, type);
        opts = new ArrayList<>();
        totals = new ArrayList<>();

        for (VOEvaluation evaluation : os
                ) {
            opts.add(evaluation.getDiscribe());
            totals.add(String.valueOf(evaluation.getTotal()));
        }

        menuItem.setTitle(String.format(mContext.getResources().getString(R.string.eva_c), tmp == null ? 0 : tmp.length, opts.size()));
        if (tmp != null) {
            if (tmp.length == opts.size()) {
                submit = true;
                menuItem.setTitle(R.string.submit);
            }

            viewPager.setMaxScrollablePosition(tmp.length);

            if (tmp.length > totals.size()) {
                choose = new String[tmp.length];
            } else {
                choose = new String[totals.size()];
            }
            int i = 0;
            for (String s : tmp) {
                choose[i++] = s;
            }
        } else {
            choose = new String[totals.size()];
        }

        CourseEvaluationActivity.choose = this.choose;
        pageViewAdapter = new PageViewAdapter(viewPager, mContext, opts, totals, choose, menuItem);
        if (tmp != null) {
            viewPager.setCurrentItem(tmp.length, true);
            viewPager.setMaxScrollablePosition(tmp.length);
        }
    }


    public static boolean submit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voStdCrs = (VOStdCrs) getArguments().getSerializable("obj");

        setHasOptionsMenu(true);

    }

    private MyViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.evaluation_opinion_layout, container, false);
        viewPager = (MyViewPager) v.findViewById(R.id.viewpager);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.eva_title) {
            int canSubmit = checkChoose();
            if (canSubmit == -1) {
//            changeContent(null);
                parseResult();
                return true;
            } else {
                viewPager.setCurrentItem(canSubmit, true);
                return super.onOptionsItemSelected(item);
            }
        } else if (item.getItemId() == R.id.eva_clear) {
            clearChoose();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean clearFlag = false;

    private void clearChoose() {
        for (int i = 0; i < choose.length; i++) {
            choose[i] = null;
        }
        clearFlag = true;
       /* IOUtil.writeStringSet(mContext, choose, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voStdCrs.getId(), null);
*/
        if (voStdCrs != null)
            IOUtil.delete(mContext, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voStdCrs.getId());
        submit = false;
        ever_choose_flag = false;
        viewPager.setCurrentItem(0, true);
        menuItem.setTitle(String.format(mContext.getResources().getString(R.string.eva_c), 0, opts.size()));
        clearRadio();
        viewPager.init();
        clearFlag = false;
    }

    private void clearRadio() {
        if (viewPager != null) {
            for (int i = 0; i < viewPager.getChildCount(); i++) {
                if (viewPager.getChildAt(i) != null) {
                    PageViewAdapter.MyHolder holder = (PageViewAdapter.MyHolder) viewPager.getChildAt(i).getTag();
                    if (holder != null && holder.radioGroup != null) {
                        holder.radioGroup.clearCheck();
                    }
                }
            }
        }

    }

    private int checkChoose() {
        for (int i = 0; i < choose.length; i++) {
            if (TextUtils.isEmpty(choose[i])) return i;
            if (Float.valueOf(choose[i]) < 0 || Float.valueOf(choose[i]) > 1) return i;
        }
        return -1;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static class PageViewAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<View> lv;


        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            View view = lv.get(position);
            if (view != null) {
                MyHolder holder = (MyHolder) view.getTag();
//                if (holder.ckb != null)
//                    holder.ckb.setChecked(false);
                holder.radioGroup.clearCheck();
                if (choose != null && position < choose.length && !TextUtils.isEmpty(choose[position])) {
                    View viewById = holder.radioGroup.findViewWithTag(choose[position]);
                    if (viewById != null) {
                        holder.ckb = (RadioButton) viewById;
                        holder.ckb.setChecked(true);
                    }
                }
                container.addView(view);
                return view;
            } else {
                view = View.inflate(context, R.layout.evaluation_layout, null);
                final MyHolder holder = new MyHolder(view);
                lv.set(position, view);
                holder.tv.setText(opts.get(position));
                if (choose != null && position < choose.length && !TextUtils.isEmpty(choose[position])) {
                    View viewById = holder.radioGroup.findViewWithTag(choose[position]);
                    if (viewById != null) {
                        RadioButton radioButton = (RadioButton) viewById;
                        radioButton.setChecked(true);
                        holder.ckb = radioButton;
                    }
                }


                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    private int lastId;

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (lastId == checkedId) return;
                        lastId = checkedId;
                        EvaluationOption evaluationOption = new EvaluationOption();
                        evaluationOption.position = position;
                        switch (group.getCheckedRadioButtonId()) {
                            case R.id.evluation_vg:
                                evaluationOption.choose = "1.0";
                                holder.ckb = holder.rb1;
                                EventBus.getDefault().post(evaluationOption);
//                                evaluationOption.total = Integer.parseInt(totals.get(position));
                                break;
                            case R.id.evluation_g:
                                evaluationOption.choose = "0.75";
                                holder.ckb = holder.rb2;
                                EventBus.getDefault().post(evaluationOption);
//                                evaluationOption.total = Integer.parseInt(totals.get(position)) * 0.75f;
                                break;
                            case R.id.evluation_bg:
                                holder.ckb = holder.rb3;
                                evaluationOption.choose = "0.5";
                                EventBus.getDefault().post(evaluationOption);
//                                evaluationOption.total = Integer.parseInt(totals.get(position)) * 0.5f;
                                break;
                            case R.id.evluation_ng:
                                holder.ckb = holder.rb4;
                                evaluationOption.choose = "0.25";
                                EventBus.getDefault().post(evaluationOption);
//                                evaluationOption.total = Integer.parseInt(totals.get(position)) * 0.25f;
                                break;
                            case R.id.evluation_vng:
                                holder.ckb = holder.rb5;
                                evaluationOption.choose = "0.0";
                                EventBus.getDefault().post(evaluationOption);
//                                evaluationOption.total = Integer.parseInt(totals.get(position)) * 0f;
                                break;
                        }
                    }
                });

            }
            container.addView(view);
            return view;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return totals == null ? 0 : totals.size();
        }


        private Context context;
        private MenuItem menuItem;

        public PageViewAdapter(ViewPager viewPager, Context context, ArrayList<String> cs, ArrayList<String> totals, String[] degrees, MenuItem menu) {

            this.context = context;
            lv = new ArrayList<>(totals.size());
            for (int i = 0; i < totals.size(); i++) {
                lv.add(null);
            }
            this.menuItem = menu;
            this.opts = cs;
            this.totals = totals;
            this.choose = degrees;
//            viewPager.scroll
//            viewPager.setDrawingCacheEnabled(true);
            viewPager.setOffscreenPageLimit(opts.size());
//            viewPager.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
            viewPager.setAdapter(this);
            viewPager.setOnPageChangeListener(this);

        }


        private String[] choose;
        private List<String> totals;
        private List<String> opts;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {

            if (menuItem != null && !EvaluationFragment.submit) {
                menuItem.setTitle(String.format(context.getResources().getString(R.string.eva_c), (position), opts.size()));
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


        private static class MyHolder {
            public TextView tv;
            public RadioButton rb1;
            public RadioButton rb2;
            public RadioButton rb3;
            public RadioButton rb4;
            public RadioButton rb5;
            public RadioButton ckb;
            public View view;
            public RadioGroup radioGroup;

            public MyHolder(View v) {
                this.view = v;
                v.setTag(this);
                tv = (TextView) view.findViewById(R.id.evaluation_text);
                radioGroup = (RadioGroup) view.findViewById(R.id.evaluation_radiogroup);
                rb1 = (RadioButton) view.findViewWithTag("1.0");
                rb2 = (RadioButton) view.findViewWithTag("0.75");
                rb3 = (RadioButton) view.findViewWithTag("0.5");
                rb4 = (RadioButton) view.findViewWithTag("0.25");
                rb5 = (RadioButton) view.findViewWithTag("0.0");


            }

        }


/*

        public void setDatas(List<String> opts, List<String> totals) {
            this.opts = opts;
            this.totals = totals;
            notifyDataSetChanged();
        }
*/
    }

    private static class EvaluationOption {
        public int position;
        public float total;
        public String choose;
    }

}
