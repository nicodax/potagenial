const { validationResult } = require('express-validator');
const jwt = require('jsonwebtoken');

const database = require('../db');

const generateAccessToken = (username) => {
    const user = {
        name: username
    };

    return jwt.sign(user, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '20m'});
}

const generateRefreshToken = (username) => {
    const user = {
        name: username
    };

    return jwt.sign(user, process.env.REFRESH_TOKEN_SECRET);
}

const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
    if(token == null) { return res.sendStatus(401); }

    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
        if (err) { return res.sendStatus(403); }

        req.user = user;
        next()
    })
}

const authenticated = (req, res) => {
    res.json({"authenticated": true});
}

const refreshAccessToken = (req, res) => {
    const errors = validationResult(req);
    if (errors.array().length > 0) { res.send(errors.array()); }
    else {
        const sqlQuery = `SELECT * FROM tokens WHERE token = '${req.body.token}';`;

        database.query(sqlQuery, (err, result) => {
            if (err) { res.sendStatus(500); }
            if (!result) { res.sendStatus(403); }
        });
    
        jwt.verify(req.body.token, process.env.REFRESH_TOKEN_SECRET, (err, user) => {
            if(err) { res.sendStatus(403); }
            else {
                const accessToken = generateAccessToken({ name: user.name });
                res.json({"accessToken": accessToken});
            }
        })
    }
};


module.exports = {
    authenticateToken,
    authenticated,
    refreshAccessToken,
    generateAccessToken,
    generateRefreshToken
}
