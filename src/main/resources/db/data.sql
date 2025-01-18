INSERT INTO public.parliamentary_clubs (id, created_at, version, updated_at, email, fax, members_count, name, phone,
                                        short_name, term)
VALUES ('00000000-0000-0000-0000-000000000000', now(), 1, now(), 'notdefined@notdefined.com', null, 0, 'Not defined',
        null, 'ND', 'term0');

INSERT INTO public.roles (id, name, created_at, updated_at, version)
VALUES ('b91d7195-69a1-4073-9c95-52ceefd93cb7', 'USER', now(), now(), 1);
INSERT INTO public.roles (id, name, created_at, updated_at, version)
VALUES ('b7a8dedb-bccc-4461-9b28-b60b5bf83e7f', 'ADMINISTRATOR', now(), now(), 1);
INSERT INTO public.roles (id, name, created_at, updated_at, version)
VALUES ('4d877c20-ebe9-4a7b-92e5-785873f277fe', 'MODERATOR', now(), now(), 1);
INSERT INTO public.roles (id, name, created_at, updated_at, version)
VALUES ('c07e7fce-134c-4004-bd70-b1e3a576a23b', 'VOTER', now(), now(), 1);

INSERT INTO public.genders (id, name, created_at, updated_at, version)
VALUES ('cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb', 'MALE', now(), now(), 1);
INSERT INTO public.genders (id, name, created_at, updated_at, version)
VALUES ('10f41829-09d2-48d0-af87-761fe5e9e6d2', 'FEMALE', now(), now(), 1);
INSERT INTO public.genders (id, name, created_at, updated_at, version)
VALUES ('aeead59f-a7c5-4570-bbc7-bd9eae859c20', 'OTHER', now(), now(), 1);

INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language, totp_secret, parliamentary_club_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'user', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en', 'lQgCvY17bN2lKxjy/EGI7Xxcn0mLxdyJRG9uZ6VLcwvyFS6ZtSwSTMdg9Ma5lm24', '00000000-0000-0000-0000-000000000000');
INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('User', 'User', '+48123456789',
        '2001-01-01', '5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'admin@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', '4d877c20-ebe9-4a7b-92e5-785873f277fe');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'c07e7fce-134c-4004-bd70-b1e3a576a23b');

-- user will be deleted when cron job will be executed
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('37767457-866c-47cc-b0ea-48665ca5ecd2', 'deleted',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now() - interval '10 day',
        now() - interval '10 day', false, false, 0, 1, 'en');
INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('deleted', 'user', '+48123456799', '2002-02-02', '37767457-866c-47cc-b0ea-48665ca5ecd2',
        'deleteduser@localhost.com', 'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('37767457-866c-47cc-b0ea-48665ca5ecd2', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 2
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'jdoe', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('John', 'Doe', '+48123456780', '2002-02-02', '5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'jdoe@localhost.com',
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1039', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 3
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'asmith',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Anna', 'Smith', '+48123456781', '2003-03-03', '5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'asmith@localhost.com',
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1040', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 4
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1041', 'rbrown',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Robert', 'Brown', '+48123456782', '2004-04-04', '5e642d0a-94d4-4a4f-8760-cd6d63cd1041', 'rbrown@localhost.com',
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1041', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 5
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1042', 'lwilson',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Laura', 'Wilson', '+48123456783', '2005-05-05', '5e642d0a-94d4-4a4f-8760-cd6d63cd1042',
        'lwilson@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1042', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 6
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language, authorisation_totp_secret)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1043', 'tjohnson',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en',
        'kBC0ff7aSM3XyYWjGpKD5K2tjj87NRHBo7ZiGvAqK0byFS6ZtSwSTMdg9Ma5lm24');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Tom', 'Johnson', '+48123456784', '2006-06-06', '5e642d0a-94d4-4a4f-8760-cd6d63cd1043',
        'tjohnson@localhost.com', 'aeead59f-a7c5-4570-bbc7-bd9eae859c20');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1043', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 7
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1044', 'mgonzalez',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Maria', 'Gonzalez', '+48123456785', '2007-07-07', '5e642d0a-94d4-4a4f-8760-cd6d63cd1044',
        'mgonzalez@localhost.com', 'aeead59f-a7c5-4570-bbc7-bd9eae859c20');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1044', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 8
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'mlee', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Michael', 'Lee', '+48123456786', '2008-08-08', '5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'mlee@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1045', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 9
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1046', 'kclark',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Katherine', 'Clark', '+48123456787', '2009-09-09', '5e642d0a-94d4-4a4f-8760-cd6d63cd1046',
        'kclark@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1046', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 10
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1047', 'mbrown',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Michael', 'Brown', '+48123456788', '2010-10-10', '5e642d0a-94d4-4a4f-8760-cd6d63cd1047',
        'mbrown@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1047', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 11
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', 'jwhite',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jessica', 'White', '+48987654321', '2011-11-11', '5e642d0a-94d4-4a4f-8760-cd6d63cd1048',
        'jwhite@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', 'b91d7195-69a1-4073-9c95-52ceefd93cb7'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1048', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 12
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'ajones',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Alice', 'Jones', '+48123456790', '2012-12-12', '5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'ajones@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1049', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 13
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'pscott',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Peter', 'Scott', '+48123456791', '2013-01-01', '5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'pscott@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1050', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 14
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', 'cgreen',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Charles', 'Green', '+48123456792', '2014-02-02', '5e642d0a-94d4-4a4f-8760-cd6d63cd1051',
        'cgreen@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', '4d877c20-ebe9-4a7b-92e5-785873f277fe'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1051', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 15
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1052', 'rthompson',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Rachel', 'Thompson', '+48123456793', '2015-03-03', '5e642d0a-94d4-4a4f-8760-cd6d63cd1052',
        'rthompson@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1052', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 16
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1053', 'bmartin',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Brian', 'Martin', '+48123456794', '2016-04-04', '5e642d0a-94d4-4a4f-8760-cd6d63cd1053',
        'bmartin@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1053', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 17
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'ashaw', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Amanda', 'Shaw', '+48123456795', '2017-05-05', '5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'ashaw@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1054', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');

-- User 18
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'jmorris',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jack', 'Morris', '+48123456796', '2018-06-06', '5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'jmorris@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1055', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- User 19
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'jkim', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Jennifer', 'Kim', '+48123456797', '2019-07-07', '5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'jkim@localhost.com',
        '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', 'b91d7195-69a1-4073-9c95-52ceefd93cb7'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1056', '4d877c20-ebe9-4a7b-92e5-785873f277fe');

-- User 20
INSERT INTO public.users (id, username, password, created_at, updated_at, verified, blocked, failed_login_attempts,
                          version, language)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1057', 'rwalker',
        '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.', now(), now(), true, false, 0, 1, 'en');

INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id, email, gender_id)
VALUES ('Rebecca', 'Walker', '+48123456798', '2020-08-08', '5e642d0a-94d4-4a4f-8760-cd6d63cd1057',
        'rwalker@localhost.com', '10f41829-09d2-48d0-af87-761fe5e9e6d2');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1057', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');

-- Surveys

INSERT INTO public.surveys (id, title, description, created_at, updated_at, version, end_date, survey_kind)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1058', 'Survey 1', 'Survey 1 description', now() - interval '3 day',
        now() - interval '3 day', 1, now() + interval '7 day', 'OTHER'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1059', 'Praca inżynierska',
        'Czy uważasz że ta praca inżynierska jest bardzo dobra?', now(), now(), 1, now() + interval '7 day', 'OTHER'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1060', 'Survey 3', 'Survey 3 description', now() - interval '1 day',
        now() - interval '1 day', 1, now() + interval '5 day', 'OTHER'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1061', 'Survey 4', 'Survey 4 description', now() - interval '1 day',
        now() - interval '1 day', 1, now() + interval '13 day', 'PARLIAMENTARY_CLUB'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1062', 'Survey 5', 'Survey 5 description', now() - interval '5 day',
        now() - interval '5 day', 1, now() + interval '3 day', 'PARLIAMENTARY_CLUB'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1063', 'Survey 6', 'Survey 6 description', now() - interval '8 day',
        now() - interval '8 day', 1, now() - interval '1 day', 'OTHER');

-- Survey votes

INSERT INTO public.user_vote_survey (id, survey_id, user_id, created_at, updated_at, version, vote_type, age, gender_id, parliamentary_club_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1065', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1039', now(), now(), 1, 'other_survey', 17,
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1066', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1040', now(), now(), 1, 'other_survey', 18,
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1067', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1041', now(), now(), 1, 'other_survey', 34,
        'cc8e8196-1bd8-4be6-a8b4-a1dc1973a7eb', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1068', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1042', now(), now(), 1, 'other_survey', 60,
        '10f41829-09d2-48d0-af87-761fe5e9e6d2', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1069', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1043', now(), now(), 1, 'other_survey', 45,
        'aeead59f-a7c5-4570-bbc7-bd9eae859c20', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1070', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1044', now(), now(), 1, 'other_survey', 25,
        'aeead59f-a7c5-4570-bbc7-bd9eae859c20', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1071', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1045', now(), now(), 1, 'other_survey', 30,
        '10f41829-09d2-48d0-af87-761fe5e9e6d2', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1072', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1046', now(), now(), 1, 'other_survey', 38,
        '10f41829-09d2-48d0-af87-761fe5e9e6d2', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1073', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1047', now(), now(), 1, 'other_survey', 50,
        '10f41829-09d2-48d0-af87-761fe5e9e6d2', '00000000-0000-0000-0000-000000000000'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1074', '5e642d0a-94d4-4a4f-8760-cd6d63cd1059',
        '5e642d0a-94d4-4a4f-8760-cd6d63cd1048', now(), now(), 1, 'other_survey', 27,
        '10f41829-09d2-48d0-af87-761fe5e9e6d2', '00000000-0000-0000-0000-000000000000');

INSERT INTO public.user_vote_other_survey (id, result)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1065', 'DEFINITELY_YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1066', 'DEFINITELY_YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1067', 'DEFINITELY_YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1068', 'DEFINITELY_YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1069', 'DEFINITELY_YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1070', 'YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1071', 'YES'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1072', 'I_DONT_KNOW'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1073', 'NO'),
       ('5e642d0a-94d4-4a4f-8760-cd6d63cd1074', 'DEFINITELY_NO');


INSERT INTO public.voter_role_request (id, user_id, request_date, state, created_at, updated_at, version)
VALUES ('b2d44bb0-1665-437d-98ec-ee8061aab53b', '5e642d0a-94d4-4a4f-8760-cd6d63cd1039', now() - interval '1 day',
        'PENDING', now() - interval '1 day', now() - interval '1 day', 1);

UPDATE votings
SET end_date = now() + interval '2 day'
WHERE id IN (SELECT id
             FROM votings
             WHERE sitting_day = 3
             ORDER BY id
             LIMIT 4);