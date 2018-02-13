package com.adk.Interfaces;

import org.json.JSONArray;



public interface IfaceCallbackDatafono {

  void responseDatafono(JSONArray response);
  void disconnect();
  void getDevices(JSONArray devices);

  void connect();
}
