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
    body('username').not().isEmpty().escape(),
    body('password').not().isEmpty().escape(),
    controller.amendPwd
);

// [PUT] amend user name
router.put('/user/name',
    body('username').not().isEmpty().escape(),
    body('firstname').not().isEmpty().escape(),
    body('lastname').not().isEmpty().escape(),
    controller.amendName
);

// [PUT] amend user email
router.put('/user/email',
    body('username').not().isEmpty().escape(),
    body('email').isEmail().normalizeEmail(),
    controller.amendEmail
);

// [PUT] amend user address
router.put('/user/address',
    body('username').not().isEmpty().escape(),
    body('country').not().isEmpty().escape(),
    body('city').not().isEmpty().escape(),
    body('address').not().isEmpty().escape(),
    body('house_number').not().isEmpty().escape(),
    body('zipcode').not().isEmpty().escape(),
    controller.amendAddress
);
  
// [GET] eshop
// router.get('/shop')
// en attente de l'itération suivante

module.exports = router;
