package uk.org.blackwood.uhresttest;

import java.util.ArrayList;

import uk.org.blackwood.uhresttest.TenantHandler;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;

public class TenantMainActivity extends FragmentActivity implements TenantHandler {
	
	public static final String EXTRA_TENANT = "uk.org.blackwood.uhresttest.TENANT";
	public static final String EXTRA_CON_KEY = "uk.org.blackwood.uhresttest.CON_KEY";
	ViewPager mViewPager;
	TabAdapter mTabAdapt;
	private long tenant_id;
	private long tenant_con_key;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Extract parameters passed in Intent
		Intent intent = getIntent();
		setTenant_id(intent.getLongExtra(EXTRA_TENANT, 0));
		setTenant_con_key(intent.getLongExtra(EXTRA_CON_KEY, 0));
		
		// Setup Views
		mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.tenant_pager);
        setContentView(mViewPager);
        
        // Setup ActionBar
        final ActionBar acBar = getActionBar();
        acBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        acBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        // Build Tabs
        mTabAdapt = new TabAdapter(this, mViewPager);
        mTabAdapt.addTab(acBar.newTab().setText(R.string.tenant_main_activity_title), TenantBasicFragment.class, null);
        mTabAdapt.addTab(acBar.newTab().setText(R.string.tenant_comms_activity_title), TenantCommsFragment.class, null);
        
        if (savedInstanceState != null) {
        	acBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	// TenantHandler Interface methods
	@Override
	public long getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(long tenant_id) {
		this.tenant_id = tenant_id;
	}

	@Override
	public long getTenant_con_key() {
		return tenant_con_key;
	}
	
	public void setTenant_con_key(long tntConKey) {
		this.tenant_con_key = tntConKey;
	}

	public static class TabAdapter
		extends FragmentPagerAdapter
		implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		
		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;
			
			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabAdapter(FragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i=0; i<mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}
		
	}

}
