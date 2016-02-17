var mongoose = require('mongoose');

//Export GPS Info to Database
var Schema = mongoose.Schema;
var BuildingEntrySchema = new Schema({
    BuildingName: String,
    Category: String,
    Coordinates: [Number],
    TotalCapacity: Number,
    CurrentCapacity: Number
});


module.exports = mongoose.model('Buildings', BuildingEntrySchema);
