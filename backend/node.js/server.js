// express & cors import
const express = require('express');
const router = express.Router();

// express app creation
const app = express();

// variables
const port = process.env.PORT || 3000

// routes
app.use('/user', require('./server/routes/user'));

app.get('*', (req, res) => {
    res.send('this is the potagenial RESTfull API')
});

app.listen(port, function(){
    console.log(`Server running on port ${port}`);
});
