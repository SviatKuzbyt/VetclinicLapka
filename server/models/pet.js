const db = require('../config/db');

const Pet = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id ");
        return rows;
    },

    getByName: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE p.name LIKE ?", [`%${filter}%`] )
        return rows; 
    },

    getByOwner: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ?", [`%${filter}%`] )
        return rows; 
    },

    getByBreed: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE b.name LIKE ?", [`%${filter}%`] )
        return rows; 
    }
};

module.exports = Pet;