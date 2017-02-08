
package com.example.tong.jiaowuxitong.view.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.Query;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.fragment.ManagerFragment;
import com.example.tong.jiaowuxitong.view.fragment.SearchResultFragment;

public class ListSelectActivity extends BaseActivity {

    private int mTag = -1;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registEventbus = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_select);
        setUpToorBar(null);
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getBundleExtra("bundle");
            if (bundle != null)
                mTag = bundle.getInt(LoadingWhat.LOADING_WHAT, -1);
            managerStudentFragment = (ManagerFragment) Fragment.instantiate(this, ManagerFragment.class.getName(), bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container, managerStudentFragment).commit();
        }
    }

    private ManagerFragment managerStudentFragment;
    private SearchResultFragment searchResultFragment;

    private void reload(Query Query) {
        Bundle bundle = new Bundle();
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        bundle.putSerializable("body", Query);
        if (searchResultFragment == null) {
            searchResultFragment = (SearchResultFragment) Fragment.instantiate(this, SearchResultFragment.class.getName());
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!searchResultFragment.isAdded()) {
            transaction.add(R.id.container, searchResultFragment);
        }
        if(searchResultFragment.isVisible()) {
            searchResultFragment.changeKey(Query);
            return;
        }else {
            searchResultFragment.setArguments(bundle);
        }
        transaction.show(searchResultFragment);
        transaction.hide(managerStudentFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTag != LoadingWhat.DEPTS)
            getMenuInflater().inflate(R.menu.m_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.m_search) {

            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(LoadingWhat.LOADING_WHAT, bundle.getInt(LoadingWhat.LOADING_WHAT));
            startActivityForResult(intent, 1);
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Query query = (Query) data.getSerializableExtra("body");
            reload(query);
        }
    }

}
