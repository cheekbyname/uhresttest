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
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapt extends AbstractThreadedSyncAdapter {
	
	public static final String API_BASE = "http://192.168.121.176:3000";
    public static final String SCHEME = "content";
	public static final String AUTHORITY = "uk.org.blackwood.uhresttest.contentprovider";
	public static final String DATA_PATH = "uhresttest";
	
	// Custom argBundle parameters
	public static final String SYNCADAPT_TABLES = "syncadapt_tables";
	public static final String SYNCADAPT_APIS = "syncadapt_apis";
	public static final String SYNCADAPT_KEYS = "syncadapt_keys";
	public static final String SYNCADAPT_SCOPES = "syncadapt_scopes";
	public static final int SYNCADAPTSCOPE_ALL = 0;
	public static final int SYNCADAPTSCOPE_SINGLE = 1;
	public static final int SYNCADAPTSCOPE_KEYED = 2;
	public static final String SYNCADAPT_KEYTYPES = "syncadapt_keytypes";
	public static final int SYNCADAPTKEYTYPE_STRING = 0;
	public static final int SYNCADAPTKEYTYPE_INT = 1;
	public static final String SYNCADAPT_METHODS = "syncadapt_methods";
	public static final int SYNCADAPTMETHOD_DELETE = 0;
	public static final int SYNCADAPTMETHOD_GET = 1;
	public static final int SYNCADAPTMETHOD_PUT = 2;
	public static final int SYNCADAPTMETHOD_POST = 3;
	
	public ContentResolver myContentResolve;

	public SyncAdapt(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		myContentResolve = context.getContentResolver();
	}

	public SyncAdapt(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		myContentResolve = context.getContentResolver();
	}

	@Override
	public void onPerformSync(Account account, Bundle argBundle, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		// Extract argBundle parameters
		String syncTables = argBundle.getString(SYNCADAPT_TABLES);
		String syncApis = argBundle.getString(SYNCADAPT_APIS);
		String syncKeys = argBundle.getString(SYNCADAPT_KEYS);
		int syncScopes = argBundle.getInt(SYNCADAPT_SCOPES);
		int syncKeyTypes = argBundle.getInt(SYNCADAPT_KEYTYPES);
		int syncMethods = argBundle.getInt(SYNCADAPT_METHODS);
		
		if (syncApis != null) {
			try {
		        Uri myUri = new Uri.Builder()
	        	.scheme(SCHEME)
	        	.authority(AUTHORITY)
	        	.path(DATA_PATH + syncTables)
	        	.build();
				int rowsUpdated = jsonToContent(myUri, FetchData(API_BASE + syncApis + syncKeys));
				Log.i("SyncAdapt", "Executed " + String.valueOf(rowsUpdated) + " inserts on " + myUri);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// TODO Upload
		// TODO Conflicts
		// TODO Other non-data network tasks
		// TODO Cleanup

	}

	private int jsonToContent (Uri uri, JSONArray jsonData) {
		
		JSONObject jObj = null; 
		ContentValues dataRow = null;
		ContentValues [] dataSet = new ContentValues[jsonData.length()];

		try {
			// Pull first row for column names
			jObj = jsonData.getJSONObject(0);
			JSONArray jNames = jObj.names();

			for (int i=0; i<jsonData.length(); i++) {
				dataRow = new ContentValues();
				jObj = jsonData.getJSONObject(i);
				for (int j=0; j<jNames.length(); j++) {
					Object obJson = jObj.get(jNames.getString(j));
					if (obJson instanceof String) {
						dataRow.put(jNames.getString(j), (String) obJson);
					} else if (obJson instanceof Integer) {
						dataRow.put(jNames.getString(j), (Integer) obJson);
					} else {
						dataRow.put(jNames.getString(j), (Long) obJson);
					}
				}
				dataSet[i] = dataRow;
			}
			return myContentResolve.bulkInsert(uri, dataSet);

		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private JSONArray FetchData(String strUrl) throws MalformedURLException, IOException {
		InputStream isReturn = null;
		String strContent = null;
		JSONArray jsRes = null;
// TODO Trap no network
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
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (isReturn != null) {
				isReturn.close();
			}
		}
		try {
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
