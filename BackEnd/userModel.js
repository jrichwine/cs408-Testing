var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

// define the schema for our user model
var userSchema = mongoose.Schema({
    local            : {
        email        : String,
        password     : String,
        building     : String,
        checkTime    : Date
    }
});

// generating a hash
userSchema.methods.generateHash = function(password) {
    var encrypt = password;
    bcrypt.hashSync(encrypt, bcrypt.genSaltSync(8), null);
    return password;
};

// checking if password is valid
userSchema.methods.validPassword = function(password) {
    var encrypt = password;
    bcrypt.compareSync(encrypt, this.local.password);
    return password;
};

// create the model for users and expose it to our app
module.exports = mongoose.model('User', userSchema);