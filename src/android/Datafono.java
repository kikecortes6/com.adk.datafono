
package com.ci24.functions;

import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import android.app.*;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.Context;
import android.content.Intent;
import 	android.os.Binder;

public class Datafono extends Activity {
  // Declare PclService interface

protected PclService mPclService = null;

// Declare Serviceconnection
  
  private PclServiceConnection mServiceConnection;
  
  // Implement ServiceConnection

class PclServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder boundService ){

                          // We've bound to LocalService, cast the IBinder and get LocalService instance

               // LocalBinder binder = boundService;

              //  mPclService = (PclService) binder.getService();

           }

        public void onServiceDisconnected(ComponentName className) {

                mPclService = null; 

            }
  public class LocalBinder extends Binder {
        PclServiceConnection getService() {
            // Return this instance of LocalService so clients can call public methods
            return PclServiceConnection.this;
        }
    }

    };
  
  // You can call this method in onCreate for instance

private void initService()

{

mServiceConnection = new PclServiceConnection();

Intent intent = new Intent(this, PclService.class);

bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

}
  // You can call this method in onDestroy for instance

private void releaseService()

{

unbindService(mServiceConnection);

}
  
  
  
  
};
