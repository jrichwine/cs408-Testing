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

//Auth before proceeding
var checkAuth = function(req, res, next) { 
    if (!req.isAuthenticated())  
    {
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

app.get('/login', function(req, res) {
    res.send('<form action="/login" method="post">'
        + '<p>Email: <input type="text" name="email" placeholder="title" /></p>'
        + '<p>Password: <input type="password" name="password" /></p>'
        + '<p><input type="submit" value="login" /></p>'
        + '</form>'
    );
});

app.post('/login', passport.authenticate('local-login', {
        successRedirect : '/test/upload', // redirect to the secure profile section
        failureRedirect : '/signup', // redirect back to the signup page if there is an error
        failureFlash : true // allow flash messages
}));

app.get('/signup', function(req, res) {   
    res.send(
          '<form action="/signup" method="post">'
        + '<p>Email: <input type="text" name="email" placeholder="email" /></p>'
        + '<p>Password: <input type="password" name="password" /></p>'
        + '<p><input type="submit" value="signup" /></p>'
        + '</form>'
    );
});

app.post('/signup', passport.authenticate('local-signup', {
        successRedirect : '/test/upload', // redirect to the secure profile section
        failureRedirect : '/signup', // redirect back to the signup page if there is an error
        failureFlash : true // allow flash messages
    }));

app.listen(3000, function () {
    console.log('Middle-Tier Listening on Port 3000');
});