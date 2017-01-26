
module.exports = (function() {

  var _connect = function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "init", []);
    };
  var _checkConnection =function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "check", [args]);
    };
  var _disconnect =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "finish", []);
    };
  var _fFunction = function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "f", []);
    };
  var _sFunction =function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "s", [args]);
    };
  var _tFunction =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "t", []);
    };
  
   return {
    connect: _connect,
    checkConnection: _checkConnection,
    disconnect:_disconnect,
    fFunction:_fFunction,
    sFunction:_sFunction,
    tFunction:_tFunction
  

  };

})();
