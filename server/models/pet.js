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

    // addPet: async (name, breed_id, owner_id, gender, date_of_birth, features) => {
    //     const [result] = await db.execute(
    //         'INSERT INTO pet (name, breed_id, owner_id, gender, date_of_birth, features) VALUES (?, ?, ?, ?, ?, ?)',
    //         [name, breed_id, owner_id, gender, date_of_birth, features]
    //     );

    //     const petId = result.insertId;

    //     const [subtext] = await db.execute("SELECT CONCAT(b.name, ', ', o.name) FROM pet p INNER JOIN breed b ON p.breed_id = b.breed_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE p.pet_id = ? LIMIT 1", [petId])
        
    //     return {
    //         insertId: petId,
    //         subtext: rows[0].subtext
    //     };
    // }

    addPet: async (name, breed_id, owner_id, gender, date_of_birth, features) => {
    // Insert the new pet record
    const [result] = await db.execute(
        'INSERT INTO pet (name, breed_id, owner_id, gender, date_of_birth, features) VALUES (?, ?, ?, ?, ?, ?)',
        [name, breed_id, owner_id, gender, date_of_birth, features]
    );

    // Get the pet ID from the result
    const petId = result.insertId;

    // Fetch the subtext for the newly inserted pet
    const [rows] = await db.execute(
        `SELECT CONCAT(b.name, ', ', o.name) as subtext 
         FROM pet p 
         INNER JOIN breed b ON p.breed_id = b.breed_id 
         INNER JOIN owner o ON p.owner_id = o.owner_id 
         WHERE p.pet_id = ? LIMIT 1`,
        [petId]
    );

    // Return the insertId and the subtext
    return {
        insertId: petId,
        subtext: rows[0].subtext
    };
}

};

module.exports = Pet;