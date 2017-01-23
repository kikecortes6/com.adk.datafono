package com.ci24.datafono;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;





public class Comdata extends CordovaPlugin {
 private CallbackContext callbackContext;
      private String test="hola Mundo";

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {
      
     
  
       if (action.equals("test")) {
          callbackContext.success(test);
    
        }
        
        
      

        

        return true;
        
    }
    

    
}
