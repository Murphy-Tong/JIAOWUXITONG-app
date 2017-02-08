package com.example.tong.jiaowuxitong.view.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.Query;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.fragment.ManagerFragment;
import com.example.tong.jiaowuxitong.view.fragment.SearchResultFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 管理员界面
 */
@ContentView(R.layout.activity_manager)
public class ManagerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ManagerFragment.ScrollCallback {

    private static final int ADD = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;
    private final int QUERY = 996;
    private int mTag;
    private int SEARCH_TAG = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registEventbus = false;
        setShareEnter();
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manager);
        x.view().inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpToorBar(getString(R.string.manage));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        //初始显示std界面
        showStd();

    }

    private int currentAction;
    private Fragment stdFragment;
    private Fragment clsFragment;
    private Fragment dptFragment;
    private Fragment thrFragment;

    private void showStd() {
        if (stdFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.STUDENTS);
            bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALL_STD_PAGE);
            currentFragment = stdFragment = (ManagerFragment) Fragment.instantiate(this, ManagerFragment.class.getName(), bundle);
            ((ManagerFragment) stdFragment).setScrollCallback(this);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!stdFragment.isAdded()) {
            transaction.add(R.id.container, stdFragment);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.show(stdFragment);
        currentFragment = stdFragment;

        mTag = LoadingWhat.STUDENTS;

        currentAction = LoadingWhat.STUDENTS;
        transaction.commit();
    }

    private Fragment tmp;
    private Fragment currentFragment;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                finish = false;
            }
        }
    };
    private boolean finish = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {//关闭drawer
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        //退出search
        if (currentFragment != null && currentFragment.getClass().getName().equals(SearchResultFragment.class.getName())) {
            getSupportFragmentManager().popBackStackImmediate();
            currentFragment = tmp;
            changeTitle();
            return;
        }

        //确认退出程序
        if (finish) {
            this.finish();
        } else {
            finish = true;
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 1000);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 设置title
     */
    private void changeTitle() {
        if (mTag == LoadingWhat.STUDENTS)
            getSupportActionBar().setTitle(R.string.manage_std);
        else if (mTag == LoadingWhat.COURSES)
            getSupportActionBar().setTitle(R.string.manage_cls);
        else if (mTag == LoadingWhat.TEACHERS)
            getSupportActionBar().setTitle(R.string.manage_thr);
        else if (mTag == LoadingWhat.DEPTS)
            getSupportActionBar().setTitle(R.string.manage_dept);
    }

    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m_main_menu, menu);
        MenuItem item = menu.findItem(R.id.m_search);
       /*  searchView = new SearchView(this);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);*/
//        item.setActionView(searchView);
        return true;
    }

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Event(type = View.OnClickListener.class, value = R.id.fab)
    private void onClick(View v) {
        if (v.getId() == R.id.fab) {//添加一个std 或 其他
            Intent intent = new Intent(this, AddActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LoadingWhat.LOADING_WHAT, currentAction);
            bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
            intent.putExtra("bundle", bundle);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getWindow().setSharedElementExitTransition(new ChangeTransform());
                startActivityForResult(intent, ADD, ActivityOptionsCompat.makeSceneTransitionAnimation(this, fab, "fab_share").toBundle());
                setShareExit();
            }else {
                startActivityForResult(intent,ADD);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD && data != null) {//添加成功后
            ((ManagerFragment) currentFragment).addItem(data.getSerializableExtra("body"));
        } else if (requestCode == QUERY && data != null) {//处理查询
            Query query = (Query) data.getSerializableExtra("body");

            if (query == null) return;
//            NetUtil.asyncPost(GsonUtil.toJson(voUser), GlobalResource.QUERY_GET, QUERY);
            getSupportActionBar().setTitle(query.getKey());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("body", query);
            if (searchResultFragment == null) {//初始化search
                searchResultFragment = (SearchResultFragment) Fragment.instantiate(this, SearchResultFragment.class.getName(), bundle);
            }/* else {
                searchResultFragment.getArguments().putSerializable("body", query);
//                searchResultFragment.changeKey(query);
            }*/
            if (searchResultFragment.isVisible()) {//当前就是search界面 改变查询关键字
                searchResultFragment.changeKey(query);
                return;
            } else {//否则设置到bundle里
                searchResultFragment.setArguments(bundle);
            }
            if (!searchResultFragment.isAdded()) {
                fragmentTransaction.addToBackStack(searchResultFragment.getClass().getName());
                fragmentTransaction.add(R.id.middle_container, searchResultFragment);
            }

            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(searchResultFragment);
            tmp = currentFragment;
            currentFragment = searchResultFragment;
            fragmentTransaction.commit();
        }
    }

    private SearchResultFragment searchResultFragment;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.m_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, QUERY);
            overridePendingTransition(0, 0);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//drawer 菜单点击处理
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (currentFragment.getClass().getName().equals(SearchResultFragment.class.getName())) {//当前是search 先退出
            fragmentManager.popBackStackImmediate();
        }
        int id = item.getItemId();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        if (id == R.id.m_class) {
            if (clsFragment == null) {
                bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALL_COURSE_PAGE);
                bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.COURSES);
                clsFragment = (ManagerFragment) Fragment.instantiate(this, ManagerFragment.class.getName(), bundle);
                ((ManagerFragment) clsFragment).setScrollCallback(this);
            }
            if (!clsFragment.isAdded()) {
                transaction.add(R.id.container, clsFragment);
            }
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.show(clsFragment);
            currentFragment = clsFragment;
            currentAction = LoadingWhat.COURSES;
            mTag = LoadingWhat.COURSES;

        } else if (id == R.id.m_dept) {
            if (dptFragment == null) {
                bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_DEPT_PAGE);
                bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.DEPTS);
                dptFragment = (ManagerFragment) Fragment.instantiate(this, ManagerFragment.class.getName(), bundle);
                ((ManagerFragment) dptFragment).setScrollCallback(this);
            }
            if (!dptFragment.isAdded()) {
                transaction.add(R.id.container, dptFragment);
            }
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.show(dptFragment);
            currentFragment = dptFragment;
            currentAction = LoadingWhat.DEPTS;
            mTag = LoadingWhat.DEPTS;

        } else if (id == R.id.m_student) {
            showStd();
        } else if (id == R.id.m_teacher) {
            if (thrFragment == null) {
                bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALLTEACHER_PAGE);
                bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.TEACHERS);
                thrFragment = (ManagerFragment) Fragment.instantiate(this, ManagerFragment.class.getName(), bundle);
                ((ManagerFragment) thrFragment).setScrollCallback(this);
            }
            if (!thrFragment.isAdded()) {
                transaction.add(R.id.container, thrFragment);
            }
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.show(thrFragment);
            currentFragment = thrFragment;
            currentAction = LoadingWhat.TEACHERS;
            mTag = LoadingWhat.TEACHERS;

        }
        transaction.commit();
        showFab();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        changeTitle();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isShowing = true;

    /**
     * 主界面滚动时 处理fab的显示状态  切换fragment时 显示
     * @param scrollX
     * @param scrollY
     */
    @Override
    public void onScroll(int scrollX, int scrollY) {
        if (scrollY > 0) {
            hideFab();
        } else {
            showFab();
        }
    }

    private void showFab() {
        if (!isShowing) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(fab, "scaleY", 0, 1);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(200);
            animatorSet.setStartDelay(100);
            animatorSet.play(objectAnimator).with(objectAnimator1);
            animatorSet.start();
            isShowing = true;
            fab.setClickable(true);
        }
    }

    private void hideFab() {
        if (isShowing) {
            fab.setClickable(false);
            isShowing = false;
            fab.setScaleType(ImageView.ScaleType.CENTER);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab, "scaleX", 1, 0);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(fab, "scaleY", 1, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setStartDelay(100);
            animatorSet.play(objectAnimator).with(objectAnimator1);
            animatorSet.start();
        }
    }
}
