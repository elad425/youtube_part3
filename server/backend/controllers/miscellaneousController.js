const mongoose = require('mongoose');

const fs = require('fs');
const path = require('path');


const deleteFileFromServer = async (req, res) => {

    try {
        const { file } = req.body;

        if (!file) {
            console.error("File not specified in request body");
            return res.sendStatus(400);
        }

        const fPath = path.join(__dirname, '..', file);

        fs.unlink(fPath, (err) => {
            if (err) {
                console.error('Error deleting file:', err);
                return res.sendStatus(400);
            } else {
                return res.sendStatus(200);
            }
        });
    } catch (error) {
        console.error('Error caught in catch block:', error);
        res.sendStatus(500);
    }
};

module.exports = {
    deleteFileFromServer,
}