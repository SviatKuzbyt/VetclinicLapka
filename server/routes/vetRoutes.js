const express = require('express');
const router = express.Router();
const vetController = require('../controllers/vetController');

router.get('/', vetController.getAllVets);

module.exports = router;