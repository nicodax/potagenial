// express import
const express = require('express');

// express router creation
const router = express.Router();

// middlewares
const { uniqueUsername } = require('../middlewares/user');

// ROUTES :

// [GET] user from username
router.get('/:username', function(req, res){
    // WAITING FOR MYSQL SERVER
});

// [GET] username from user id
router.get('/username/:id', function(req, res){
    User.findById(req.params.id).exec(function(err, user){
        if(err){
            res.sendStatus(404);
        }else {
            res.json(user.username);
        }
    })
});

// [POST] log user in
router.post('/login', function(req, res){
  let usr = req.body.username;
  let psw = req.body.password;
  // WAITING FOR MYSQL SERVER
});

// [POST] log user in
router.post('/', uniqueUsername, function(req, res){
    let usr = req.body.username;
    let psw = req.body.password;
    let email = req.body.email;
    // WAITING FOR MYSQL SERVER
});

module.exports = router;