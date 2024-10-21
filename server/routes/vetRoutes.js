const express = require('express');
const router = express.Router();
const vetController = require('../controllers/vetController');

router.get('/', vetController.getAllVets);
router.get('/filter/:filter/:key', vetController.getByFilter);
router.post('/add', vetController.addVet);
router.get('/info/:id', vetController.getInfo);
router.get('/filter/id/:column&:parentid', vetController.getById);
router.get('/infoedit/:id', vetController.getEditInfo);
router.put('/update/:updateId', vetController.updateVet);
router.get('/available/get/:id', vetController.isAvailable);
router.put('/available/put/:updateId', vetController.updateAvailable);
router.get('/filter/appointment/:petId&:date', vetController.getVetsAppointment);
router.get('/filter/available', vetController.getAvailable);
router.get('/report/:filter/:key', vetController.getReport);
router.get('/report/:filter', vetController.getReport);

module.exports = router;