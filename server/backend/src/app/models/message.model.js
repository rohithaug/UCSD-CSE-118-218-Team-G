// REQUIRE PACKAGES
const mongoose = require('mongoose');

// REQUIRE PLUGINS
const { toJson } = require('./plugins');

const messageSchema = mongoose.Schema(
    {
        userId: {
            type: String,
            required: true
        },
        messages: [{
            from: {
                type: String,
                required: true
            },
            to: {
                type: String,
                required: true
            },
            message: {
                type: String,
                required: true
            },
            readStatus: {
                type: Boolean,
                required: true,
                default: false
            },
        }]
    },
    {
        timestamps: true
    }
)

// add plugin that converts mongoose to json
messageSchema.plugin(toJson);

/**
 * @typedef Message
 */
const Message = mongoose.model('message', messageSchema);

module.exports = Message;