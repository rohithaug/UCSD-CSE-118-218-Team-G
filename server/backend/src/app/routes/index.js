// REQUIRE CONFIG
const { config } = require("../../config");

// REQUIRE ROUTES

module.exports = (app) => {
    // BASE URI
    app.get(`${config.api.basePath}`, (req, res) => {
        res.send("UCSD CSE 118/218 Project Server Backend API Base URL");
    })
};