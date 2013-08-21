package uk.org.blackwood.uhresttest;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HousingTenantsCommsTable {
	// Table defaults
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_DEFAULTS = " INTEGER PRIMARY KEY AUTOINCREMENT ";
	public static final String COLUMN_TEXT_DEFAULTS = " TEXT NOT NULL ";
	public static final String COLUMN_INT_DEFAULTS = " INTEGER ";
	public static final String COLUMN_SEPARATOR = ", ";
	public static final String TABLE_HOUSING_TENANTS_COMMS = "housing_tenants_comms";
	public static final String API_PATH = "/housing/tenants/comms/tenant/";
	
	// Columns
	public static final String COLUMN_HOUSING_TENANTS_COMMS_CON_KEY = "con_key";
	public static final String COLUMN_HOUSING_TENANTS_COMMS_COMMS_TYPE = "comms_type";
	public static final String COLUMN_HOUSING_TENANTS_COMMS_COMMS_VALUE = "comms_value";
	public static final String COLUMN_HOUSING_TENANTS_COMMS_COMMS_DESC = "comms_desc";
	public static final String COLUMN_HOUSING_TENANTS_COMMS_COMMS_NAME = "comms_name";
	
	// Projections
	public static final String [] PROJECTION_HOUSING_TENANTS_COMMS_ALL = 
		{COLUMN_ID, COLUMN_HOUSING_TENANTS_COMMS_COMMS_TYPE, COLUMN_HOUSING_TENANTS_COMMS_COMMS_VALUE,
		COLUMN_HOUSING_TENANTS_COMMS_COMMS_DESC, COLUMN_HOUSING_TENANTS_COMMS_COMMS_NAME};

	// Create Table SQL
	public static final String CREATE_TENANTS_COMMS = 
		"CREATE TABLE " + TABLE_HOUSING_TENANTS_COMMS + "("
		+ COLUMN_ID + COLUMN_ID_DEFAULTS + COLUMN_SEPARATOR
		+ COLUMN_HOUSING_TENANTS_COMMS_CON_KEY + COLUMN_INT_DEFAULTS + COLUMN_SEPARATOR
		+ COLUMN_HOUSING_TENANTS_COMMS_COMMS_TYPE + COLUMN_TEXT_DEFAULTS + COLUMN_SEPARATOR
		+ COLUMN_HOUSING_TENANTS_COMMS_COMMS_VALUE + COLUMN_TEXT_DEFAULTS + COLUMN_SEPARATOR
		+ COLUMN_HOUSING_TENANTS_COMMS_COMMS_DESC + COLUMN_TEXT_DEFAULTS + COLUMN_SEPARATOR
		+ COLUMN_HOUSING_TENANTS_COMMS_COMMS_NAME + COLUMN_TEXT_DEFAULTS + ");";
	
	public static void onCreate(SQLiteDatabase db) {
		Log.d("HousingTenantsCommsTable", "Executing " + CREATE_TENANTS_COMMS);
		db.execSQL(CREATE_TENANTS_COMMS);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		// Upgrade - clear down existing data, recreate and pull down
		Log.w(UHRESTTestHelper.class.getName(), "Upgrading database from version "+ oldVer + " to"
			+ newVer + " which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSING_TENANTS_COMMS);
		onCreate(db);
		ContentResolver.requestSync(HomescreenActivity.exAcct, HomescreenActivity.AUTHORITY, null);
	}

}
