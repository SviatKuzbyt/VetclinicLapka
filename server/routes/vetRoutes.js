const express = require('express');
const router = express.Router();
const vetController = require('../controllers/vetController');

router.get('/', vetController.getAllVets);
router.get('/filter/name/:filter', vetController.getByName);
router.get('/filter/phone/:filter', vetController.getByPhone);
router.get('/filter/specie/:filter', vetController.getBySpecie);
router.post('/add', vetController.addVet);
router.get('/info/:id', vetController.getInfo);
router.get('/filter/id/:column&:parentid', vetController.getById);
router.get('/infoedit/:id', vetController.getEditInfo);
router.put('/update/:updateId', vetController.updateVet);
router.get('/available/get/:id', vetController.isAvailable);
router.put('/available/put/:updateId', vetController.updateAvailable);
router.get('/filter/appointment/:petId&:date', vetController.getVetsAppointment);

module.exports = router;