INSERT INTO roles (id,name) VALUES ('1','ROLE_USER') on duplicate key update name ='ROLE_USER';
INSERT INTO roles (id,name) VALUES ('2','ROLE_MODERATOR') on duplicate key update name ='ROLE_MODERATOR';
INSERT INTO roles (id,name) VALUES ('3','ROLE_ADMIN') on duplicate key update name ='ROLE_ADMIN';
