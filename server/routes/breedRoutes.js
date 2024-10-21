const express = require('express');
const router = express.Router();
const breedController = require('../controllers/breedController');

router.get('/', breedController.getAll);
router.get('/filter/:filter/:key', breedController.getByFilter);

module.exports = router;