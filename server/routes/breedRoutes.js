const express = require('express');
const router = express.Router();
const breedController = require('../controllers/breedController');

router.get('/', breedController.getAllBreeds);
router.get('/filter/name/:filter', breedController.getByName);
router.get('/filter/specie/:filter', breedController.getBySpecie);

module.exports = router;