package ru.magnat.android.service.openvpn.client;

import ru.magnat.android.service.openvpn.aidl.IOpenVpnService_External;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private IOpenVpnService_External mOpenVpnService_External;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	Log.d("", "Connected to external service");
	    	
	    	mOpenVpnService_External = IOpenVpnService_External.Stub.asInterface(service);
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	Log.d("", "Disconnected from external service");
	    	
	    	mOpenVpnService_External = null;
	    }
	    
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bindService(new Intent("ru.magnat.android.service.openvpn.OpenVpnService_External"), mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unbindService(mConnection); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem item = menu.add(R.string.test);
		item.setIcon(android.R.drawable.ic_media_play);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getTitle().equals(getResources().getString(R.string.test))) {
			try {
				mOpenVpnService_External.test();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
		
		return super.onMenuItemSelected(featureId, item);
	}
	
}
