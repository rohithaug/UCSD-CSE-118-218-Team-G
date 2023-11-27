// REQUIRE PACKAGES
const mongoose = require('mongoose');

// REQUIRE CONFIG
const { roles } = require('../../config/roles');

// REQUIRE PLUGINS
const { toJson } = require('./plugins');

const userSchema = mongoose.Schema(
    {
        name: {
            type: String,
            required: true,
            unique: true,
        },
        email: {
            type: String,
            required: true,
            unique: true,
            lowercase: true
        },
        role: {
            type: String,
            enum: roles,
            default: 'user'
        }
    },
    {
        timestamps: true
    }
)

/**
 * Checks if an email is already taken by an existing user, excluding a specified user.
 *
 * @static
 * @memberof User
 * @param {string} email - The email address to check.
 * @param {ObjectId} excludeUserId - The user ID to exclude from the check.
 * @returns {Promise<boolean>} A promise that resolves to a boolean indicating whether the email is taken (true) or not (false).
 */
userSchema.statics.isEmailTaken = async function (email, excludeUserId) {
    const user = await this.findOne({ email, _id: { $ne: excludeUserId } });
    return !!user;
};

// add plugin that converts mongoose to json
userSchema.plugin(toJson);

/**
 * @typedef User
 */
const User = mongoose.model('user', userSchema);

module.exports = User;