package com.ci24.datafono;

import com.ci24.functions.Datafono;
import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;





public class Comdata extends CordovaPlugin {
 private CallbackContext callbackContext;
      private String test="hola Mundo";
 private String ser="Servicio Iniciado";
 private String saludo;

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {
      
     
  
       if (action.equals("test")) {
      
          callbackContext.success(test);
    
        }else if(action.equals("testing")){
         saludo=test+args;
        callbackContext.success(saludo);
        
       }else if(action.equals("pcl")){
       Datafono.initService();
         callbackContext.success(ser);
        
       }
        
        
      

        

        return true;
        
    }
    

    
}
