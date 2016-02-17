var express = require('express');
var buildingModel = require('./buildingModel');

var userRoutes = module.exports = express();

//Handle User Reset Password
userRoutes.post('/reset', function (request, response) {
    response.send('Hello World');
});

//Handle Checkin Request
userRoutes.post('/checkin', function (request, response) {
    console.log("Checked In User:" + request.user.local.email);
    response.sendStatus(200);
    //set current user location to building
    //update database statistics for that building
    //send data back
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