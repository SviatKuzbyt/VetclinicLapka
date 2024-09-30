const express = require('express');
const router = express.Router();
const medController = require('../controllers/medController');

router.get('/', medController.getAllMedCards);

module.exports = router;