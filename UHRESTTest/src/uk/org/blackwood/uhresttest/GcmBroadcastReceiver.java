package uk.org.blackwood.uhresttest;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	public static final String AUTHORITY = "uk.org.blackwood.uhresttest.contentprovider";
	// public static final String ACCOUNT_TYPE = "uk.org.blackwood";
	public static final String ACCOUNT_TYPE = "com.android.exchange";
	public static final String ACCOUNT = "dummyaccount";
	public static final String KEY_SYNC_REQUEST = "uk.org.blackwood.uhresttest.KEY_SYNC_REQUEST";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Retrieve Exchange Account
		AccountManager am = AccountManager.get(context);
		Account[] accts = am.getAccountsByType(ACCOUNT_TYPE);
		// TODO Ensure at least one Exchange Account is visible
		
		// Get MessageType etc
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType) && intent.getBooleanExtra(KEY_SYNC_REQUEST, false)) {
			ContentResolver.requestSync(accts[0], AUTHORITY, null);
		}
		
	}
	

}
