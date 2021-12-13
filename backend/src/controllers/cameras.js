const { validationResult } = require('express-validator');

const database = require('../db');

const getCamera = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT * FROM cameras WHERE camera_id = '${req.params.camera_id}'`
        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(520); }
            else { res.json(result); }
        });
    }
};


module.exports = {
    getCamera
}
