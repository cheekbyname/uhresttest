package uk.org.blackwood.uhresttest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HousingOfficersTable {
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_DEFAULTS=" INTEGER PRIMARY KEY AUTOINCREMENT ";
	public static final String COLUMN_TEXT_DEFAULTS=" TEXT NOT NULL ";
	public static final String TABLE_HOUSING_OFFICERS = "housing_officers";
	public static final String COLUMN_HOUSING_OFFICERS_LU_REF = "lu_ref";
	public static final String COLUMN_HOUSING_OFFICERS_LU_DESC = "lu_desc";

	private static final String CREATE_HOUSING_OFFICERS=
			"CREATE TABLE " + TABLE_HOUSING_OFFICERS + "("
			+ COLUMN_ID + COLUMN_ID_DEFAULTS + ", "
			+ COLUMN_HOUSING_OFFICERS_LU_REF + COLUMN_TEXT_DEFAULTS + ", "
			+ COLUMN_HOUSING_OFFICERS_LU_DESC + COLUMN_TEXT_DEFAULTS + ");";

	public static void onCreate(SQLiteDatabase db) {
		// Create Tables
		Log.d("HousingOfficersTable", "Executing " + CREATE_HOUSING_OFFICERS);
		db.execSQL(CREATE_HOUSING_OFFICERS);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		// Upgrade - clear down existing data, recreate and pull down
		Log.w(UHRESTTestHelper.class.getName(),"Upgrading database from version " + oldVer + " to "
			+ newVer + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSING_OFFICERS);
		onCreate(db);
		// TODO Request Sync
	}
}
