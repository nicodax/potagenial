const express = require('express');
const https = require('https');
const path = require('path');
const fs = require('fs');

const app = express();

require('dotenv').config();

const port = process.env.PORT || 3443;

const routes = require('./src/routes/routes');

app.use(express.json());
app.use(routes);

const sslServer = https.createServer({
    key: fs.readFileSync(path.join(__dirname, 'cert', 'key.pem')),
    cert: fs.readFileSync(path.join(__dirname, 'cert', 'cert.pem'))
}, app)

sslServer.listen(port, () => console.log(`Server running on port ${port}`));
