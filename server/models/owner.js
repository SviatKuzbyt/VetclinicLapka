const db = require('../config/db');

const Owner = {

    getItems: async (filter, key = '') => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'phone':
                filterRow = `WHERE phone LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }

        const [result] = await db.execute(
            `SELECT owner_id as 'id', name as 'label', phone as 'subtext' 
            FROM vetclinic_lapka.owner ${filterRow} ORDER BY name`, params);

        return result;
    },

    addOwner: async (name, phone) => {
        const [result] = await db.execute(
            'INSERT INTO owner (name, phone) VALUES (?, ?)',
            [name, phone]
        );
        return result.insertId;
    },

    getInfo: async (owner_id) => {
        const [rows] = await db.execute('SELECT name, phone FROM owner WHERE owner_id = ? LIMIT 1', [owner_id]);
        return [rows[0].name, rows[0].phone];
    },

    getById: async (parentid) => {
        const [rows] = await db.execute(
            `SELECT o.owner_id as 'id', o.name as 'label', o.phone as 'subtext' 
            FROM pet p INNER JOIN owner o ON p.owner_id = o.owner_id WHERE p.pet_id = ?`, [parentid] 
        )
        return rows; 
    },

    getEditInfo: async (owner_id) => {
        const [rows] = await db.execute('SELECT name, phone FROM owner WHERE owner_id = ? LIMIT 1', [owner_id]);

        return [
            { "data": rows[0].name, "labelData": "" },
            { "data": rows[0].phone, "labelData": "" }
        ];
    },

    updateOwner: async (name, phone, updateId) => {
        await db.execute(
            'UPDATE owner SET name = ?, phone = ? WHERE owner_id = ?',
            [name, phone, updateId]
        );
    },

    getReport: async (filter, key) => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE o.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'phone':
                filterRow = `WHERE o.phone LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }
    
        const [result] = await db.execute(
            `SELECT 
                o.name AS owner_name,
                o.phone AS owner_phone,
                GROUP_CONCAT(p.name ORDER BY p.name ASC SEPARATOR ', ') AS pets
            FROM owner o
            JOIN pet p ON o.owner_id = p.owner_id
            ${filterRow}
            GROUP BY o.owner_id ORDER BY o.name`,
            params
        );

        let formateResult = []

        for(let i in result){
            formateResult.push(
                `<b>Ім'я:</b> ${result[i].owner_name}<br><b>Телефон:</b> ${result[i].owner_phone}<br><b>Домашні вихованці:</b> ${result[i].pets}`
            )
        }
    
        return formateResult;
    }
    
};

module.exports = Owner;