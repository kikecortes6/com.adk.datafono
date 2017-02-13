package com.ci24.datafono;
import com.ci24.Interfaces.IfaceCallbackDatafono;
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
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

import static android.R.attr.name;
import static org.apache.cordova.device.Device.TAG;






public class Comdata extends CordovaPlugin implements IfaceCallbackDatafono {
  public CallbackContext callbackContext;
 private String test="Conexion Iniciada";
 private String ser="Servicio Finalizado";

  byte[] extDataOut = new byte[5000];

  public Activity getActivity() {    return this.cordova.getActivity();  }
 // private Intent getIntent() {     return getActivity().getIntent();   }
 // private void setIntent(Intent intent) {     getActivity().setIntent(intent);   }

 @Override

    public boolean execute(String action, final JSONArray args,final  CallbackContext callbackContext) throws JSONException {

       if (action.equals("init")) {
         try {
           Datafono.getInstance().initService(getActivity());
           callbackContext.success(test);
         }catch (Exception e){
           callbackContext.error(e.toString());
         }


        }else if(action.equals("check")){
          boolean check= Datafono.getInstance().isCompanionConnected();
            if(check==true){
              callbackContext.success("Connected");
            }else{
              callbackContext.error("Not Connected");
            }

       }else if(action.equals("finish")){
         try {
           Datafono.getInstance().releaseService(getActivity());
           callbackContext.success(ser);
         }catch (Exception e){
           callbackContext.error(e.toString());
         }

       }else if(action.equals("f")){

         byte[] extDataOut2= extDataOut;

         Log.d(TAG, "ingenico");
         Log.d(TAG, String.valueOf(extDataOut2.length));
         callbackContext.error("trans");



       }
     else if(action.equals("transactionEX")){
         this.callbackContext = callbackContext;
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
             TransactionIn transIn = new TransactionIn();
             TransactionOut transOut = new TransactionOut();


             long[] sizebuff = new long[5000];

             String amount = new String();
             String currencyCode = new String();
             String operation = new String();
             String termNum = new String();
             String authorizationType = new String();
             String ctrlCheque = new String();
             String userData1 = new String();
             JSONArray trama = new JSONArray();
             int appNumber = 0;
             int sizeIn = 0;
             JSONArray obj = new JSONArray();
             obj = args;
             try {
               for (int i = 0; i < obj.length(); i++) {
                 JSONObject jsonobject = obj.getJSONObject(i);
                 amount = jsonobject.getString("Amount");
                 currencyCode = jsonobject.getString("CurrencyCode");
                 operation = jsonobject.getString("Operation");
                 termNum = jsonobject.getString("TermNum");
                 authorizationType = jsonobject.getString("AuthorizationType");
                 ctrlCheque = jsonobject.getString("CtrlCheque");
                 userData1 = jsonobject.getString("UserData1");
                 appNumber = jsonobject.getInt("AppNumber");
                 trama = jsonobject.getJSONArray("Trama");
               }
             } catch (Exception e) {
               Log.d(TAG, e.toString());
               callbackContext.error(e.toString());
              // return true;

             }
             sizeIn = trama.length();

             if (!amount.equals("")) { transIn.setAmount(amount); }
             if (!currencyCode.equals("")) { transIn.setCurrencyCode(currencyCode);}
             if (!operation.equals("")) { transIn.setOperation(operation); }
             if (!termNum.equals("")) { transIn.setTermNum(amount); }
             if (!authorizationType.equals("")) { transIn.setAuthorizationType(authorizationType); }
             if (!ctrlCheque.equals("")) { transIn.setCtrlCheque(ctrlCheque); }
             if (!userData1.equals("")) { transIn.setUserData1(userData1); }

             byte tramaIn[] = new byte[sizeIn];
             for (int i = 0; i < sizeIn; i++) {
               try {
                 tramaIn[i] = (byte) Integer.parseInt(trama.getString(i));
                 if (tramaIn[i] < 0) {
                   tramaIn[i] =  tramaIn[i];
                 }
               } catch (Exception e) {
                 Log.d(TAG, e.toString());
                 callbackContext.error(e.toString());
               }
             }

             boolean check = Datafono.getInstance().isCompanionConnected();

             if (check == true) {
               try {
                 Log.d(TAG, "Amount:" + transIn.getAmount() + " Currency:" + transIn.getCurrencyCode() + " Operation:" + transIn.getOperation());
                 Log.d(TAG, "TermNum:" + transIn.getTermNum() + " AuthoType:" + transIn.getAuthorizationType() + " CtrlCheque:" + transIn.getCtrlCheque());
                 Log.d(TAG, "UserData:" + transIn.getUserData1());

                 Datafono.getInstance().doTransactionEx(Comdata.this, transIn, transOut, appNumber, tramaIn, sizeIn, extDataOut, sizebuff);

               } catch (Exception e) {
                 Log.d(TAG, e.toString());
                 callbackContext.error(e.toString());
                }
             } else {
               callbackContext.error("Device Not Connected");
             }
           }
         });
         return true;
       }
     else if(action.equals("t")){
         TransactionIn transIn = new TransactionIn();
         TransactionOut result=new TransactionOut();
         String amount = "995";
         transIn.setAmount(amount);
         transIn.setCurrencyCode("978");
         transIn.setOperation("C");
         transIn.setTermNum("58");
         Log.d(TAG,"envio Tramas!!!!");
         boolean trans = Datafono.getInstance().doTransaction(transIn,result);
         callbackContext.success("getInfo");
       }
        return true;
    }


  @Override
  public void responseDatafono(JSONArray response) {
    callbackContext.success(response);
  }
}
