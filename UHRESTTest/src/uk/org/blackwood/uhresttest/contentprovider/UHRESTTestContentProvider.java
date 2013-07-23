package uk.org.blackwood.uhresttest.contentprovider;

import uk.org.blackwood.uhresttest.UHRESTTestHelper;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class UHRESTTestContentProvider extends ContentProvider {
	
	// Database
	private UHRESTTestHelper db;
	
	// URI
	private static final int HOUSING_OFFICERS = 10;
	private static final int HOUSING_OFFICERS_ID = 11;
	private static final String HOUSING_OFFICERS_BASE = "Housing/Officers";
	private static final int HOUSING_SCHEMES = 20;
	private static final int HOUSING_SCHEMES_ID=21;
	private static final String HOUSING_SCHEMES_BASE = "Housing/Schemes";
	private static final String AUTHORITY = "uk.org.blackwood.uhresttest.contentprovider";

	// TODO Sort these out, they're wrong eh
	public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+HOUSING_OFFICERS_BASE);
	public static final String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+HOUSING_OFFICERS_BASE;
	public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+HOUSING_OFFICERS_BASE;
	
	private static final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		myURIMatcher.addURI(AUTHORITY, HOUSING_OFFICERS_BASE, HOUSING_OFFICERS);
		myURIMatcher.addURI(AUTHORITY, HOUSING_OFFICERS_BASE+"/#", HOUSING_OFFICERS_ID);
		myURIMatcher.addURI(AUTHORITY, HOUSING_SCHEMES_BASE, HOUSING_SCHEMES);
		myURIMatcher.addURI(AUTHORITY, HOUSING_SCHEMES_BASE+"/#", HOUSING_SCHEMES_ID);
	}
	
	@Override
	public boolean onCreate() {
		db=new UHRESTTestHelper(getContext());
		return false;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType=myURIMatcher.match(uri);
		SQLiteDatabase sqlDb=db.getWritableDatabase();
		int rowsUpdated=0;
		switch (uriType) {
			case (HOUSING_OFFICERS):
				
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
