// REQUIRE PACKAGES
const httpStatus = require('http-status');

// REQUIRE SERVICES
const { userService, messageService } = require('../services');

// REQUIRE UTILS
const catchAsync = require('../utils/catchAsync');

/**
 * Creates a new message based on the request body.
 *
 * @function
 * @async
 * @name createMessage
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves when the message is created and the response is sent.
 * @throws {Error} If there is an issue creating the message or sending the response.
 */
const createMessage = catchAsync(async (req, res) => {
    await messageService.createMessage(req.body);  
    res.status(httpStatus.CREATED).send('Message created successfully');
});

/**
 * Get messages for the given user ID.
 *
 * @function
 * @async
 * @name getMessage
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves with messages for the user.
 * @throws {Error} If there is an issue getting messages for the user or sending the response.
 */
const getMessage = catchAsync(async (req, res) => {
    const user = await userService.getUser(req.query.userId);
    if (!user) {
        res.status(httpStatus.NOT_FOUND).send('User not found');
    } else {
        const messages = await messageService.getMessage(req.query.userId);
        res.status(httpStatus.OK).send(messages);
    }
});

/**
 * Get messages metrics for the given user ID.
 *
 * @function
 * @async
 * @name getMetrics
 * @param {Object} req - Express request object.
 * @param {Object} res - Express response object.
 * @returns {Promise<void>} Promise that resolves with metrics for the user.
 * @throws {Error} If there is an issue getting metrics for the user or sending the response.
 */
const getMetrics = catchAsync(async (req, res) => {
    const user = await userService.getUser(req.query.userId);
    if (!user) {
        res.status(httpStatus.NOT_FOUND).send('User not found');
    } else {
        const metrics = await messageService.getMetrics(req.query.userId, req.query.responseType);
        res.status(httpStatus.OK).send(metrics);
    }
});

module.exports = {
    createMessage,
    getMessage,
    getMetrics
};