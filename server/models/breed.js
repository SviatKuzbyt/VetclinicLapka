const db = require('../config/db');

const Breed = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT b.breed_id as 'id', b.name as 'label', s.name as 'subtext' FROM breed b INNER JOIN specie s ON b.specie_id = s.specie_id ORDER BY b.name");
        return rows;
    },

    getByName: async (filter) => {
        const [rows] = await db.execute("SELECT b.breed_id as 'id', b.name as 'label', s.name as 'subtext' FROM breed b INNER JOIN specie s ON b.specie_id = s.specie_id WHERE b.name LIKE ? ORDER BY b.name", [`%${filter}%`]);
        return rows;
    },

    getBySpecie: async (filter) => {
        const [rows] = await db.execute("SELECT b.breed_id as 'id', b.name as 'label', s.name as 'subtext' FROM breed b INNER JOIN specie s ON b.specie_id = s.specie_id WHERE s.name LIKE ? ORDER BY b.name", [`%${filter}%`]);
        return rows;
    }
};

module.exports = Breed;