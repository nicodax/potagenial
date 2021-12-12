const express = require('express');
const https = require('https');
const fs = require('fs');

const app = express();

require('dotenv').config();

const port = process.env.PORT || 3443;

const routes = require('./src/routes/routes');

app.use(express.json());
app.use(routes);

const privateKey = fs.readFileSync('/etc/letsencrypt/live/daxhelet.ovh/privkey.pem', 'utf8');
const certificate = fs.readFileSync('/etc/letsencrypt/live/daxhelet.ovh/cert.pem', 'utf8');
const ca = fs.readFileSync('/etc/letsencrypt/live/daxhelet.ovh/chain.pem', 'utf8');

const httpsServer = https.createServer({
    key: privateKey,
	cert: certificate,
	ca: ca
}, app)

httpsServer.listen(port, () => console.log(`Server running on port ${port}`));
