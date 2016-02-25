var express      = require('express');
var config       = require('config-node')();
var morgan       = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser   = require('body-parser');
var session      = require('express-session');
var mongoose     = require('mongoose');
var passport     = require('passport');
var flash        = require('connect-flash');

//Get GPS Model
var buildingModel = require('./buildingModel');
//Read in object from file
var jsondata = require('./map.json');

mongoose.connect(config.mongodb);
require('./config/passport')(passport);

var app = express();

//Remove previous entries
buildingModel.collection.remove();
//Insert data from json file
buildingModel.collection.insert(jsondata.GPSData, onInsert);


function onInsert(err, docs) {
    if (err) {
        console.log("Error on inserting Building Entries");
    } else {
        console.info('%d Building Entries added.', docs.insertedCount);
    }
}



//Setup Express
app.use(morgan('dev')); // log every request to the console
app.use(cookieParser()); // read cookies (needed for auth)
app.use(bodyParser.json()); // get information from html forms
app.use(bodyParser.urlencoded({ extended: true }));
    
// required for passport
app.use(session({ 
                secret: 'deadbeefisnumbertwo', // session secret
                saveUninitialized: true,
                resave: true })); 
app.use(passport.initialize());
app.use(passport.session()); // persistent login sessions
app.use(flash());
    
var userRoutes = require('./userRoutes');

//Testing working correctly
app.get('/', function (request, response) {
    response.send('Hello, deadbeef');
});

//Testing working correctly
app.get('/success', function (request, response) {
    response.send('Success, deadbeef');
});


//Auth before proceeding
var checkAuth = function(req, res, next) { 
    if (!req.isAuthenticated())  
    {
        console.log('Not Authenticated');
        res.sendStatus(401); 
    }
    else 
    {
        console.log('Authenticated');
        next(); 
    }
};


//Load Route Handlers
app.use('/users' , checkAuth, userRoutes);

//Login Route
app.post('/login', function(req, res, next) {
        passport.authenticate('local-login', function (err, user) {
           

           //Sucessfully logged in user
           if(user)
           {
                req.logIn(user, function(err) 
                {
                    if (err) { return next(err); }
                    
                    console.log("Logged In User:" + user.local.email);
                    return res.sendStatus(200);
                });
           }
           else
           {
               switch(err.ID)
               {
                   case 2:
                            console.log(err.message);
                            res.set({
                            'error': 2
                            });
                            res.sendStatus(400);
                            break;
                   case 3:
                            console.log(err.message);
                            res.set({
                            'error': 3
                            });
                            res.sendStatus(400);
                            break;
                  default:
                            res.sendStatus(400);
                            break;         
               }
           }     
  })(req, res, next);
});


//Create User Route
app.post('/signup', function(req, res, next) {
        passport.authenticate('local-signup', function (err, user) {
         
           //Sucessfully created user
           if(user)
           {
               console.log("Created User:" + user);
               res.sendStatus(200); 
           }
           else
           {
               console.log(err.message);
               res.set({
                   'error': 1
               });
               res.sendStatus(400);
           }
           
  })(req, res, next);
});

app.listen(3000, function () {
    console.log('Middle-Tier Listening on Port 3000');
});