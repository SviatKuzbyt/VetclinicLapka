const db = require('../config/db');

const Pet = {

    getItems: async (filter, key = '') => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE p.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'owner':
                filterRow = `WHERE o.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'breed':
                filterRow = `WHERE b.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }

        const [result] = await db.execute(
            `SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' 
            FROM pet p 
            INNER JOIN breed b ON p.breed_id = b.breed_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id 
            ${filterRow} ORDER BY p.name`, params
        );

        return result;
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
            `SELECT 
                p.name as 'pet_name', 
                CONCAT(b.name, ', ', s.name) as 'breed_name', 
                CASE WHEN p.gender = 1 Then 'Самець' ELSE 'Самка' END AS 'gender', 
                DATE_FORMAT(p.date_of_birth, '%Y.%m.%d') as 'date_of_birth',
                CASE WHEN p.features is null Then 'Немає' ELSE p.features END AS 'features' 
            FROM pet p 
            INNER JOIN breed b ON p.breed_id = b.breed_id 
            INNER JOIN specie s ON b.specie_id = s.specie_id 
            WHERE p.pet_id = ? LIMIT 1`, [pet_id]
        );

        return [rows[0].pet_name, rows[0].breed_name, rows[0].gender, rows[0].date_of_birth, rows[0].features];
    },

    getById: async (column, parentid) => {

        switch (column) {
            case 'owner':
                filter = 'WHERE p.owner_id';
                break;
            case 'appointment':
                filter = 'INNER JOIN appointment a ON p.pet_id = a.pet_id WHERE a.appointment_id';
                break; 
            case 'medcard':
                filter = 'INNER JOIN appointment a ON p.pet_id = a.pet_id INNER JOIN medical_card mc ON a.appointment_id = mc.appointment_id WHERE mc.card_id';
                break; 
            default:
                filter = 'mc.card_id';
        }

        const [rows] = await db.execute(
            `SELECT p.pet_id as 'id', p.name as 'label', CONCAT(b.name, ', ', o.name) as 'subtext' 
            FROM pet p 
            INNER JOIN breed b ON p.breed_id = b.breed_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id ${filter} = ? ORDER BY p.name`, 
            [parentid] 
        )        
        return rows; 

    },

    getEditInfo: async (petid) => {
        const [rows] = await db.execute(`
            SELECT 
                p.name as 'name',
                b.name as 'bread_name',
                o.name as "owner_name",
                p.gender as 'gender',
                DATE_FORMAT(p.date_of_birth, '%Y.%m.%d') as 'date_of_birth',
                p.features as 'features',
                p.breed_id as 'bread',
                p.owner_id as 'owner'
            FROM pet p
            INNER JOIN breed b ON p.breed_id =b.breed_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id
            WHERE p.pet_id = ?`, [petid]);

        return [
            { "data": rows[0].name, "labelData": "" },
            { "data": rows[0].bread, "labelData": rows[0].bread_name },
            { "data": rows[0].owner, "labelData": rows[0].owner_name },
            { "data": rows[0].gender, "labelData": "" },
            { "data": rows[0].date_of_birth, "labelData": "" },
            { "data": rows[0].features, "labelData": "" }
        ];
    },

    updatePet: async (name, breed_id, owner_id, gender, date_of_birth, features, updateId) => {
        const [result] = await db.execute(
            'UPDATE pet SET name = ?, breed_id = ?, owner_id = ?, gender = ?, date_of_birth = ?, features = ? WHERE pet_id = ?',
            [name, breed_id, owner_id, gender, date_of_birth, features, updateId]
        );
    },

    getReport: async (filter, key) => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE p.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'owner':
                filterRow = `WHERE o.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'breed':
                filterRow = `WHERE b.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }
    
        const [result] = await db.execute(
            `select 
                p.name as 'pet_name',
                CONCAT(b.name, ', ', s.name) as 'breed_name',
                CASE WHEN p.gender = 1 Then 'Самець' ELSE 'Самка' END AS 'gender',
                DATE_FORMAT(p.date_of_birth, '%Y.%m.%d') as 'date_of_birth',
                CASE WHEN p.features is null Then 'Немає' ELSE p.features END AS 'features',
                CONCAT(o.name, ' (', o.phone, ')') as 'owner',
                COUNT(a.appointment_id) AS appointment_count
            from pet p 
            inner join breed b on p.breed_id = b.breed_id 
            inner join specie s on b.specie_id = s.specie_id 
            inner join owner o on p.owner_id = o.owner_id 
            LEFT JOIN appointment a ON p.pet_id = a.pet_id
            ${filterRow}
            GROUP BY p.pet_id, p.name ORDER BY p.name`,
            params
        );

        let formateResult = []

        for(let i in result){
            formateResult.push(
                `<b>Ім'я:</b> ${result[i].pet_name}<br><b>Порода:</b> ${result[i].breed_name}<br>
                <b>Стать:</b> ${result[i].gender}<br><b>Дата народження:</b> ${result[i].date_of_birth}<br>
                <b>Особливості здоров'я:</b> ${result[i].features}<br><b>Власник:</b> ${result[i].owner}<br>
                <b>Кількість звертань:</b> ${result[i].appointment_count}`
            )
        }
    
        return formateResult;
    }
};

module.exports = Pet;