INSERT INTO public.role (id, name, created_at, updated_at, version)
VALUES ('b91d7195-69a1-4073-9c95-52ceefd93cb7', 'USER', now(), now(), 1);
INSERT INTO public.role (id, name, created_at, updated_at, version)
VALUES ('b7a8dedb-bccc-4461-9b28-b60b5bf83e7f', 'ADMINISTRATOR', now(), now(), 1);
INSERT INTO public.role (id, name, created_at, updated_at, version)
VALUES ('4d877c20-ebe9-4a7b-92e5-785873f277fe', 'MODERATOR', now(), now(), 1);

INSERT INTO public.gender (id, name, created_at, updated_at, version)
VALUES ('cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb', 'MALE', now(), now(), 1);
INSERT INTO public.gender (id, name, created_at, updated_at, version)
VALUES ('10f41829-09d2-48d0-af87-761fe5e9e6d2', 'FEMALE', now(), now(), 1);
INSERT INTO public.gender (id, name, created_at, updated_at, version)
VALUES ('aeead59f-a7c5-4570-bbc7-bd9eae859c20', 'OTHER', now(), now(), 1);

INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'user', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en');
INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('User', 'User', '123456789',
        '2001-01-01', '5e642d0a-94d4-4a4f-8760-cd6d63cd1038','admin@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', '4d877c20-ebe9-4a7b-92e5-785873f277fe');


-- User 2
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'jdoe', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('John', 'Doe', '123456780', '2002-02-02', '5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'jdoe@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 3
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'asmith', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Anna', 'Smith', '123456781', '2003-03-03', '5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'asmith@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 4
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1041', 'rbrown', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Robert', 'Brown', '123456782', '2004-04-04', '5e642d0a-94d4-4a4f-8760-cd6d63cd1041', 'rbrown@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1041', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 5
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1042', 'lwilson', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Laura', 'Wilson', '123456783', '2005-05-05', '5e642d0a-94d4-4a4f-8760-cd6d63cd1042', 'lwilson@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1042', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 6
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1043', 'tjohnson', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Tom', 'Johnson', '123456784', '2006-06-06', '5e642d0a-94d4-4a4f-8760-cd6d63cd1043', 'tjohnson@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1043', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 7
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1044', 'mgonzalez', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Maria', 'Gonzalez', '123456785', '2007-07-07', '5e642d0a-94d4-4a4f-8760-cd6d63cd1044', 'mgonzalez@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1044', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 8
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'mlee', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Michael', 'Lee', '123456786', '2008-08-08', '5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'mlee@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 9
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1046', 'kclark', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Katherine', 'Clark', '123456787', '2009-09-09', '5e642d0a-94d4-4a4f-8760-cd6d63cd1046', 'kclark@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1046', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 10
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1047', 'mbrown', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Michael', 'Brown', '123456788', '2010-10-10', '5e642d0a-94d4-4a4f-8760-cd6d63cd1047', 'mbrown@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1047', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 11
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', 'jwhite', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jessica', 'White', '987654321', '2011-11-11', '5e642d0a-94d4-4a4f-8760-cd6d63cd1048', 'jwhite@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', 'b91d7195-69a1-4073-9c95-52ceefd93cb7'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 12
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'ajones', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Alice', 'Jones', '123456790', '2012-12-12', '5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'ajones@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 13
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'pscott', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Peter', 'Scott', '123456791', '2013-01-01', '5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'pscott@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 14
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', 'cgreen', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Charles', 'Green', '123456792', '2014-02-02', '5e642d0a-94d4-4a4f-8760-cd6d63cd1051', 'cgreen@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', '4d877c20-ebe9-4a7b-92e5-785873f277fe'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 15
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1052', 'rthompson', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Rachel', 'Thompson', '123456793', '2015-03-03', '5e642d0a-94d4-4a4f-8760-cd6d63cd1052', 'rthompson@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1052', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 16
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1053', 'bmartin', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Brian', 'Martin', '123456794', '2016-04-04', '5e642d0a-94d4-4a4f-8760-cd6d63cd1053', 'bmartin@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1053', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 17
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'ashaw', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Amanda', 'Shaw', '123456795', '2017-05-05', '5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'ashaw@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 18
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'jmorris', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jack', 'Morris', '123456796', '2018-06-06', '5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'jmorris@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 19
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'jkim', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jennifer', 'Kim', '123456797', '2019-07-07', '5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'jkim@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'b91d7195-69a1-4073-9c95-52ceefd93cb7'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 20
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts, version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1057', 'rwalker', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Rebecca', 'Walker', '123456798', '2020-08-08', '5e642d0a-94d4-4a4f-8760-cd6d63cd1057', 'rwalker@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1057', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');


