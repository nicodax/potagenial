const { validationResult } = require('express-validator');

const database = require('../db');

const getUserSettings = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT user_username, camera_id, sonde_id, settings_temperature_outside, settings_temperature_ground, \
            settings_humidity, (SELECT DATE_FORMAT(settings_last_sprinkling, '%d-%m-%Y')) AS settings_last_sprinkling, \
            settings_last_sprinkling_quantity, settings_automatic_sprinkling, settings_automatic_sprinkling_frequency FROM settings \
            WHERE user_username = ?;`;
            
        if(req.params.username.match(/^[0-9a-zA-Z]+$/)) {
        	database.query(sqlQuery, [req.params.username], (err, result) => {
		    if (err) { res.sendStatus(520); }
		    else { res.json(result); }
		});
        }
        else {
        	res.sendStatus(400);
        }
    }
};


const postUserSettings = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());
    else {
        const sqlQuery = `UPDATE settings SET settings_automatic_sprinkling = ?, \
            settings_automatic_sprinkling_frequency = ? WHERE \
            user_username = ?;`;
        if(req.params.username.match(/^[0-9a-zA-Z]+$/) && req.body.automatic_sprinkling.match(/^[0-9a-zA-Z]+$/) && req.body.automatic_sprinkling_frequency.match(/^[0-9a-zA-Z]+$/)) {
                database.query(sqlQuery,[req.body.automatic_sprinkling, req.body.automatic_sprinkling_frequency, req.params.username], (err, result) => {
            
                    if (err) { res.sendStatus(400); }
                    else { res.json(result); }
                })
        }
        else {
        	res.sendStatus(400);
        }
    }
};

const postSondeSettings = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE settings SET settings_temperature_outside = ?, \
            settings_temperature_ground = ?, settings_humidity = ?, \
            settings_last_sprinkling = (SELECT STR_TO_DATE(?, '%d-%m-%Y')), \
            settings_last_sprinkling_quantity = ? WHERE sonde_id = ?;`;
        
        if(req.body.settings_temperature_outside.match(/^[0-9a-zA-Z]+$/) &&
            req.body.settings_temperature_ground.match(/^[0-9a-zA-Z]+$/) && 
            req.body.settings_humidity.match(/^[0-9a-zA-Z]+$/) && 
            req.body.settings_last_sprinkling.match(/^[0-9a-zA-Z\-]+$/) &&
            req.body.settings_last_sprinkling_quantity.match(/^[0-9a-zA-Z]+$/) && 
            req.params.sonde_id.match(/^[0-9a-zA-Z]+$/)){
                database.query(sqlQuery,[req.body.settings_temperature_outside, 
                    req.body.settings_temperature_ground, 
                    req.body.settings_humidity, 
                    req.body.settings_last_sprinkling, 
                    req.body.settings_last_sprinkling_quantity, 
                    req.params.sonde_id], (err, result) => {
            
                    if (err) { res.sendStatus(400); }
                    else { res.json(result); }
                });

        }else{
            res.sendStatus(400);
        }

    }
};

module.exports = {
    getUserSettings,
    postUserSettings,
    postSondeSettings
}
