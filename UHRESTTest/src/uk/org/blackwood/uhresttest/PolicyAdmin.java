package uk.org.blackwood.uhresttest;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PolicyAdmin extends DeviceAdminReceiver {

	private static final String APP_PREF = "RestTestPrefs";
//	DevicePolicyManager myDPM=(DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
	
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		// TODO Code for data wipe on policy revocation
		SharedPreferences prefs=context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
	}
}
