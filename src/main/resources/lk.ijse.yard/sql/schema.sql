CREATE DATABASE IF NOT EXISTS RJYard;

USE RJYard ;

CREATE TABLE user(

                     user_id VARCHAR(14) PRIMARY KEY,
                     name VARCHAR(30) NOT NULL,
                     email VARCHAR(30),
                     password VARCHAR(25) NOT NULL

);

CREATE TABLE suppliers(
                          sup_id VARCHAR(6) PRIMARY KEY,
                          company_name VARCHAR(20) NOT NULL,
                          company_email VARCHAR(50) NOT NULL
);

CREATE TABLE material(
                         m_id VARCHAR(6) PRIMARY KEY,
                         material_name VARCHAR(20) NOT NULL,
                         type VARCHAR(30) NOT NULL ,
                         qty_available FLOAT(15) NOT NULL,
                         unit VARCHAR (6) NOT NULL
);


CREATE TABLE material_receive_details(
                                         received_id VARCHAR(10) PRIMARY KEY ,
                                         sup_id VARCHAR(6) NOT NULL ,
                                         m_id VARCHAR(6) NOT NULL ,
                                         material_name VARCHAR(20) NOT NULL,
                                         type VARCHAR(30) NOT NULL ,
                                         received_qty FLOAT(15) NOT NULL,
                                         unit VARCHAR(6) NOT NULL ,
                                         received_date DATE ,
                                         FOREIGN KEY (sup_id) REFERENCES suppliers(sup_id) ON DELETE CASCADE ON   UPDATE CASCADE ,
                                         FOREIGN KEY (m_id) REFERENCES material(m_id) ON DELETE CASCADE ON   UPDATE CASCADE

);

CREATE TABLE project(
                        p_no VARCHAR(20) PRIMARY KEY,
                        p_name VARCHAR(50) NOT NULL,
                        location VARCHAR(25) NOT NULL,
                        completion_date DATE
);


CREATE TABLE project_material_requirements(
                        p_no VARCHAR(20) NOT NULL ,
                        p_name VARCHAR(50) NOT NULL ,
                        m_id VARCHAR(6) NOT NULL,
                        issue_qty FLOAT(15) NOT NULL,
                        required_qty FLOAT(15) NOT NULL,
                        unit VARCHAR (6) NOT NULL,
                        FOREIGN KEY (p_no) REFERENCES project(p_no) ON DELETE CASCADE ON   UPDATE CASCADE,
                        FOREIGN KEY (m_id) REFERENCES material(m_id) ON DELETE CASCADE ON   UPDATE CASCADE,
                        PRIMARY KEY (p_no,m_id)
);

select * from project;
select * from project_material_requirements;
select * from material;
SELECT required_qty - issue_qty AS checkRange FROM project_material_requirements WHERE p_no = 'P003' AND m_id ='AGG001';
update project_material_requirements SET issue_qty = 0 WHERE p_no = 'P003';

CREATE TABLE machine(
                        machine_id VARCHAR(6) PRIMARY KEY,
                        machine_name VARCHAR(25) NOT NULL,
                        status VARCHAR(20) NOT NULL
);
SELECT * FROM machine WHERE status NOT IN ('remove');
SELECT * FROM machine;
update machine set status = 'ON_yard' where status = 'remove';

CREATE TABLE employe(
                        e_id VARCHAR(10) PRIMARY KEY,
                        e_name VARCHAR(25) NOT NULL ,
                        e_address VARCHAR(50) NOT NULL,
                        job_title VARCHAR(15) NOT NULL,
                        availability VARCHAR(5) Null
);

# /////////////////////////////////////////////////////////////////////////////////////////////
SELECT availability FROM employe WHERE  = 'DR003'
SELECT e_id FROM vehicle  WHERE e_id = 'DR00';
UPDATE employe set availability = 'NA' WHERE e_id = 'DR002';
DELETE  FROM employe Where e_id = 'DR001';
Select * from employe;
desc vehicle;
# /////////////////////////////////////////////////////////////////////////////////////////////

CREATE TABLE vehicle(
                        v_id VARCHAR(15) PRIMARY KEY,
                        v_name VARCHAR(20) NOT NULL,
                        e_id VARCHAR(10) ,
                        root_status VARCHAR(25) NOT NULl,
                        driver_availability VARCHAR(5),
                        remove_or_working VARCHAR(10) NOT NULL ,
                        FOREIGN KEY (e_id) REFERENCES employe(e_id) ON DELETE CASCADE ON UPDATE CASCADE
);
# /////////////////////////////////////////////////////////////////////////////////////////////
SELECT * FROM vehicle WHERE  driver_availability LIKE 'AV%';
UPDATE vehicle SET  e_id = NULL , driver_availability = NULL WHERE e_id = 'DR001';
DELETE  FROM vehicle WHERE v_id ='V001';

select * from employe;
select * from vehicle;
SELECT root_status FROM vehicle WHERE e_id = 'DR002';

UPDATE vehicle SET  e_id = 'DR002' , driver_availability = 'AV' WHERE v_id = 'V001';


SELECT * FROM employe WHERE  e_id NOT IN ('DR001') AND job_title = 'DRIVER';

SELECT e_id FROM vehicle WHERE v_id = 'V002';
# ////////////////////////////////////////////////////////////////////////////////////////


CREATE TABLE material_send_details(

                              issue_id VARCHAR(6) PRIMARY KEY ,
                              m_id VARCHAR(6) ,
                              p_no VARCHAR(20) ,
                              issue_qty FLOAT(15) NOT NULL,
                              v_id VARCHAR(15),
                              issue_date DATE,
                              FOREIGN KEY (m_id) REFERENCES material(m_id) ON DELETE CASCADE ON   UPDATE CASCADE ,
                              FOREIGN KEY (p_no) REFERENCES project(p_no) ON DELETE CASCADE ON   UPDATE CASCADE  ,
                              FOREIGN KEY (v_id) REFERENCES vehicle(v_id) ON DELETE CASCADE ON   UPDATE CASCADE

);

select  * from material_send_details;
DELETE  FROM material_send_details WHERE issue_id ='MI_RE1';
TRUNCATE material_send_details;

CREATE TABLE vehicle_repairs(

                                repair_id VARCHAR(10) PRIMARY KEY,
                                v_id VARCHAR(6),
                                status VARCHAR(50),
                                cost FLOAT (15),
                                repair_date DATE,
                                FOREIGN KEY (v_id) REFERENCES vehicle(v_id) ON DELETE CASCADE ON   UPDATE CASCADE

);
select * from  vehicle_repairs;


CREATE TABLE machine_send_details(
                                issue_id VARCHAR(6) NOT NULL,
                                machine_id VARCHAR(6) ,
                                p_no VARCHAR(20) ,
                                v_id VARCHAR(15),
                                send_date DATE ,
                                FOREIGN KEY (machine_id) REFERENCES machine(machine_id) ON DELETE CASCADE ON   UPDATE CASCADE ,
                                FOREIGN KEY (p_no) REFERENCES project(p_no) ON DELETE CASCADE ON   UPDATE CASCADE  ,
                                FOREIGN KEY (v_id) REFERENCES vehicle(v_id) ON DELETE CASCADE ON   UPDATE CASCADE  ,
                                PRIMARY KEY(issue_id , machine_id , p_no , v_id)
);
select  * from machine_send_details;



CREATE TABLE machine_repairs(

                                repair_id VARCHAR(6) PRIMARY KEY,
                                machine_id VARCHAR(6),
                                cost FLOAT (15),
                                repair_date DATE,
                                FOREIGN KEY (machine_id) REFERENCES machine(machine_id) ON DELETE CASCADE ON UPDATE CASCADE

);


INSERT INTO user VALUES ('1','Navod','tis@gmail.com','1');

ALTER TABLE material
ADD COLUMN unit VARCHAR (6) NOT NULL ;



select * from material;

desc suppliers;


DELETE FROM material WHERE m_id = 'SAN001';

SELECT COUNT(*) AS noOfRaws FROM material;

desc material;

UPDATE material SET m_id = 'SAN001' Where m_id ='SA1';

use RJYard;
desc material_receive_details;

select * from material_receive_details;
select * from material;
desc material_receive_details;




