const express = require('express');
const router = express.Router();
const medController = require('../controllers/medController');

router.get('/', medController.getAllMedCards);
router.get('/filter/pet/:filter', medController.getByPet);
router.get('/filter/vet/:filter', medController.getByVet);
router.get('/filter/diagnosis/:filter', medController.getByDiagnosis);
router.get('/filter/owner/:filter', medController.getByOwner);


module.exports = router;