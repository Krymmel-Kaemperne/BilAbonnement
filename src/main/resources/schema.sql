CREATE DATABASE IF NOT EXISTS bilabonnement;

USE bilabonnement;

create table brand (
brand_id INT AUTO_INCREMENT PRIMARY KEY,
brand_name VARCHAR(100) UNIQUE NOT NULL
);

create table model (
model_id INT AUTO_INCREMENT PRIMARY KEY,
model_name VARCHAR(100) NOT NULL,
brand_id INT NOT NULL,
FOREIGN KEY(brand_id) REFERENCES brand(brand_id)
);

create table carstatus (
car_status_id INT AUTO_INCREMENT PRIMARY KEY,
status_name VARCHAR(100) UNIQUE NOT NULL
);

create table fueltype (
fuel_type_id INT AUTO_INCREMENT PRIMARY KEY,
fuel_type_name VARCHAR(100) UNIQUE NOT NULL
);

create table transmissiontype (
transmission_type_id INT AUTO_INCREMENT PRIMARY KEY,
transmission_type_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE zipcode (
zipcode_id INT AUTO_INCREMENT PRIMARY KEY,
zip_code VARCHAR(10) UNIQUE NOT NULL,
city_name VARCHAR(50) NOT NULL
);

CREATE TABLE location (
location_id INT AUTO_INCREMENT PRIMARY KEY,
zipcode_id INT NOT NULL,
address VARCHAR(100) NOT NULL,
phone VARCHAR(20),
email VARCHAR(100),
opening_hours VARCHAR(100),
FOREIGN KEY(zipcode_id) REFERENCES zipcode(zipcode_id)
);

create table car (
car_id INT AUTO_INCREMENT PRIMARY KEY,
registration_number VARCHAR(20) UNIQUE NOT NULL,
chassis_number VARCHAR(17) UNIQUE NOT NULL,
steel_price DECIMAL(10,2) NOT NULL,
color VARCHAR(50) NOT NULL,
co2_emission  DECIMAL NOT NULL,
vehicle_number VARCHAR(50) UNIQUE NOT NULL,
model_id INT NOT NULL,
car_status_id INT NOT NULL,
fuel_type_id INT NOT NULL,
transmission_type_id INT NOT NULL,
current_odometer INT NOT NULL,
irk_code VARCHAR(50) NOT NULL,
FOREIGN KEY(model_id) REFERENCES model(model_id),
FOREIGN KEY(car_status_id) REFERENCES carstatus(car_status_id),
FOREIGN KEY(fuel_type_id) REFERENCES fueltype(fuel_type_id),
FOREIGN KEY(transmission_type_id) REFERENCES transmissiontype(transmission_type_id)
);

CREATE TABLE customer (
customer_id INT AUTO_INCREMENT PRIMARY KEY,
fname VARCHAR(50) NOT NULL,
lname VARCHAR(50) NOT NULL,
email VARCHAR(50) UNIQUE NOT NULL,
phone VARCHAR(20) NOT NULL,
address VARCHAR(100) NOT NULL,
zipcode_id int NOT NULL,
customer_type ENUM('PRIVATE', 'BUSINESS') NOT NULL,
FOREIGN KEY(zipcode_id) REFERENCES zipcode(zipcode_id)
);


CREATE TABLE business_customer (
business_customer_id INT AUTO_INCREMENT PRIMARY KEY,
customer_id INT NOT NULL,
cvr_number VARCHAR(8) UNIQUE NOT NULL,
company_name VARCHAR(100) NOT NULL,
FOREIGN KEY(customer_id) REFERENCES customer(customer_id)
);


CREATE TABLE private_customer (
private_customer_id INT AUTO_INCREMENT PRIMARY KEY,
customer_id INT NOT NULL,
cpr_number VARCHAR(10) UNIQUE NOT NULL,
FOREIGN KEY(customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE rental_agreement (
rental_agreement_id INT AUTO_INCREMENT PRIMARY KEY,
car_id INT NOT NULL,
customer_id INT NOT NULL,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
monthly_price DECIMAL(10,2) NOT NULL,
kilometers_included INT NOT NULL,
start_odometer INT NOT NULL,
end_odometer INT,
pickup_location_id INT NOT NULL,
return_location_id INT,
leasing_code VARCHAR(50) NOT NULL,
FOREIGN KEY(car_id) REFERENCES car(car_id),
FOREIGN KEY(customer_id) REFERENCES customer(customer_id),
FOREIGN KEY(pickup_location_id) REFERENCES location(location_id),
FOREIGN KEY(return_location_id) REFERENCES location(location_id)
);

CREATE TABLE condition_report (
condition_report_id INT AUTO_INCREMENT PRIMARY KEY,
rental_agreement_id INT NOT NULL,
condition_notes VARCHAR(2000),
report_date DATE NOT NULL,
total_price DECIMAL(10,2) DEFAULT 0.00,
FOREIGN KEY (rental_agreement_id) REFERENCES rental_agreement(rental_agreement_id)
);

CREATE TABLE damage (
damage_id int AUTO_INCREMENT PRIMARY KEY,
condition_report_id INT NOT NULL,
damage_description VARCHAR(250) NOT NULL,
damage_price DECIMAL(10,2) NOT NULL,
FOREIGN KEY (condition_report_id) REFERENCES condition_report(condition_report_id)
); 