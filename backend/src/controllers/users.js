const { validationResult } = require('express-validator');

const database = require('../db');
const authorization = require('../controllers/authorization');

const getUser = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());

    const sqlQuery = `SELECT user_username, user_role, user_money, user_firstname, user_lastname, user_email, \
        (SELECT DATE_FORMAT(user_birthdate, '%d-%m-%Y')) AS user_birthdate, user_sexe, user_country, user_city, \
        user_address, user_house_number, user_zipcode FROM users WHERE user_username = '${req.params.username}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(520);
        res.json(result);
    });
};

const logUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());

    const sqlQuery = `SELECT user_username FROM users WHERE user_username = '${req.body.username}' \
        AND user_password = '${req.body.password}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(520);

        const username = result[0].user_username;
        const accessToken = authorization.generateAccessToken(username);
        const refreshToken = authorization.generateRefreshToken(username);
        
        const sqlQuery = `INSERT INTO tokens (token) VALUES ('${refreshToken}');`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.sendStatus(520);
            res.json([{ 
                user_username: username,
                accessToken: accessToken,
                refreshToken: refreshToken
            }]);
        });
    });
};

const signUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());

    const sqlQuery = `INSERT INTO users (user_username, user_password, user_firstname, user_lastname, user_email, \
        user_birthdate, user_sexe, user_country, user_city, user_address, user_house_number, user_zipcode) VALUES \
        ('${req.body.username}', '${req.body.password}', '${req.body.firstname}', '${req.body.lastname}', '${req.body.email}', \
        '${req.body.birthdate}', '${req.body.sexe}', '${req.body.country}', '${req.body.city}', '${req.body.address}', \
        ${req.body.house_number}, ${req.body.zipcode});`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(400);

        const username = req.body.username;
        const accessToken = authorization.generateAccessToken(username);
        const refreshToken = authorization.generateRefreshToken(username);

        const sqlQuery = `INSERT INTO tokens (token) VALUES ('${refreshToken}');`;

        database.query(sqlQuery, (err, result) => { if (err) res.sendStatus(520); });

        if(!result) res.json(result);
        res.json({
            accessToken: accessToken,
            refreshToken: refreshToken
        });
    });
};

const logUserOut = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0)  res.send(errors.array());

    const sqlQuery = `DELETE * FROM tokens WHERE token = '${req.body.refreshToken}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(520);

        res.json({ "msg": "logged out" });
    });
};

const amendPwd = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0)  res.send(errors.array());

    const sqlQuery = `UPDATE users SET user_password = '${req.body.password}' where user_username = '${req.body.username}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(400);
        
        res.json(result);
    });
};

const amendName = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0)  res.send(errors.array());

    const sqlQuery = `UPDATE users SET user_firstname = '${req.body.firstname}', user_lastname = '${req.body.lastname}' where \
        user_username = '${req.body.username}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(400);
        
        res.json(result);
    });
};

const amendEmail = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());

        const sqlQuery = `UPDATE users SET user_email = '${req.body.email}' where user_username = '${req.body.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) res.sendStatus(400);
            
            res.json(result);
        });
};

const amendAddress = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) res.send(errors.array());

    const sqlQuery = `UPDATE users SET user_country = '${req.body.country}', user_city = '${req.body.city}', \
        user_address = '${req.body.address}', user_house_number = ${req.body.house_number}, user_zipcode = ${req.body.zipcode} \
        where user_username = '${req.body.username}';`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(400);
        
        res.json(result);
    });
};


module.exports = {
    getUser,
    signUserIn,
    logUserIn,
    amendPwd,
    amendName,
    amendEmail,
    amendAddress,
    logUserOut
}
