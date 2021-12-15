const { validationResult } = require('express-validator');

const database = require('../db');
const authorization = require('../controllers/authorization');

const argon2i = require('argon2-ffi').argon2i;
crypto = require('crypto');

const getUser = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT user_username, user_role, user_money, user_firstname, user_lastname, user_email, \
            (SELECT DATE_FORMAT(user_birthdate, '%d-%m-%Y')) AS user_birthdate, user_sexe, user_country, user_city, \
            user_address, user_house_number, user_zipcode FROM users WHERE user_username = '${req.params.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(520); }
            else { res.json(result); }
        });
    }
};

const logUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT user_username, user_password FROM users WHERE user_username = '${req.body.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(520); }
            else if (result.length == 0) { res.json(result); }
            else {
                const passwordHash = result[0].user_password;
                try {
                    argon2i.verify(passwordHash, req.body.password).then(correct => {
                        if (correct) {
                            const username = result[0].user_username;
                            const accessToken = authorization.generateAccessToken(username);
                            const refreshToken = authorization.generateRefreshToken(username);
                            
                            const sqlQuery = `INSERT INTO tokens (token) VALUES ('${refreshToken}');`;
                            
                            try {
                                database.query(sqlQuery, (err, result) => {
                                    if (err) { res.sendStatus(520); }
                                    else {
                                        res.json([{ 
                                            user_username: username,
                                            accessToken: accessToken,
                                            refreshToken: refreshToken
                                        }]);
                                    }
                                });
                            } catch (err) {
                                res.sendStatus(520);
                            }
                        } else {
                            res.sendStatus(400);
                        }
                    })
                } catch (error) {
                    res.sendStatus(520);
                }
            }
        });
    }
};

const signUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        crypto.randomBytes(32, function(err, salt) {
            if (err) throw err;
            argon2i.hash(req.body.password, salt).then(hash => {
                const sqlQuery = `INSERT INTO users (user_username, user_password, user_firstname, user_lastname, user_email, \
                    user_birthdate, user_sexe, user_country, user_city, user_address, user_house_number, user_zipcode) VALUES \
                    ('${req.body.username}', '${hash}', '${req.body.firstname}', '${req.body.lastname}', '${req.body.email}', \
                    '${req.body.birthdate}', '${req.body.sexe}', '${req.body.country}', '${req.body.city}', '${req.body.address}', \
                    ${req.body.house_number}, ${req.body.zipcode});`;
                
                try {
                    database.query(sqlQuery, (err, result) => {
                        if (err) { res.sendStatus(400); }
                        else {
                            const username = req.body.username;
                            const accessToken = authorization.generateAccessToken(username);
                            const refreshToken = authorization.generateRefreshToken(username);
                    
                            const sqlQuery = `INSERT INTO tokens (token) VALUES ('${refreshToken}');`;
                    
                            database.query(sqlQuery, (err, result) => { if (err) res.sendStatus(520); });
                    
                            if(!result) { res.json(result); }
                            else {
                                res.json({
                                    accessToken: accessToken,
                                    refreshToken: refreshToken
                                });
                            }
                        }
                    }); 
                } catch (err) {
                    res.sendStatus(520);
                }
            });
        });
    }
};

const logUserOut = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `DELETE FROM tokens WHERE token = '${req.body.refreshToken}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(520); }
            else { res.json({ "msg": "logged out" }); }
        });
    }
};

const amendPwd = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        crypto.randomBytes(32, function(err, salt) {
            if (err) throw err;
            argon2i.hash(req.body.password, salt).then(hash => {
                const sqlQuery = `UPDATE users SET user_password = '${hash}' where user_username = '${req.body.username}';`;

                database.query(sqlQuery, (err, result) => {
                    if (err) { res.sendStatus(400); }
                    else { res.json(result); }
                });
            });
        });
    }
};

const amendName = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_firstname = '${req.body.firstname}', user_lastname = '${req.body.lastname}' where \
            user_username = '${req.body.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(400); }
            else { res.json(result); }
        });
    }
};

const amendEmail = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_email = '${req.body.email}' where user_username = '${req.body.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(400); }
            else { res.json(result); }
        });
    } 
};

const amendAddress = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_country = '${req.body.country}', user_city = '${req.body.city}', \
            user_address = '${req.body.address}', user_house_number = ${req.body.house_number}, user_zipcode = ${req.body.zipcode} \
            where user_username = '${req.body.username}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(400); }
            else { res.json(result); }
        });
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
    logUserOut
}
