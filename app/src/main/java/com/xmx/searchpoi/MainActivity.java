package com.xmx.searchpoi;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.xmx.searchpoi.Map.MapActivity;
import com.xmx.searchpoi.Splash.SplashActivity;
import com.xmx.searchpoi.Tools.ActivityBase.BaseNavigationActivity;

public class MainActivity extends BaseNavigationActivity {
    private long mExitTime = 0;

    ViewPager vp;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        startActivity(MapActivity.class);
        startActivity(SplashActivity.class);
        finish();

//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new HomeFragment());
//
//        List<String> titles = new ArrayList<>();
//        titles.add("首页");
//
//        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);
//
//        vp = getViewById(R.id.pager_main);
//        vp.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        NavigationView navigation = getViewById(R.id.nav_view);
//        Menu menu = navigation.getMenu();
//        final MenuItem login = menu.findItem(R.id.nav_logout);
//
//        UserManager.getInstance().autoLogin(new AutoLoginCallback() {
//            @Override
//            public void success(final AVObject user) {
//                login.setTitle(user.getString("nickname") + " 点击注销");
//            }
//
//            @Override
//            public void error(AVException e) {
//                filterException(e);
//            }
//
//            @Override
//            public void error(int error) {
//                switch (error) {
//                    case UserConstants.NOT_LOGGED_IN:
//                        showToast("请在侧边栏中选择登录");
//                        break;
//                    case UserConstants.USERNAME_ERROR:
//                        showToast("请在侧边栏中选择登录");
//                        break;
//                    case UserConstants.CHECKSUM_ERROR:
//                        showToast("登录过期，请在侧边栏中重新登录");
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item);

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                vp.setCurrentItem(0);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
