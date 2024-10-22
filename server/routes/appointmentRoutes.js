const express = require('express');
const router = express.Router();
const appointmentController = require('../controllers/appointmentController');

router.get('/', appointmentController.getAll);
router.get('/filter-search/:filter/:key', appointmentController.getByFilter);
router.get('/info/:id', appointmentController.getInfo);
router.get('/filter/id/:column&:parentid', appointmentController.getById);
router.post('/add', appointmentController.addAppointment);
router.get('/filter/vetid/:vetId', appointmentController.getByVetId);
router.post('/addreturn', appointmentController.addAppointmentReturn);
router.get('/infoedit/:id', appointmentController.getDataForEdit);
router.put('/update/:updateId', appointmentController.updateAppointment);
router.get('/report/:filter/:key', appointmentController.getReport);
router.get('/report/:filter', appointmentController.getReport);

module.exports = router;