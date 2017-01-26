
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
  
   return {
    connect: _connect,
    checkConnection: _checkConnection,
    disconnect:_disconnect
  

  };

})();
