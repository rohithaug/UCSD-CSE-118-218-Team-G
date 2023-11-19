// REQUIRE ROUTE HANDLER
const express = require("express");
const router = express.Router();

// REQUIRE CONFIG
const { config } = require("../../../config");

// REQUIRE ROUTES
const userRoutes = require("./user.route");

module.exports = (app) => {
    // BASE URI
    app.get(`${config.api.basePath}`, (req, res) => {
        res.send("UCSD CSE 118/218 Project Server Backend API Base URL");
    })

    // USER ROUTES
    router.use('/user', userRoutes);

    // REGISTER API ROUTES
    app.use(`${config.api.basePath}`, router);
};