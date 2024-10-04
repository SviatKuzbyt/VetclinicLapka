const express = require('express');
const router = express.Router();
const breedController = require('../controllers/breedController');

router.get('/', breedController.getAllBreeds);

module.exports = router;