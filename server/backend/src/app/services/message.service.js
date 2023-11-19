// REQUIRE PACKAGES
const httpStatus = require('http-status');

// REQUIRE MODELS
const { userModel, messageModel } = require('../models');

// REQUIRE UTILS
const apiError = require('../utils/apiError');

/**
 * Creates a new message.
 *
 * @function
 * @async
 * @name createMessage
 * @param {Object} messageBody - The data for creating a new message.
 * @returns {Promise<user>} A promise that resolves to the created message or throws an error.
 */
const createMessage = async (messageBody) => {
    if (!messageBody.from.match(/^[0-9a-fA-F]{24}$/) || !await userModel.findById(messageBody.from)) {
        throw new apiError(httpStatus.BAD_REQUEST, `from user id "${messageBody.from}" not found`);
    }

    if (!messageBody.to.match(/^[0-9a-fA-F]{24}$/) || !await userModel.findById(messageBody.to)) {
        throw new apiError(httpStatus.BAD_REQUEST, `to user id "${messageBody.to}" not found`);
    }

    const filter = { userId: messageBody.to };
    const update = {
        $push: {
            messages: messageBody
        }
    };
    
    const options = {
        upsert: true, // Creates a new document if no document matches the filter
        new: true,    // Returns the modified document if a document was found and updated
        setDefaultsOnInsert: true // Sets default values for fields when inserting a new document
    };
    
    let res = await messageModel.findOne(filter);
    
    if (!res) {
        // If the document doesn't exist, create a new one
        const message = new messageModel({
            userId: messageBody.to,
            messages: [
                messageBody
            ]
        });
        res = await message.save();
    } else {
        // If the document exists, update it
        res = await messageModel.findOneAndUpdate(filter, update, options);
    }    

    return;
};

/**
 * Get a list of messages for an user ID
 * 
 * @function
 * @async
 * @name getMessage
 * @param {string} userId - The user's ID
 * @returns {Promise<user>} - Promise that resolved to the retrieved messages.
 */
const getMessage = async (userId) => {
    const res = await messageModel.findOne({ userId });

    if (!res) {
        return []
    } else {
        const filteredMessages = res.messages.filter(item => !item.readStatus);

        const messages = filteredMessages.map(item => {
            return {
                from: item.from,
                to: item.to,
                message: item.message
            }
        });

        return messages;
    }
};

module.exports = {
    createMessage,
    getMessage
};