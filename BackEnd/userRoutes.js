var express = require('express');

var userRoutes = module.exports = express();

//Handle User Reset Password
userRoutes.post('/reset', function (request, response) {
    response.send('Hello World');
});

//Handle Checkin Request
userRoutes.post('/checkin', function (request, response) {
    //Get GPS Coords from send data
    //match gps coords up to entries for buildings in database
    
    //set current user location to building
    //update database statistics for that building
    //send data back
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