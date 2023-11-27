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
    const res = await messageModel.aggregate([
            {
                $match: { userId }
            },
            {
                $project: {
                    messages: {
                    $filter: {
                        input: '$messages',
                        as: 'message',
                        cond: { $eq: ['$$message.readStatus', false] }
                    }
                    }
                }
            }
        ]);

    if (!res || res.length == 0 || !res[0].messages) {
        return []
    } else {
        messages = res[0].messages.map(item => {
            return {
                from: item.from,
                to: item.to,
                message: item.message
            }
        });

        // Update the readStatus of these filtered messages to true
        const messagesIdArray = res[0].messages.map(item => item._id);
        await messageModel.updateMany(
            { '_id': res[0]._id },
            { $set: { 'messages.$[elem].readStatus': true } },
            { arrayFilters: [{ 'elem._id': { $in: messagesIdArray } }] }
        );

        return messages;
    }
};

/**
 * Get metrics of messages for an user ID
 * 
 * @function
 * @async
 * @name getMetrics
 * @param {string} userId - The user's ID
 * @param {'object'|'text'} responseType - Response Type
 * @returns {Promise<user>} - Promise that resolved to the retrieved messages.
 */
const getMetrics = async (userId, responseType) => {
    const res = await messageModel.aggregate([
            {
                $match: { userId }
            },
            {
                $project: {
                    messages: {
                    $filter: {
                        input: '$messages',
                        as: 'message',
                        cond: { $eq: ['$$message.readStatus', false] }
                    }
                    }
                }
            }
        ]);

    if (!res || res.length == 0 || !res[0].messages) {
        if (responseType == 'text') {
            return "There are no messages."
        } else {
            return {
                total: 0,
                messages: []
            }
        }
    } else {
        const result = {};
        res[0].messages.forEach(item => {
            if (item.from in result) {
                result[item.from] += 1;
            } else {
                result[item.from] = 0;
            }
        });

        // Get user names of all IDs
        const userIdArray = Object.keys(result);
        const userDetails = await userModel.find({ '_id': { $in: userIdArray } });
        const userNames = {}
        userDetails.forEach((item) => {
            userNames[item._id] = item.name
        });

        const totalMessages =  Object.values(result).reduce((a, b) => a + b, 0);
        const resultEntries = Object.entries(result);
        if (responseType == 'text') {
            let responseText = `There are in total ${totalMessages} messages`;

            resultEntries.forEach(([key, value], idx) => {
                if (idx == resultEntries.length - 1) {
                    responseText = responseText.concat(` and ${value} messages from ${userNames[key]}.`);
                } else {
                    responseText = responseText.concat(`, ${value} messages from ${userNames[key]}`);
                }
            });

            return responseText;
        } else {
            const metrics = {
                total: totalMessages,
                messages: []
            };

            resultEntries.forEach(([key, value]) => {
                metrics.messages.push({
                    userId: key,
                    userName: userNames[key],
                    count: value
                })
            });

            return metrics;
        }
    }
};

module.exports = {
    createMessage,
    getMessage,
    getMetrics
};
