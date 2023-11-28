// REQUIRE PACKAGES
const httpStatus = require('http-status');

// REQUIRE MODELS
const { userModel } = require('../models');

// REQUIRE UTILS
const apiError = require('../utils/apiError');

/**
 * Creates a new user.
 *
 * @function
 * @async
 * @param {Object} userBody - The data for creating a new user.
 * @returns {Promise<user>} A promise that resolves to the created user.
 * @throws {apiError} If the email is already taken, returns a 400 Bad Request error.
 */
const createUser = async (userBody) => {
    if (await userModel.isEmailTaken(userBody.email)) {
        throw new apiError(httpStatus.BAD_REQUEST, 'Email already taken');
    }

    const user = new userModel(userBody);
    await user.save();

    return user;
};

/**
 * Get list of users based on query details
 * 
 * @function
 * @async
 * @param {Object} filter - MongoDB filter
 * @returns {Promise<QueryResult>} - Promise that resolved to the query result.
 */
const getUsers = async (filter) => {
    const users = await userModel.find(filter);
    return users
};

/**
 * Get a user by ID
 * 
 * @function
 * @async
 * @param {string} userId - The user's email
 * @returns {Promise<user>} - Promise that resolved to the retrieved user.
 */
const getUser = async (userId) => {
    return userModel.findById(userId);
};

/**
 * Update user details by ID
 * 
 * @function
 * @async
 * @param {string} userId - The user's ID
 * @param {Object} updatedUserBody - The data to update.
 * @returns {Promise<user>} - Promise that resolved to the updated user.
 * @throws {apiError} If the user is not found or the email is already taken, returns a 404 Not Found or 400 Bad Request error, respectively.
 */
const updateUser = async (userId, updatedUserBody) => {
    const user = await getUser(userId);

    if (!user) {
        throw new apiError(httpStatus.NOT_FOUND, 'User not found');
    }

    if (updatedUserBody.email && (await userModel.isEmailTaken(updatedUserBody.email, userId))) {
        throw new apiError(httpStatus.BAD_REQUEST, 'Email already taken');
    }

    Object.assign(user, updatedUserBody);
    await user.save();
    return user;
};

/**
 * Delete user details by ID
 * 
 * @function
 * @async
 * @param {string} userId - The user's ID
 * @returns {Promise<user>} - Promise that resolved to the retrieved user.
 */
const deleteUser = async (userId) => {
    const user = await getUser(userId);

    if (!user) {
        throw new apiError(httpStatus.NOT_FOUND, 'User not found');
    }

    await userModel.deleteOne({ _id: userId });

    return;
};

/**
 * Get user ID for a given user name
 * 
 * @function
 * @async
 * @name getUserId
 * @param {string} userName - The user's name
 * @returns {Promise<userId>} - Promise that resolved to the retrieved user ID.
 */
const getUserId = async (userName) => {
    const user = await userModel.findOne({ name: userName });

    return {
        userId: user?.id
    };
};

module.exports = {
    getUsers,
    createUser,
    getUser,
    updateUser,
    deleteUser,
    getUserId
};
