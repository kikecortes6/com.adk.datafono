
module.exports = (function() {

  var _test = function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "test", []);
    };
  var _opengps =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "openGps", []);
    };
  var _subscribe =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "Subscribe", []);
    };
  
   return {
    test: _test,
    openGps: _opengps,
    subscribe:_subscribe
  

  };

})();
