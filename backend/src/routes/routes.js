const express = require('express');
const { body, param } = require('express-validator');
const router = new express.Router();

const users = require('../controllers/users');
const settings = require('../controllers/settings');
const help = require('../controllers/help');
const authorization = require('../controllers/authorization');

// ################################################################
// USER ROUTES
// ################################################################

// [GET] user from username
router.get('/user/:username', 
    param('username').not().isEmpty().escape(),
    users.getUser
);

// [POST] log user in
router.post('/user/login',
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    users.logUserIn
  );
  
// [POST] sign user in
router.post('/user',
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    body('firstname').not().isEmpty().escape(),
    body('lastname').not().isEmpty().escape(),
    body('email').isEmail().normalizeEmail(),
    body('birthdate').not().isEmpty().escape(),
    body('sexe').not().isEmpty().escape(),
    body('country').not().isEmpty().escape(),
    body('city').not().isEmpty().escape(),
    body('address').not().isEmpty().escape(),
    body('house_number').not().isEmpty().escape(),
    body('zipcode').not().isEmpty().escape(),
    users.signUserIn
);

// [POST] log user out
router.post('/user/logout',
    param('refreshToken').not().isEmpty().escape(),
    users.logUserOut
);

// [PUT] amend user password
router.put('/user/pwd',
    authorization.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    users.amendPwd
);

// [PUT] amend user name
router.put('/user/name',
    authorization.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('firstname').not().isEmpty().escape(),
    body('lastname').not().isEmpty().escape(),
    users.amendName
);

// [PUT] amend user email
router.put('/user/email',
    authorization.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('email').isEmail().normalizeEmail(),
    users.amendEmail
);

// [PUT] amend user address
router.put('/user/address',
    authorization.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('country').not().isEmpty().escape(),
    body('city').not().isEmpty().escape(),
    body('address').not().isEmpty().escape(),
    body('house_number').not().isEmpty().escape(),
    body('zipcode').not().isEmpty().escape(),
    users.amendAddress
);

// ################################################################
// SETTINGS ROUTES
// ################################################################
  
// [GET] settings from username
router.get('/settings/:username',
    authorization.authenticateToken,
    param('username').not().isEmpty().escape(),
    settings.getUserSettings
);

// [POST] amend settings from username
router.post('/settings/:username',
    authorization.authenticateToken,
    param('username').not().isEmpty().escape(),
    body('automatic_sprinkling').not().isEmpty().escape(),
    body('automatic_sprinkling_frequency').not().isEmpty().escape(),
    settings.postUserSettings
);

// [POST] amend settings from arduino
router.post('/settings/sonde/:sonde_id',
    param('sonde_id').not().isEmpty().escape(),
    body('settings_temperature_outside').not().isEmpty().escape(),
    body('settings_temperature_ground').not().isEmpty().escape(),
    body('settings_humidity').not().isEmpty().escape(),
    body('settings_last_sprinkling').not().isEmpty().escape(),
    body('settings_last_sprinkling_quantity').not().isEmpty().escape(),
    settings.postSondeSettings
);

// ################################################################
// HELP ROUTES
// ################################################################

// [GET] emails for support
router.get('/help/emails',
    authorization.authenticateToken,
    help.getEmailSupport
);

// ################################################################
// AUTHORIZATION ROUTES
// ################################################################

// [GET] authenticated
router.get('/authorization/authenticated',
    authorization.authenticateToken,
    authorization.authenticated
);

// [POST] refresh access token
router.post('/authorization/token',
    body('token').not().isEmpty().escape(),
    authorization.refreshAccessToken
);

module.exports = router;
