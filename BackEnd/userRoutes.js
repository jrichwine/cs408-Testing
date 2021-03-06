var express = require('express');
var buildingModel = require('./buildingModel');
var User = require('./userModel');

var userRoutes = module.exports = express();

//Handle Checkin Request
userRoutes.post('/checkin', function (request, response) {

    User.findOne({ 'local.email': request.user.local.email }, function (err, user) {

        user.local.building = request.body.building;
        user.local.checkTime = Date.now();

        user.save(function (err) {
            if (err)
                throw err;
            else {
                console.log("Check In User: " + request.user.local.email + " To: " + user.local.building);

                updateCapacity(user.local.building, true);
                response.sendStatus(200);
            }
        });
    });
});


//Handle CheckOut
userRoutes.get('/checkOut', function (request, response) {

    User.findOne({ 'local.email': request.user.local.email }, function (err, user) {

        var building = user.local.building;
        
        //Can't checkout cause user isnt checkedin
        if(building == "none"){
             response.sendStatus(400);
             return;
        }
      
        
        user.local.checkTime = null;
        user.local.building = "none";

        user.save(function (err) {
            if (err)
                throw err;
            else {
                console.log("Checked Out User: " + request.user.local.email + " From: " + building);

                updateCapacity(building, false);
                response.sendStatus(200);
            }
        });
    });
});


//Send updated building statistics to App
userRoutes.get('/refreshCapacity', function (request, response) {

    console.log("Refreshing Current Capacities For:" + request.user.local.email);
    
    //Read Total Capacity from DB
    buildingModel.collection.find({}, { BuildingName: true, CurrentCapacity: true }).toArray(function (err, docs) {
        response.send(docs);
    });
});


//Get all buildings from the database and send to the App
userRoutes.get('/getBuildings', function (request, response) {

    console.log("Getting Buildings For:" + request.user.local.email);
    
    //Read Building Data from Database
    buildingModel.collection.find().toArray(function (err, docs) {
        response.send(docs);
    });
});




//Update building capacity in database
function updateCapacity(buildingName, status) {
    buildingModel.findOne({ 'BuildingName': buildingName }, function (err, building) {

        if (status == true)
            building.CurrentCapacity += 1;
        else
            building.CurrentCapacity -= 1;

        building.save(function (err) {
            if (err)
                throw err;
            else {
                console.log("Updated Current Capacity of: " + building.BuildingName + " To: " + building.CurrentCapacity);
            }
        });
    });
}