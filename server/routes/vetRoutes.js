const express = require('express');
const router = express.Router();
const vetController = require('../controllers/vetController');

router.get('/', vetController.getAllVets);
router.get('/filter/name/:filter', vetController.getByName);
router.get('/filter/phone/:filter', vetController.getByPhone);
router.get('/filter/specie/:filter', vetController.getBySpecie);

module.exports = router;