const database = require('../db');

const getEmailSupport = (req, res) => {
    const sqlQuery = `SELECT * from emails;`;

    database.query(sqlQuery, (err, result) => {
        if (err) res.sendStatus(520);
        
        res.json(result);
    });
}


module.exports = {
    getEmailSupport
}
