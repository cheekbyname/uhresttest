package uk.org.blackwood.uhresttest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncServe extends Service {
	private static SyncAdapt mySyncAdapt = null;
	private static Object mySyncLock = new Object();

	public SyncServe() {
		super();
	}

	@Override
	public void onCreate() {
		synchronized (mySyncLock) {
			if (mySyncAdapt == null) {
				mySyncAdapt = new SyncAdapt(getApplicationContext(), true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mySyncAdapt.getSyncAdapterBinder();
	}

}
