package uk.org.blackwood.uhresttest.contentprovider;

import uk.org.blackwood.uhresttest.HomescreenActivity;
import uk.org.blackwood.uhresttest.HousingOfficersTable;
import uk.org.blackwood.uhresttest.HousingSchemesTable;
import uk.org.blackwood.uhresttest.HousingTenantsCommsTable;
import uk.org.blackwood.uhresttest.HousingTenantsTable;
import uk.org.blackwood.uhresttest.SyncAdapt;
import uk.org.blackwood.uhresttest.UHRESTTestHelper;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class UHRESTTestContentProvider extends ContentProvider {
	
	// Database
	public UHRESTTestHelper db;
	
	// URI
	private static final String AUTHORITY = "uk.org.blackwood.uhresttest.contentprovider";
	private static final String BASE_PATH = "uhresttest/";
	private static final int HOUSING_OFFICERS = 10;
	private static final int HOUSING_OFFICERS_ID = 11;
	private static final String HOUSING_OFFICERS_PATH = "Housing/Officers";
	private static final int HOUSING_SCHEMES = 20;
	private static final int HOUSING_SCHEMES_ID=21;
	private static final String HOUSING_SCHEMES_PATH = "Housing/Schemes";
	private static final int HOUSING_TENANTS = 30;
	private static final int HOUSING_TENANTS_ID = 31;
	private static final String HOUSING_TENANTS_PATH = "Housing/Tenants";
	private static final int HOUSING_TENANTS_COMMS = 40;
	private static final int HOUSING_TENANTS_COMMS_ID = 41;
	private static final String HOUSING_TENANTS_COMMS_PATH = "Housing/Tenants/Comms";

	// TODO Sort these out, they're wrong?
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final Uri HOUSING_TENANTS_URI = Uri.parse(CONTENT_URI + HOUSING_TENANTS_PATH);	// Why?
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE;
	
	private static final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_OFFICERS_PATH, HOUSING_OFFICERS);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_OFFICERS_PATH + "/#", HOUSING_OFFICERS_ID);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_SCHEMES_PATH, HOUSING_SCHEMES);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_SCHEMES_PATH + "/#", HOUSING_SCHEMES_ID);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_TENANTS_PATH, HOUSING_TENANTS);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_TENANTS_PATH + "/#", HOUSING_TENANTS_ID);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_TENANTS_COMMS_PATH, HOUSING_TENANTS_COMMS);
		myURIMatcher.addURI(AUTHORITY, BASE_PATH + HOUSING_TENANTS_COMMS_PATH + "/#", HOUSING_TENANTS_COMMS_ID);
	}
	
	@Override
	public boolean onCreate() {
		db = new UHRESTTestHelper(getContext());
		return true;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = myURIMatcher.match(uri);
//		TODO SQLiteDatabase sqlDb=db.getWritableDatabase();
		int rowsUpdated=0;
		switch (uriType) {
		}
		
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName;
		int contentUri = myURIMatcher.match(uri);
		Log.d("ContentProvider", "Attempting insert for " + String.valueOf(contentUri) + ":" + uri.toString());
		switch (contentUri) {
		case HOUSING_TENANTS:
			tableName = HousingTenantsTable.TABLE_HOUSING_TENANTS;
			break;
		case HOUSING_TENANTS_COMMS:
			tableName = HousingTenantsCommsTable.TABLE_HOUSING_TENANTS_COMMS;
			break;
		case HOUSING_OFFICERS:
			tableName = HousingOfficersTable.TABLE_HOUSING_OFFICERS;
			break;
		case HOUSING_SCHEMES:
			tableName = HousingSchemesTable.TABLE_HOUSING_SCHEMES;
			break;
		default :
			throw new IllegalArgumentException("Invalid URI for insert operation: " + uri.toString());
		}
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		try {
			long newID = sqlDB.insertOrThrow(HousingTenantsTable.TABLE_HOUSING_TENANTS, null, values);	// TODO Replace with tableName
			if (newID > 0) {
				Uri newUri = ContentUris.withAppendedId(uri, newID);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
		} catch (SQLiteConstraintException e) {
			Log.i("ContentProvider", "Ignoring constraint failure.");
		}
		return null; 
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qBuild = new SQLiteQueryBuilder();
		
		// Sync parameter argBundle for empty cursor fallback
		Bundle syncSet = new Bundle();
		syncSet.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		syncSet.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		
		int contentUri = myURIMatcher.match(uri);
		switch (contentUri) {
		case HOUSING_TENANTS:
			qBuild.setTables(HousingTenantsTable.TABLE_HOUSING_TENANTS);
			// Fallback argBundle parameters
			syncSet.putString(SyncAdapt.SYNCADAPT_TABLES, HousingTenantsTable.CONTENT_PATH);
			syncSet.putString(SyncAdapt.SYNCADAPT_APIS, HousingTenantsTable.API_PATH);
			syncSet.putInt(SyncAdapt.SYNCADAPT_SCOPES, SyncAdapt.SYNCADAPTSCOPE_KEYED);
			syncSet.putInt(SyncAdapt.SYNCADAPT_KEYTYPES, SyncAdapt.SYNCADAPTKEYTYPE_STRING);
			syncSet.putString(SyncAdapt.SYNCADAPT_KEYS, "AD2");		// TODO Remove hard-coding
			break;
		case HOUSING_TENANTS_ID:
			qBuild.setTables(HousingTenantsTable.TABLE_HOUSING_TENANTS);
			qBuild.appendWhere(HousingTenantsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			// TODO Fallback argBundle parameters
			break;
		case HOUSING_TENANTS_COMMS:
			qBuild.setTables(HousingTenantsCommsTable.TABLE_HOUSING_TENANTS_COMMS);
			// TODO Fallback argBundle parameters
			break;
		case HOUSING_TENANTS_COMMS_ID:
			qBuild.setTables(HousingTenantsCommsTable.TABLE_HOUSING_TENANTS_COMMS);
			qBuild.appendWhere(HousingTenantsCommsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			// TODO Fallback argBundle parameters
			break;
		default:
			throw new IllegalArgumentException("Unknown Content URI " + uri);
		}
		
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		// TODO checkColumns(projection);

		Cursor cursor = qBuild.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);

		// Request data sync if cursor blank and no search filter (i.e. selection parameters)
		if (cursor.getCount() == 0 && selection == null) {
			ContentResolver.requestSync(HomescreenActivity.exAcct, HomescreenActivity.AUTHORITY, syncSet);
		} else {
			Log.d("ContentProvider.query", "Returned " + cursor.getCount() + " rows from query on URI " + uri.toString());
		}

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
}
