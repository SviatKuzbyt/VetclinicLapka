const express = require('express');
const router = express.Router();
const petController = require('../controllers/petController');

router.get('/', petController.getAllPets);
router.get('/filter/name/:filter', petController.getByName);
router.get('/filter/owner/:filter', petController.getByOwner);
router.get('/filter/breed/:filter', petController.getByBreed);
router.post('/add', petController.addPet);
router.get('/info/:id', petController.getInfo);
router.get('/filter/id/:column&:parentid', petController.getById);
router.get('/infoedit/:id', petController.getEditInfo);
router.put('/update/:updateId', petController.updatePet);
router.get('/report/:filter/:key', petController.getReport);
router.get('/report/:filter', petController.getReport);

module.exports = router;