const express = require('express');
const router = express.Router();
const appointmentController = require('../controllers/appointmentController');

router.get('/', appointmentController.getAllAppointments);
router.get('/filter/pet/:filter', appointmentController.getByPet);
router.get('/filter/vet/:filter', appointmentController.getByVet);
router.get('/filter/complaint/:filter', appointmentController.getByComplaint);
router.get('/filter/owner/:filter', appointmentController.getByOwner);
router.get('/filter/vettoday/:filter', appointmentController.getByVetToday);
router.get('/filter/date/:filter', appointmentController.getByDate);

module.exports = router;