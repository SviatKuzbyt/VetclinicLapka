const db = require('../config/db');

const Breed = {
    getItems: async (filter, key = ``) => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE b.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'specie':
                filterRow = `WHERE s.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }

        const [result] = await db.execute(
            `SELECT b.breed_id as 'id', b.name as 'label', s.name as 'subtext' 
            FROM breed b 
            INNER JOIN specie s ON b.specie_id = s.specie_id ${filterRow} ORDER BY b.name`, 
            params
        );
        
        return result;
    }
};

module.exports = Breed;