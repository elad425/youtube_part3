const User = require('../models/userModel');

const validate_token = async (req, res) => {
    try {
        const user = await User.findById(req.user.userId)
        if (!user) {
            return res.sendStatus(404);
        }
        res.status(200).json(user);
    } catch (error) {
        res.sendStatus(500);
    }
};

module.exports = {
    validate_token,
}