const { validationResult } = require('express-validator');

const database = require('../db');

const getCamera = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT * FROM cameras WHERE camera_id = ?`

        if(req.params.camera_id.match(/^[0-9a-zA-Z]+$/)){

            database.query(sqlQuery,[req.params.camera_id], (err, result) => {
                if (err) { res.sendStatus(520); }
                else { res.json(result); }
            });

        }else{
            res.sendStatus(400);

        }

    }
};


module.exports = {
    getCamera
}
