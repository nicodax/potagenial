const express = require('express');
const controller = require('../controllers/controllers');
const { body, param } = require('express-validator');
const router = new express.Router();

// [GET] user from username
router.get('/user/:username', 
    param('username').not().isEmpty().escape(),
    controller.getUser
);

// [POST] log user in
router.post('/user/login',
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    controller.logUserIn
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
    controller.signUserIn
);

// [PUT] amend user password
router.put('/user/pwd',
    controller.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    controller.amendPwd
);

// [PUT] amend user name
router.put('/user/name',
    controller.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('firstname').not().isEmpty().escape(),
    body('lastname').not().isEmpty().escape(),
    controller.amendName
);

// [PUT] amend user email
router.put('/user/email',
    controller.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('email').isEmail().normalizeEmail(),
    controller.amendEmail
);

// [PUT] amend user address
router.put('/user/address',
    controller.authenticateToken,
    body('username').not().isEmpty().escape(),
    body('country').not().isEmpty().escape(),
    body('city').not().isEmpty().escape(),
    body('address').not().isEmpty().escape(),
    body('house_number').not().isEmpty().escape(),
    body('zipcode').not().isEmpty().escape(),
    controller.amendAddress
);
  
// [GET] settings from username
router.get('/user/settings/:username',
    controller.authenticateToken,
    param('username').not().isEmpty().escape(),
    controller.getUserSettings
);

// [POST] amend settings from username
router.post('/user/settings/:username',
    controller.authenticateToken,
    param('username').not().isEmpty().escape(),
    body('automatic_sprinkling').not().isEmpty().escape(),
    body('automatic_sprinkling_frequency').not().isEmpty().escape(),
    controller.postUserSettings
);

// [POST] amend settings from arduino
router.post('/sonde/settings/:sonde_id',
    param('sonde_id').not().isEmpty().escape(),
    body('settings_temperature_outside').not().isEmpty().escape(),
    body('settings_temperature_ground').not().isEmpty().escape(),
    body('settings_humidity').not().isEmpty().escape(),
    body('settings_last_sprinkling').not().isEmpty().escape(),
    body('settings_last_sprinkling_quantity').not().isEmpty().escape(),
    controller.postSondeSettings
);

// [GET] emails for support
router.get('/emails',
    controller.authenticateToken,
    controller.getEmailSupport
);

// [GET] authenticated
router.get('/authenticated',
    controller.authenticateToken,
    controller.authenticated
);

module.exports = router;
