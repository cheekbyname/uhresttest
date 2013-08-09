package uk.org.blackwood.uhresttest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticService extends Service {
	private Authentic myAuth;
	
	@Override
	public void onCreate() {
		myAuth=new Authentic(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myAuth.getIBinder();
	}

}
