/** 
 *	Fragment for displaying household information
 */
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


public class TenantHouseholdFragment
	extends ListFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter cAdapt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Plug in data
    	cAdapt = new SimpleCursorAdapter(
    			this.getActivity(),											// Context
    			R.layout.list_household,									// Layout ID
    			null,														// Existing cursor
    			new String[]												// Cursor projection columns 
    				{HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_TITLE,
    				 HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_FORENAME,
    				 HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_SURNAME,
    				 HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_RELATIONSHIP,
    				 HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_DOB,
    				 HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_NI_NO},
    			new int[]													// Column view mapping IDs
    				{R.id.tvHHtitle,
    				 R.id.tvHHforename,
    				 R.id.tvHHsurname,
    				 R.id.tvHHRelation,
    				 R.id.tvHHDob,
    				 R.id.tvHHNino},
    			0);															// Flags
    	setListAdapter(cAdapt);
    	return inflater.inflate(R.layout.tenant_household_frag, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle argBundle = new Bundle();
		argBundle.putString("tenant_house_ref", ((TenantHandler) getActivity()).getTenant_house_ref());
		getLoaderManager().initLoader(1, argBundle, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle argBundle) {
		String selectRef = "";
		String selection = null;
		String[] selectArgs = null;
		if (argBundle != null) {
			selectRef = argBundle.getString("tenant_house_ref");
			selection = HousingTenantsHouseholdTable.COLUMN_HOUSING_TENANTS_HOUSEHOLD_HOUSE_REF + " = ?";
			selectArgs = new String[] {selectRef};
		} else {
			return null;
		}
		CursorLoader mcLoader = new CursorLoader(
				getActivity(),																// Context
				UHRESTTestContentProvider.HOUSING_TENANTS_HOUSEHOLD_URI,					// URI
				HousingTenantsHouseholdTable.PROJECTION_HOUSING_TENANTS_HOUSEHOLD_SUMMARY,	// Projection
				selection,																	// Selection
				selectArgs,																	// Selection Args
				null);																		// Sort Order
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
