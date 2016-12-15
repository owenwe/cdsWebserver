INSERT INTO users (username,password,enabled,name)
VALUES ('admin','$2a$10$Xdy5DAnmIa2pqAbi9rxyxOB/EqfymmVAsMZzXSb.mJ5BsCmAJLblm', true,'System Admin');

INSERT INTO users (username,password,enabled,name)
VALUES ('jwhetstone','$2a$10$Xdy5DAnmIa2pqAbi9rxyxOB/EqfymmVAsMZzXSb.mJ5BsCmAJLblm', true,'James Whetstone');

INSERT INTO users (username,password,enabled,name)
VALUES ('sallen','$2a$10$Xdy5DAnmIa2pqAbi9rxyxOB/EqfymmVAsMZzXSb.mJ5BsCmAJLblm', true,'Sam Allen');

INSERT INTO users (username,password,enabled,name)
VALUES ('prabhu','$2a$10$Xdy5DAnmIa2pqAbi9rxyxOB/EqfymmVAsMZzXSb.mJ5BsCmAJLblm', true,'Prabhu Rajendran');


INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('COUNSELOR');

INSERT INTO users_roles (users_id, roles_id) VALUES (1,1);
INSERT INTO users_roles (users_id, roles_id) VALUES (1,2);
INSERT INTO users_roles (users_id, roles_id) VALUES (2,1);
INSERT INTO users_roles (users_id, roles_id) VALUES (2,2);
INSERT INTO users_roles (users_id, roles_id) VALUES (3,1);
INSERT INTO users_roles (users_id, roles_id) VALUES (3,2);
INSERT INTO users_roles (users_id, roles_id) VALUES (4,1);
INSERT INTO users_roles (users_id, roles_id) VALUES (4,2);