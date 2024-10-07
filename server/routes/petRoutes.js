const express = require('express');
const router = express.Router();
const petController = require('../controllers/petController');

router.get('/', petController.getAllPets);
router.get('/filter/name/:filter', petController.getByName);
router.get('/filter/owner/:filter', petController.getByOwner);
router.get('/filter/breed/:filter', petController.getByBreed);
router.post('/add', petController.addPet);
router.get('/info/:id', petController.getInfo);
router.get('/filter/ownerid/:filter', petController.getByOwnerId);

module.exports = router;