package uk.org.blackwood.uhresttest;

import uk.org.blackwood.uhresttest.contentprovider.UHRESTTestContentProvider;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;

public class TenantCommsFragment
	extends ListFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter cAdapt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Plug in data
    	cAdapt = new SimpleCursorAdapter(
    			this.getActivity(),											// Context
    			R.layout.list_comms,										// Layout ID
    			null,														// Existing cursor
    			new String[]												// Cursor projection columns 
    				{HousingTenantsCommsTable.COLUMN_HOUSING_TENANTS_COMMS_COMMS_TYPE,
    				 HousingTenantsCommsTable.COLUMN_HOUSING_TENANTS_COMMS_COMMS_VALUE,
    				 HousingTenantsCommsTable.COLUMN_HOUSING_TENANTS_COMMS_COMMS_DESC,
    				 HousingTenantsCommsTable.COLUMN_HOUSING_TENANTS_COMMS_COMMS_NAME},
    			new int[]													// Column view mapping IDs
    				{R.id.tvCommsType,
    				 R.id.tvCommsValue,
    				 R.id.tvCommsDesc,
    				 R.id.tvCommsName},
    			0);															// Flags
    	setListAdapter(cAdapt);
    	return inflater.inflate(R.layout.tenant_comms_frag, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle argBundle = new Bundle();
		argBundle.putLong("tenant_con_key", ((TenantHandler) getActivity()).getTenant_con_key());
		getLoaderManager().initLoader(1, argBundle, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle argBundle) {
		long selectId = 0;
		String selection = null;
		String[] selectArgs = null;
		if (argBundle != null) {
			selectId = argBundle.getLong("tenant_con_key");
			selection = HousingTenantsCommsTable.COLUMN_HOUSING_TENANTS_COMMS_CON_KEY+ " = ?";
			selectArgs = new String[] {String.valueOf(selectId)};
		} else {
			return null;
		}
		CursorLoader mcLoader = new CursorLoader(
				getActivity(),													// Context
				UHRESTTestContentProvider.HOUSING_TENANTS_COMMS_URI,			// URI
				HousingTenantsCommsTable.PROJECTION_HOUSING_TENANTS_COMMS_ALL,	// Projection
				selection,														// Selection
				selectArgs,														// Selection Args
				null);															// Sort Order
			return mcLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		cAdapt.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cAdapt.swapCursor(null);
	}
	
}
