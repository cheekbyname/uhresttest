package uk.org.blackwod.uhresttest.contentprovider;

import uk.org.blackwood.uhresttest.UHRESTTestHelper;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.UriMatcher;
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

	public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
	public static final String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+BASE_PATH;
	public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+BASE_PATH;
	
	private static final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		myURIMatcher.addURI(AUTHORITY, HOUSING_OFFICERS_BASE, HOUSING_OFFICERS);
		myURIMatcher.addURI(AUTHORITY, HOUSING_OFFICERS_BASE+"/#", HOUSING_OFFICERS_ID);
		myURIMatcher.addURI(AUTHORITY, HOUSING_SCHEMES_BASE, HOUSING_SCHEMES);
		myURIMatcher.addURI(AUTHORITY, HOUSING_SCHEMES_BASE+"/#", HOUSING_SCHEMES_ID);
	}
	
}
