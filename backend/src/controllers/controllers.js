const { validationResult } = require('express-validator');
const database = require('../db');

const getUser = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `SELECT * FROM users WHERE user_username = '${req.params.username}'`;

        try {
            database.query(sqlQuery, (err, result) => {
                if (err) res.status(520);
                
                res.json(result);
            });
        }
        catch(err) {
            res.status(520);
        }
    }
};

const logUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `SELECT * FROM users WHERE user_username = '${req.body.username}' AND user_password = '${req.body.password}'`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(520);
            
            res.json(result);
        });
    }
};

const signUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `INSERT INTO users (user_username, user_password, user_firstname, user_lastname, user_email, user_birthdate, user_sexe, user_country, user_city, user_address, user_house_number, user_zipcode) VALUES ('${req.body.username}', '${req.body.password}', '${req.body.firstname}', '${req.body.lastname}', '${req.body.email}', '${req.body.birthdate}', '${req.body.sexe}', '${req.body.country}', '${req.body.city}', '${req.body.address}', ${req.body.house_number}, ${req.body.zipcode});`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(400);
            
            res.json(result);
        });
    }
};

const amendPwd = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `UPDATE users SET user_password = '${req.body.password}' where user_username = '${req.body.username}'`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(400);
            
            res.json(result);
        });
    }
};

const amendName = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `UPDATE users SET user_firstname = '${req.body.firstname}', user_lastname = '${req.body.lastname}' where user_username = '${req.body.username}'`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(400);
            
            res.json(result);
        });
    }
};

const amendEmail = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `UPDATE users SET user_email = '${req.body.email}' where user_username = '${req.body.username}'`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(400);
            
            res.json(result);
        });
    }
};

const amendAddress = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `UPDATE users SET user_country = '${req.body.country}', user_city = '${req.body.city}', user_address = '${req.body.address}', user_house_number = ${req.body.house_number}, user_zipcode = ${req.body.zipcode} where user_username = '${req.body.username}'`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.status(400);
            
            res.json(result);
        });
    }
};

const getUserSettings = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) {
        res.send(errors.array());
    } else {
        const sqlQuery = `SELECT * FROM settings WHERE user_username = '${req.params.username}'`;

        try {
            database.query(sqlQuery, (err, result) => {
                if (err) res.status(520);
                
                res.json(result);
            });
        }
        catch(err) {
            res.status(520);
        }
    }
};

module.exports = {
    getUser,
    signUserIn,
    logUserIn,
    amendPwd,
    amendName,
    amendEmail,
    amendAddress,
    getUserSettings
}
