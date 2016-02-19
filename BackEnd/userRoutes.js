var express = require('express');
var buildingModel = require('./buildingModel');

var User            = require('./userModel');
var buildingModel = require('./buildingModel');

var userRoutes = module.exports = express();

//Handle User Reset Password
userRoutes.post('/reset', function (request, response) {
    response.send('Hello World');
});

//Handle Checkin Request
userRoutes.post('/checkin', function (request, response) {
    //console.log(request.body);
    //console.log("Checked In User:" + request.user.local.email);
    
    
    //Todo
    //Add error handling if already checked in?? or handle that in the app?
    
    
    User.findOne({'local.email': request.user.local.email}, function(err, user){
        
        user.local.building = request.body.building;
        user.local.checkTime = Date.now();
        user.save(function(err) {
                    if (err)
                        throw err;
                    else
                        {
                            console.log("Check In User: " + request.user.local.email + "\nAt: " + user.local.checkTime + "\nTo: " + user.local.building);
                            
                            updateCapacity(user.local.building);
                            response.sendStatus(200);
                        }
                });
    });
    
});


//Handle user logout
userRoutes.post('/checkOut', function (request, response) {
    //Checkout if in current location
    
    //Destroy current session
    request.logOut();
    response.send(200);
});


//Handle user logout
userRoutes.post('/refreshCapacity', function (request, response) {

    //Have an async task on app that fires every so often to get currentCapacity for buildings?
    
    //Destroy current session
    request.logOut();
    response.send(200);
});








//Handle After Authentication
userRoutes.get('/getBuildings', function (request, response) {
    console.log("Getting Buildings For:" + request.user.local.email);
    
    //Read Building Data from Database
    buildingModel.collection.find().toArray(function (err, docs) {
         response.send(docs);
    });


});

//Handle updated information sent every 15 minutes?
userRoutes.post('/updatelocation', function (request, response) {
    //Get GPS Coords from sent data
    //Compare to previously saved user building
    //if different, checkout of that building
    //calculate to see if somewhere else
    //send database
});

//Handle user logout
userRoutes.post('/logout', function (request, response) {
    //Checkout if in current location
    
    //Destroy current session
    request.logOut();
    response.send(200);
});


function updateCapacity(buildingName)
{
    buildingModel.findOne({'BuildingName': buildingName}, function(err, building) {
        
        building.CurrentCapacity += 1;
        building.save(function(err) {
                if (err)
                        throw err;
                    else
                        {
                            console.log("Updated Current Capacity of: " + building.BuildingName + " To: " + building.CurrentCapacity);
                        }
                });
    });
    
}





