# Vetclinic Lapka
A project to manage the records of a veterinary clinic in a MySQL database, namely: accounting for pets, their owners, veterinarians, appointment records, and medical history of patients. The project consists of a MySQL database, an Android client application, and a Node.js server that provides secure communication between the application and the database.

## Features

#### Mobile application
- **Home page:** contains a list of pages with records and pages for creating an appointment and a medical record
- **Records page:** contains a list of records in the selected table and buttons for creating a record
- **Record details page:** contains all the details of the open record and all related records
- **Record creation page:** a page or pull-down panel that contains text fields and other elements for easy record creation

#### Server
- **index.js** - the main file that starts the server
- **controllers** - a package that contains SQL queries to be executed
- **routes** - a package that defines the paths to server functions
- **models** - a package that contains business logic, executes and processes requests
- **config** and other files that ensure server stability

#### Database
- Stores all project data and is based on MySql
- **Contains the following tables:** specie, breed, owner, pet, vet, vet_speciality, appointment and medical_card

## Screenshots
![Home and forecast screen](https://github.com/SviatKuzbyt/VetclinicLapka/blob/master/other/readme_screenshot.png) 
