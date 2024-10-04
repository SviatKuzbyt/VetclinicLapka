const db = require('../config/db');

const Pet = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id ORDER BY p.name");
        return rows;
    },

    getByName: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE p.name LIKE ? ORDER BY p.name", [`%${filter}%`] )
        return rows; 
    },

    getByOwner: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ? ORDER BY p.name", [`%${filter}%`] )
        return rows; 
    },

    getByBreed: async (filter) => {
        const [rows] = await db.execute("SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE b.name LIKE ? ORDER BY p.name", [`%${filter}%`] )
        return rows; 
    },

    addPet: async (name, breed_id, owner_id, gender, date_of_birth, features) => {
        const [result] = await db.execute(
            'INSERT INTO pet (name, breed_id, owner_id, gender, date_of_birth, features) VALUES (?, ?, ?, ?, ?, ?)',
            [name, breed_id, owner_id, gender, date_of_birth, features]
        );

        const petId = result.insertId;

        const [rows] = await db.execute(
            `SELECT CONCAT(b.name, ', ', o.name) as subtext 
             FROM pet p 
             INNER JOIN breed b ON p.breed_id = b.breed_id 
             INNER JOIN owner o ON p.owner_id = o.owner_id 
             WHERE p.pet_id = ? LIMIT 1`,
            [petId]
        );

        return {
            insertId: petId,
            subtext: rows[0].subtext
        };
    },

    getInfo: async (pet_id) => {
        const [rows] = await db.execute(
            "SELECT p.name as 'pet_name', CONCAT(b.name, ', ', s.name) as 'breed_name', CASE WHEN p.gender = 1 Then 'Самець' ELSE 'Самка' END AS 'gender', DATE_FORMAT(p.date_of_birth, '%Y.%m.%d') as 'date_of_birth' FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN specie s ON b.specie_id = s.specie_id WHERE p.pet_id = ? LIMIT 1", [pet_id]);
        return [rows[0].pet_name, rows[0].breed_name, rows[0].gender, rows[0].date_of_birth];
    }   
};

module.exports = Pet;

