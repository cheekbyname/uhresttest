package uk.org.blackwood.uhresttest;

import uk.org.blackwood.uhresttest.contentprovider.UHRESTTestContentProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

public class TenantBasicFragment
	extends Fragment
	implements LoaderManager.LoaderCallbacks<Cursor>, ViewBinder {

	private SimpleCursorAdapter cAdapt;
	TenantHandler mTntHandle;
	
	public interface TenantHandler {
		public long getTenant_id();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Plug in data
    	cAdapt = new SimpleCursorAdapter(
    			this.getActivity(),											// Context
    			R.layout.tenant_basic_frag,									// Layout ID
    			null,														// Existing cursor
    			new String[]												// Cursor projection columns 
    				{HousingTenantsTable.COLUMN_HOUSING_TENANTS_TITLE,
    				 HousingTenantsTable.COLUMN_HOUSING_TENANTS_FORENAME,
    				 HousingTenantsTable.COLUMN_HOUSING_TENANTS_SURNAME},
    			new int[]													// Column view mapping IDs
    				{R.id.edTntTitle,
    				 R.id.edTntForename,
    				 R.id.edTntSurname},
    			0);															// Flags
    	// TODO SimpleCursorAdapter.ViewBinder?
    	cAdapt.setViewBinder(this);
    	return inflater.inflate(R.layout.tenant_basic_frag, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle argBundle = new Bundle();
		argBundle.putLong("tenant_id", ((TenantHandler) getActivity()).getTenant_id());
		getLoaderManager().initLoader(0, argBundle, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = null;
		String[] selectArgs = null;
		if (args != null) {
			long selectId = args.getLong("tenant_id");
			selection = HousingTenantsTable.COLUMN_ID + " = ?";
			selectArgs = new String[] {String.valueOf(selectId)};
		}
		CursorLoader mcLoader = new CursorLoader(
			this.getActivity(),											// Context
			UHRESTTestContentProvider.HOUSING_TENANTS_URI,				// URI
			HousingTenantsTable.PROJECTION_HOUSING_TENANTS_SUMMARY,		// Projection
			selection,													// Selection
			selectArgs,													// Selection Args
			null);														// Sort Order
		return mcLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d("TenantBasicFragment","I got " + cursor.getCount() + " rows yeah?");
		cAdapt.swapCursor(cursor);
		cursor.moveToFirst();
		try {
			cAdapt.bindView(this.getView(), getActivity().getApplicationContext(), cursor);
		} catch (CursorIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		cAdapt.swapCursor(null);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		EditText eView = (EditText) view;
		eView.setText(cursor.getString(columnIndex));
		Log.d("TenantBasicFragment","I supposedly bound a view for column " + columnIndex);
		return true;
	}
}
