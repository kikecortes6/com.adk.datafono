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

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;





public class Comdata extends CordovaPlugin {
 private CallbackContext callbackContext;
      private String test="Conexion Iniciada";
 private String ser="Servicio Finalizado";
 private String saludo;
  private Datafono datafono;
  protected PclService mPclService;
  public Activity getActivity() {
    return this.cordova.getActivity();
  }

  private Intent getIntent() {
    return getActivity().getIntent();
  }

  private void setIntent(Intent intent) {
    getActivity().setIntent(intent);
  }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {



       if (action.equals("test")) {

         Datafono.getInstance().initService(getActivity());

          callbackContext.success(test);

        }else if(action.equals("testing")){

          boolean bRet= Datafono.getInstance().isCompanionConnected();
            if(bRet==true){
              callbackContext.success("Connected");

            }else{
              callbackContext.success("Not Connected");
            }




       }else if(action.equals("pcl")){
         Datafono.getInstance().releaseService(getActivity());

         callbackContext.success(ser);

       }






        return true;

    }



}
