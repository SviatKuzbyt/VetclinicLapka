const express = require('express');
const router = express.Router();
const ownerController = require('../controllers/ownerController');

router.get('/', ownerController.getAllOwners);
router.get('/filter/:filter/:key', ownerController.getByFilter);
router.post('/add', ownerController.addOwner);
router.get('/info/:id', ownerController.getInfo);
router.get('/filter/id/:column&:parentid', ownerController.getById);
router.get('/infoedit/:id', ownerController.getEditInfo);
router.put('/update/:updateId', ownerController.updateOwner);
router.get('/report/:filter/:key', ownerController.getReport);
router.get('/report/:filter', ownerController.getReport);

module.exports = router;