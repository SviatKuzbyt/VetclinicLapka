const express = require('express');
const router = express.Router();
const ownerController = require('../controllers/ownerController');

router.get('/', ownerController.getAllOwners);
router.get('/filter/name/:filter', ownerController.getByName);
router.get('/filter/phone/:filter', ownerController.getByPhone);
router.post('/add', ownerController.addOwner);

module.exports = router;