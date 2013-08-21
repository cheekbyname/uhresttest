package uk.org.blackwood.uhresttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapt extends AbstractThreadedSyncAdapter {
	
	public static final String API_BASE = "http://192.168.121.176:3000";
	
	public ContentResolver myContentResolve;
	private UHRESTTestHelper dbHelp;
	private SQLiteDatabase db;

	public SyncAdapt(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		myContentResolve = context.getContentResolver();
		dbHelp = new UHRESTTestHelper(getContext());
		db = dbHelp.getWritableDatabase();
	}

	public SyncAdapt(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		myContentResolve = context.getContentResolver();
		dbHelp = new UHRESTTestHelper(getContext());
		db = dbHelp.getWritableDatabase();
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		// TODO Download - How are we going to identify which tables need updated?
		try {
			jsonToTable(HousingTenantsTable.TABLE_HOUSING_TENANTS, 
				FetchData(API_BASE + HousingTenantsTable.API_PATH + "AD2"));	// TODO - Replace explicit Officer reference
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Upload
		// TODO Conflicts
		// TODO Other non-data network tasks
		// TODO Cleanup
	}

	// Parse JSONArray into Table
	private void jsonToTable(String table, JSONArray jsonData) {

		// Build & compile SQL statement
		String strSql = "INSERT INTO " + table + " ";
		JSONObject jObj = null;
		// Pull first row for column heads
		try {
			jObj = jsonData.getJSONObject(0);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		JSONArray jNames = jObj.names();
		strSql = strSql + jNames.toString();
		strSql = strSql.replace("[", "(").replace("]", ")").replace("\"", "");
		strSql = strSql + " VALUES (";
		for (int i = 0; i < jNames.length(); i++) {
			strSql = strSql.concat("?, ");
		}
		strSql = strSql.substring(0, strSql.length()-2);
		strSql = strSql + ");";
		Log.d("SyncAdapt", strSql);

		db.beginTransaction();
		SQLiteStatement sqlInsert = db.compileStatement(strSql);

		// TODO Start of Transaction try block
		// Iterate rows
		for (int i = 0; i < jsonData.length(); i++) {
			sqlInsert.clearBindings();
			try {
				jObj = jsonData.getJSONObject(i);
				// Iterate columns and bind parameterized values
				for (int j = 0; j < jNames.length(); j++) {
					Object obJson = jObj.get(jNames.getString(j));
					if (obJson instanceof String) {
						sqlInsert.bindString(j + 1, jObj.getString(jNames.getString(j)));
					} else { 
						sqlInsert.bindLong(j + 1, jObj.getLong(jNames.getString(j)));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			sqlInsert.executeInsert();
		}
		db.setTransactionSuccessful(); // TODO End of Transaction try block
		db.endTransaction();	// TODO Finally...
	}

	private JSONArray FetchData(String strUrl) throws MalformedURLException, IOException {
//		Log.d("SyncAdapt", strUrl);	// TODO DEBUG
		InputStream isReturn = null;
		String strContent = null;
		JSONArray jsRes = null;
		try {
			// Connection
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			isReturn = conn.getInputStream();
			strContent = readStream(isReturn);
//			Log.d("SyncAdapt", strContent);	// TODO DEBUG
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (isReturn != null) {
				isReturn.close();
			}
		}
		try {	// TODO Remove this when moving to GSON
			jsRes = new JSONArray(strContent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsRes;
	}
	
	private String readStream(InputStream isReturn) throws IOException, UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(isReturn));
		StringBuilder strBuild = new StringBuilder();
		String strInput;
		while ((strInput = reader.readLine()) != null ) strBuild.append(strInput);
		return strBuild.toString();
	}

}
