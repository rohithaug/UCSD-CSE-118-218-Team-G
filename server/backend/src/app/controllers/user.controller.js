// REQUIRE PACKAGES
const httpStatus = require('http-status');

// REQUIRE SERVICES
const { userService } = require('../services');

// REQUIRE UTILS
const catchAsync = require('../utils/catchAsync');
const pick = require('../utils/pick');

/**
 * Get list of users based on filter and option details from query.
 *
 * @function
 * @async
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves with list of user details.
 * @throws {Error} If there is an issue getting the list of users or sending the response.
 */
const getUsers = catchAsync(async (req, res) => {
    const filter = pick(req.query, ['name', 'role']);
    const result = await userService.getUsers(filter);
    res.status(httpStatus.OK).send(result);
});

/**
 * Creates a new user based on the request body.
 *
 * @function
 * @async
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves when the user is created and the response is sent.
 * @throws {Error} If there is an issue creating the user or sending the response.
 */
const createUser = catchAsync(async (req, res) => {
    const user = await userService.createUser(req.body);  
    res.status(httpStatus.CREATED).send(user);
});

/**
 * Get user details for the given user ID.
 *
 * @function
 * @async
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves with user details.
 * @throws {Error} If there is an issue getting user details or sending the response.
 */
const getUser = catchAsync(async (req, res) => {
    const user = await userService.getUser(req.params.userId);
    if (!user) {
        res.status(httpStatus.NOT_FOUND).send('User not found');
    } else {
        res.status(httpStatus.CREATED).send(user);
    }
});

/**
 * Updates an existing user based on the request parameters and body.
 *
 * @function
 * @async
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves when the user is updated and the response is sent.
 * @throws {Error} If there is an issue updating the user or sending the response.
 */
const updateUser = catchAsync(async (req, res) => {
    const user = await userService.updateUser(req.params.userId, req.body);  
    res.send(user);
});
  
/**
 * Deletes an existing user based on the request parameters.
 *
 * @function
 * @async
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves when the user is deleted and the response is sent.
 * @throws {Error} If there is an issue deleting the user or sending the response.
 */
const deleteUser = catchAsync(async (req, res) => {
    await userService.deleteUser(req.params.userId);
    res.status(httpStatus.NO_CONTENT).send();
});

module.exports = {
    getUsers,
    createUser,
    getUser,
    updateUser,
    deleteUser,
};