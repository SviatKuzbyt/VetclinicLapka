const express = require('express');
const router = express.Router();
const medController = require('../controllers/medController');

router.get('/', medController.getAllMedCards);
router.get('/filter/pet/:filter', medController.getByPet);
router.get('/filter/vet/:filter', medController.getByVet);
router.get('/filter/diagnosis/:filter', medController.getByDiagnosis);
router.get('/filter/owner/:filter', medController.getByOwner);
router.get('/filter/date/:filter', medController.getByDate);
router.get('/info/:id', medController.getInfo);
router.get('/filter/id/:column&:parentid', medController.getById);
router.get('/infocreate/:id', medController.getInfoCreate);
router.post('/add', medController.addMedCard)
router.post('/addreturn', medController.addMedCardReturn);

module.exports = router;