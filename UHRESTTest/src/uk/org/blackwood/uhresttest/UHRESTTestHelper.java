package uk.org.blackwood.uhresttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UHRESTTestHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "uhresttest.db";
	public static final int DATABASE_VERSION = 1;
	
	// Constructor
	public UHRESTTestHelper (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		HousingSchemesTable.onCreate(db);
		HousingOfficersTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		HousingSchemesTable.onUpgrade(db, oldVer, newVer);
		HousingOfficersTable.onUpgrade(db, oldVer, newVer);
	}
}
