package uk.org.blackwood.uhresttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

public class HomescreenActivity extends Activity implements OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        
        Spinner urlSpin=(Spinner) findViewById(R.id.spinTestUrls);
        urlSpin.setOnItemSelectedListener(this);
        
        // Check connectivity
        String strURL=getResources().getString(R.string.base_url);
        ConnectivityManager conMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conMgr.getActiveNetworkInfo();
    	TextView tvConStatus=(TextView) findViewById(R.id.textViewConStatus);
        if (netInfo !=null && netInfo.isConnected()) {
        	tvConStatus.setText("Network Connected as "+netInfo.getTypeName()+", checking REST API...");
        	new RestTest().execute(strURL);
        } else {
        	tvConStatus.setText("Connection Error!");
    	};
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        return true;
    }

    private class RestTest extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... urls) {
    		try {
    			return fetchUrl(urls[0]);
    		} catch (IOException e) {
    			return "Error retrieving URL";
    		}
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
        	TextView tvUrlReturn=(TextView) findViewById(R.id.textViewUrlReturn);
    		tvUrlReturn.setText(result);
    		try {
				JSONArray jsRes=new JSONArray(result);
				Toast.makeText(getApplicationContext(), "JSON ahoy!", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Not JSON!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
    	}

		private String fetchUrl(String strUrl) throws IOException {
			InputStream isReturn=null;
			
			try {
				URL url=new URL(strUrl);
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				isReturn=conn.getInputStream();
				String strContent=readStream(isReturn);
				return strContent;
			} finally {
				if (isReturn !=null) {
					isReturn.close();
				}
			}
		}

		private String readStream(InputStream isReturn) throws IOException, UnsupportedEncodingException {
			BufferedReader reader= new BufferedReader(new InputStreamReader(isReturn));
			StringBuilder strBuild = new StringBuilder();
			String strInput;
			while ((strInput=reader.readLine()) !=null ) strBuild.append(strInput);
			return strBuild.toString();
		}
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
    	TextView tvUrlReturn=(TextView) findViewById(R.id.textViewUrlReturn);
		tvUrlReturn.setText("Retrieving selected URL...");
		new RestTest().execute(parent.getItemAtPosition(pos).toString());
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    
}
