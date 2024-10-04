const db = require('../config/db');

const Breed = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT b.breed_id as 'id', b.name as 'label', s.name as 'subtext' FROM breed b INNER JOIN specie s ON b.specie_id = s.specie_id ORDER BY b.name");
        return rows;
    }
};

module.exports = Breed;