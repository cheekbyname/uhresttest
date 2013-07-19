package uk.org.blackwood.uhresttest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HousingSchemesTable {
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_DEFAULTS=" INTEGER PRIMARY KEY AUTOINCREMENT ";
	public static final String COLUMN_TEXT_DEFAULTS=" TEXT NOT NULL ";
	public static final String TABLE_HOUSING_SCHEMES = "schemes";
	public static final String COLUMN_HOUSING_SCHEMES_SHORT_ADDRESS = "short_address";
	public static final String COLUMN_HOUSING_SCHEMES_PROP_REF = "prop_ref";

	private static final String CREATE_HOUSING_SCHEMES=
			"CREATE TABLE "+TABLE_HOUSING_SCHEMES+"("
			+COLUMN_ID+COLUMN_ID_DEFAULTS+", "
			+COLUMN_HOUSING_SCHEMES_SHORT_ADDRESS+COLUMN_TEXT_DEFAULTS+", "
			+COLUMN_HOUSING_SCHEMES_PROP_REF+COLUMN_TEXT_DEFAULTS+");";

	public static void onCreate(SQLiteDatabase db) {
		// Create Tables
		db.execSQL(CREATE_HOUSING_SCHEMES);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		// Upgrade - clear down existing data, recreate and pull down
		Log.w(UHRESTTestHelper.class.getName(),"Upgrading database from version " + oldVer + " to "
			+ newVer + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSING_SCHEMES);
		onCreate(db);
		// TODO Pull down data into local reference tables
	}
}
