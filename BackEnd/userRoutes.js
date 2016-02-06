var express = require('express');

var userRoutes = module.exports = express();

//User Account Handling
userRoutes.post('/reset', function (request, response) {
    response.send('Hello World');
});

//User Account Handling
userRoutes.post('/checkin', function (request, response) {
    response.send('Hello World');
});

//User Account Handling
userRoutes.post('/updatelocation', function (request, response) {
    response.send('Hello World');
});

userRoutes.post('/logout', function (request, response) {
    request.logOut();
    response.send(200);
});