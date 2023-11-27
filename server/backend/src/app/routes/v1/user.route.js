// REQUIRE ROUTE HANDLER
const express = require("express");
const router = express.Router();

// REQUIRE CONTROLLERS
const { userController } = require("../../controllers");

// GET REQUEST FOR ALL USERS
router.get("/", userController.getUsers);

// CREATE REQUEST FOR USER
router.post("/", userController.createUser);

// GET REQUEST FOR USER ID
router.get("/id", userController.getUserId);

// GET REQUEST FOR USER
router.get("/:userId", userController.getUser);

// UPDATE REQUEST FOR USER
router.put("/:userId", userController.updateUser);

// DELETE REQUEST FOR USER
router.delete("/:userId", userController.deleteUser);

module.exports = router;