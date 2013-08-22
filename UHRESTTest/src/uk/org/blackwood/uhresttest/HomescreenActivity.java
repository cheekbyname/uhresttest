package uk.org.blackwood.uhresttest;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import uk.org.blackwood.uhresttest.contentprovider.UHRESTTestContentProvider;

public class HomescreenActivity extends Activity 
	implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>, OnQueryTextListener {

    public static final String SCHEME = "content";
	public static final String AUTHORITY = "uk.org.blackwood.uhresttest.contentprovider";
	public static final String PATH = "uhresttest/Housing/Tenants";
	public static final String EXTRA_TENANT = "uk.org.blackwood.uhresttest.TENANT";
	// If using an Exchange account
	public static final String EXCHANGE_ACCOUNT_NAME = "AlexC@mbha.org.uk";
	public static final String EXCHANGE_ACCOUNT_TYPE = "com.android.exchange";
	// If using a Blackwood-specific account
    public static final String ACCOUNT = "dummyaccount";
	public static final String ACCOUNT_TYPE = "uk.org.blackwood";
	
	// Fields
    Uri myUri;
    ContentResolver myResolve;
    public static Account exAcct;
    private SimpleCursorAdapter cAdapt;
//    DataObserver watcher = null;
    AccountManager am = null;
    String curSearch = null;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Establish views, menus etc.
        setContentView(R.layout.activity_homescreen);
//        registerForContextMenu(this.getListView());

        // Intercept search intents
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	curSearch = intent.getStringExtra(SearchManager.QUERY);
        }
/*        
        // Instantiate Observer for Data Syncing of housing_tenants table
        myResolve = getContentResolver();
        myUri = new Uri.Builder()
        	.scheme(SCHEME)
        	.authority(AUTHORITY)
        	.path(PATH)
        	.build();
        DataObserver watcher = new DataObserver(new Handler());
        Log.d("HomescreenActivity", "Registering ContentObserver for " + myUri);
        myResolve.registerContentObserver(myUri, true, watcher);
*/
        // Dummy account for emulator debugging
		am = AccountManager.get(getApplicationContext());
		Account newAcct = new Account(ACCOUNT, ACCOUNT_TYPE);
		am.addAccountExplicitly(newAcct, null, null);
		Account[] accts = am.getAccountsByType(ACCOUNT_TYPE);
		exAcct = accts[0];
        
		// Check network connectivity
		ConnectivityManager conMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conMgr.getActiveNetworkInfo();
    	TextView tvConStatus=(TextView) findViewById(R.id.textViewConStatus);
        if (netInfo !=null && netInfo.isConnected()) {
        	tvConStatus.setText("Network Connected as "+netInfo.getTypeName());
        } else {
        	tvConStatus.setText("No network connection, only local data available");
    	};
    	
    	// Plug data into GridView
    	getLoaderManager().initLoader(0, null, this);
    	cAdapt = new SimpleCursorAdapter(
    			this,																// Context
    			R.layout.list_tenant,												// Layout ID
    			null,																// Existing cursor
    			new String[]														// Cursor projection columns 
    				{HousingTenantsTable.COLUMN_HOUSING_TENANTS_SURNAME,
    				 HousingTenantsTable.COLUMN_HOUSING_TENANTS_FORENAME,
    				 HousingTenantsTable.COLUMN_HOUSING_TENANTS_CHECK_DETAILS},
    			new int[] {R.id.surname, R.id.forename, R.id.address},				// Column view mapping IDs
    			0);																	// Flags
    	GridView mTntGrid = (GridView) findViewById(R.id.tenant_grid);
    	mTntGrid.setAdapter(cAdapt);
    	mTntGrid.setOnItemClickListener(this);
    }
/*
    @Override
    protected void onPause() {
    	super.onPause();
    	myResolve.unregisterContentObserver(watcher);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (watcher != null) {
    		myResolve.registerContentObserver(myUri, true, watcher);
    	}
    }
*/

    @Override
    public void onDestroy() {
    	super.onDestroy();
//    	myResolve.unregisterContentObserver(watcher);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // TODO Configure search, add listeners, etc.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu); // Do we still need this? - return true instead?
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.action_search:
    			return false;
    		case R.id.action_settings:
    			// TODO Main settings menu
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Intent intent = new Intent(this, TenantMainActivity.class);
		intent.putExtra(EXTRA_TENANT, id);
		startActivity(intent);
		Log.d(parent.toString(), "Item selected from " + view.toString() + ": Item at position " + pos);
		return;
	}
/*
	// Observer for ContentProvider to trigger redraw
    public class DataObserver extends ContentObserver {

		public DataObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			onChange(selfChange, null);
		}
		
		@Override
		public void onChange (boolean selfChange, Uri changeUri) {
			Log.d("DataObserver", "I totally observed data.");
			getLoaderManager().restartLoader(0, null, );
		}
    }
*/  
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle argBundle) {
		String selection = null;
		String[] selectArgs = null;
		if (argBundle != null) {
			String searchTerm = argBundle.getString("searchTerm");
			selection = HousingTenantsTable.COLUMN_HOUSING_TENANTS_FORENAME + " LIKE ? OR "
				+ HousingTenantsTable.COLUMN_HOUSING_TENANTS_SURNAME + " LIKE ? OR "
				+ HousingTenantsTable.COLUMN_HOUSING_TENANTS_CHECK_DETAILS + " LIKE ?";
			selectArgs = new String[] {searchTerm, searchTerm, searchTerm};
		}
		CursorLoader mcLoader = new CursorLoader(
			this,														// Context
			UHRESTTestContentProvider.HOUSING_TENANTS_URI,				// URI
			HousingTenantsTable.PROJECTION_HOUSING_TENANTS_SUMMARY,		// Projection
			selection,													// Selection
			selectArgs,													// Selection Args
			null);														// Sort Order
		return mcLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		cAdapt.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		cAdapt.swapCursor(null);
	}

	@Override
	public boolean onQueryTextChange(String searchText) {
		String newSearch = !TextUtils.isEmpty(searchText) ? searchText : null;
		if (curSearch == null && newSearch == null) {
			return true;
		}
		if (curSearch != null && curSearch.equals(newSearch)) {
			return true;
		}
		curSearch = newSearch;
		if (curSearch == null) {
			getLoaderManager().restartLoader(0, null, this);
		} else {
			Bundle argBundle = new Bundle();
			argBundle.putString("searchTerm", "%" + curSearch + "%");
			getLoaderManager().restartLoader(0, argBundle, this);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String searchText) {
		invalidateOptionsMenu();
		return true;
	}

}
