// REQUIRE ROUTE HANDLER
const express = require("express");
const router = express.Router();

// REQUIRE CONTROLLERS
const { messageController } = require("../../controllers");

// CREATE REQUEST FOR MESSAGE
router.post("/", messageController.createMessage);

// GET REQUEST FOR MESSAGE
router.get("/", messageController.getMessage);

// GET REQUEST FOR MESSAGE METRICS
router.get("/metrics", messageController.getMetrics);

module.exports = router;