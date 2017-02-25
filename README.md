# com.ci24.datafono

plugin for Bluetooth payment communication

## Getting Started

This proyect is to acomplish bluetooth communication between genico miniDatafono and ionic framework.

### Prerequisites

Cordova 5.0 or Later
Ionic 1.2 or Later
for Android 4.4 or later

### Installing

cordova plugin add https://github.com/kikecortes6/com.ci24.datafono.git

## Methods

These are functions that you can call into your ionic project

### datacom.connect

>-datacom.connect(
>-function (data) {
>- //Conecction successfull 
>- },function (err) { 
>- //Error trying to connect console.log(err); 
>- });

### datacom.checkConnection

datacom.checkConnection(
function (data) { 
//Connected 
},function (err) { 
//not Conected console.log(err); 
})


###  datacom.disconnect

datacom.disconnect(
function (data) { 
//Disconecction successfull 
},function (err) { 
//Can not Disconnect console.log(err); 
});

## datacom.transactionEX

var args= {
"Amount": "",
"CurrencyCode": "",
"Operation": "",
"TermNum": "",
"AuthorizationType": "",
"CtrlCheque": "",
"UserData1": "",
"AppNumber": 0,
"Trama":["252","50","220","1","10","223","254","64","6","0","0","0","50","130","89","223","255","34","6","0","0","0","5","36","17","223","254","130","6","0","0","0","0","0","0","223","255","37","4","49","50","53","48","223","255","43","5","52","48","53","55","50"]};


datacom.transactionEX(args,function (data) {
  //return a json with the outbuffer 
  console.log(data);
},function (err) {
//Error running transaction
  console.log(err);
});


## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
