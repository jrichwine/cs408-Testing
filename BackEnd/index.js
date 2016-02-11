var express      = require('express');
var config       = require('config-node')();
var morgan       = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser   = require('body-parser');
var session      = require('express-session');
var mongoose     = require('mongoose');
var passport     = require('passport');
var flash        = require('connect-flash');

mongoose.connect(config.mongodb);
require('./config/passport')(passport);

var app = express();

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

//Change this later
app.post('/login', function(req, res, next) {
        passport.authenticate('local-login', function (err, user, info) {
           console.log("Error:" + err);
           console.log("User:" + user);
           console.log("Info:" + info);

           //Sucessfully created user
           if(user)
           {
                req.logIn(user, function(err) 
                {
                    if (err) { return next(err); }
                    return res.sendStatus(200);
                    //return res.redirect('/users/' + user.username);
                });
               
           }
           else
           {
               res.sendStatus(400);
           }
           
        
           
        
  })(req, res, next);
});

app.post('/signup', function(req, res, next) {
        passport.authenticate('local-signup', function (err, user, info) {
           console.log("Error:" + err);
           console.log("User:" + user);
           console.log("Info:" + info);

           //Sucessfully created user
           if(user)
           {
               res.sendStatus(200); 
           }
           else
           {
               res.sendStatus(400);
           }
           
           /*req.logIn(user, function(err) {
                if (err) { return next(err); }
                return res.redirect('/users/' + user.username);
                });*/
           
        
  })(req, res, next);
});

app.listen(3000, function () {
    console.log('Middle-Tier Listening on Port 3000');
});