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

