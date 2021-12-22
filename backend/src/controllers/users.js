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
            user_address, user_house_number, user_zipcode FROM users WHERE user_username = ?;`;

        database.query(sqlQuery,[req.params.username], (err, result) => {
            if (err) { res.sendStatus(520); }
            else { res.json(result); }
        });
    }
};

const logUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT user_username, user_password FROM users WHERE user_username = ?;`;

        if(req.body.username.match(/^[0-9a-zA-Z]+$/)){
            database.query(sqlQuery,[req.body.username], (err, result) => {
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
    
                                crypto.randomBytes(32, function(err, salt) {
                                    if (err) throw err;
                                    argon2i.hash(refreshToken, salt).then(hash => {
                                        const sqlQuery = `INSERT INTO tokens (token, user_username) VALUES (?, ?);`;
                                
                                        try {
                                            database.query(sqlQuery,[hash, username], (err, result) => {
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
                                    });
                                });
                            } else {
                                res.sendStatus(400);
                            }
                        })
                    } catch (error) {
                        res.sendStatus(520);
                    }
                }
            });

        }else{
            res.sendStatus(400);

        }


    }
};

const signUserIn = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        crypto.randomBytes(32, function(err, salt) {
            if (err) throw err;
            argon2i.hash(req.body.password, salt).then(hash => {

                if(req.body.username.match(/^[0-9a-zA-Z]+$/) &&
                    hash,req.body.firstname.match(/^[0-9a-zA-Z\- ]+$/) && 
                    req.body.lastname.match(/^[0-9a-zA-Z\- ]+$/) &&
                    req.body.email.match(/^[a-zA-Z0-9._-]+@[a-z]+\.+[a-z]+$/) &&
                    req.body.birthdate.match(/^[0-9a-zA-Z\-]+$/) &&
                    req.body.sexe.match(/^[0-9a-zA-Z]+$/) &&
                    req.body.country.match(/^[0-9a-zA-Z]+$/) &&
                    req.body.city.match(/^[0-9a-zA-Z\- ]+$/) &&
                    req.body.address.match(/^[0-9a-zA-Z\- ]+$/) &&
                    req.body.house_number.match(/^[0-9a-zA-Z]+$/) &&
                    req.body.zipcode.match(/^[0-9a-zA-Z]+$/)){

                    const sqlQuery = `INSERT INTO users (user_username, user_password, user_firstname, user_lastname, user_email, \
                        user_birthdate, user_sexe, user_country, user_city, user_address, user_house_number, user_zipcode) VALUES \
                        (?, ?, ?, ?, ?, \
                        ?, ?, ?, ?, ?, \
                        ?, ?);`;
                    
                    try {
                        database.query(sqlQuery,[req.body.username,
                                                 hash,
                                                 req.body.firstname, 
                                                 req.body.lastname,
                                                 req.body.email,
                                                 req.body.birthdate,
                                                 req.body.sexe,
                                                 req.body.country,
                                                 req.body.city,
                                                 req.body.address,
                                                 req.body.house_number,
                                                 req.body.zipcode
                                                 ], (err, result) => {
                            if (err) { res.sendStatus(400); }
                            else {
                                const username = req.body.username;
                                const accessToken = authorization.generateAccessToken(username);
                                const refreshToken = authorization.generateRefreshToken(username);
    
                                crypto.randomBytes(32, function(err, salt) {
                                    if (err) throw err;
                                    argon2i.hash(refreshToken, salt).then(hash => {
                                        const sqlQuery = `INSERT INTO tokens (token, user_username) VALUES (?, ?);`;
                                
                                        try {
                                            database.query(sqlQuery,[hash, username], (err, result) => {
                                                if (err) { res.sendStatus(520); }
                                                else if(!result) { res.json(result); }
                                                else {
                                                    res.json({
                                                        accessToken: accessToken,
                                                        refreshToken: refreshToken
                                                    });
                                                }
                                            });
                                        } catch (err) {
                                            res.sendStatus(520);
                                        }
                                    });
                                });
                            }
                        }); 
                    } catch (err) {
                        res.sendStatus(520);
                    }

                }else{
                    res.sendStatus(400);
                }

            });
        });
    }
};

const logUserOut = (req, res) => {
    const sqlQuery = `SELECT token FROM tokens WHERE user_username = ?;`;

    if(req.user.name.match(/^[0-9a-zA-Z]+$/)){
        database.query(sqlQuery,[req.user.name], (err, result) => {
            if (err) { res.sendStatus(520); }
            else if (result.length == 0) { res.json(result); }
            else {
                const tokenHash = result[0].token;
                try {
                    argon2i.verify(tokenHash, req.body.refreshToken).then(correct => {
                        if (correct) {
                            const sqlQuery = `DELETE FROM tokens WHERE token = ?;`;
    
                            database.query(sqlQuery,[tokenHash], (err, result) => {
                                if (err) { res.sendStatus(520); }
                                else { res.json({ "msg": "logged out" }); }
                            });
                        } else {
                            res.sendStatus(400);
                        }
                    })
                } catch (error) {
                    res.sendStatus(520);
                }
            }
        });
    } else{
        res.sendStatus(400);
    }

};

const amendPwd = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        crypto.randomBytes(32, function(err, salt) {
            if (err) throw err;
            argon2i.hash(req.body.password, salt).then(hash => {
                const sqlQuery = `UPDATE users SET user_password = ? where user_username = ?;`;

                if(req.body.username.match(/^[0-9a-zA-Z]+$/)){
                    database.query(sqlQuery,[hash,req.body.username ], (err, result) => {
                        if (err) { res.sendStatus(400); }
                        else { res.json(result); }
                    });
                }else{

                    res.sendStatus(400);

                }


            });
        });
    } 
};

const amendName = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_firstname = ?, user_lastname = ? where \
            user_username = ?;`;
        if(req.body.firstname.match(/^[0-9a-zA-Z\- ]+$/) && req.body.lastname.match(/^[0-9a-zA-Z\- ]+$/) && req.body.username.match(/^[0-9a-zA-Z]+$/)){
            database.query(sqlQuery,[req.body.firstname,req.body.lastname,req.body.username ], (err, result) => {
                if (err) { res.sendStatus(400); }
                else { res.json(result); }
            });
        }
        else{
            res.sendStatus(400);
        }


    }
};

const amendEmail = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_email = ? where user_username = ?;`;

        if(req.body.email.match(/^[a-zA-Z0-9._-]+@[a-z]+\.+[a-z]+$/) && req.body.username.match(/^[0-9a-zA-Z]+$/)){
            database.query(sqlQuery,[req.body.email,req.body.username ], (err, result) => {
                if (err) { res.sendStatus(400); }
                else { res.json(result); }
            });
        }else{
            res.sendStatus(400);
        }


    } 
};

const amendAddress = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `UPDATE users SET user_country = ?, user_city = ?, \
            user_address = ?, user_house_number = ?, user_zipcode = ? \
            where user_username = ?;`;

        if(req.body.country.match(/^[0-9a-zA-Z]+$/) &&
            req.body.city.match(/^[0-9a-zA-Z\- ]+$/) &&
            req.body.address.match(/^[0-9a-zA-Z\- ]+$/) &&
            req.body.house_number.match(/^[0-9a-zA-Z]+$/) &&
            req.body.zipcode.match(/^[0-9a-zA-Z]+$/) &&
            req.body.username.match(/^[0-9a-zA-Z]+$/)){
            database.query(sqlQuery,[req.body.country,req.body.city,req.body.address, req.body.house_number, req.body.zipcode,req.body.username], (err, result) => {
                if (err) { res.sendStatus(400); }
                else { res.json(result); }
            });
        }else{
            res.sendStatus(400);
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
    logUserOut
}
