INSERT INTO public.role (id, name, created_at, updated_at)
VALUES ('b91d7195-69a1-4073-9c95-52ceefd93cb7', 'USER', now(), now());
INSERT INTO public.role (id, name, created_at, updated_at)
VALUES ('b7a8dedb-bccc-4461-9b28-b60b5bf83e7f', 'ADMINISTRATOR', now(), now());
INSERT INTO public.role (id, name, created_at, updated_at)
VALUES ('4d877c20-ebe9-4a7b-92e5-785873f277fe', 'MODERATOR', now(), now());

INSERT INTO public.users (id, username, password, email, created_at, updated_at, verified, blocked, failed_login_attempts, version)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'user', '$2a$12$bOPVAvWOC2f9gJoF37IeE.N9Ij15GfWeVlvHzDPTOJk66NimJMJ4.',
        'admin@localhost', now(), now(), true, false, 0, 1);
INSERT INTO public.personal_data (first_name, last_name, phone_number, birth_date, user_id)
VALUES ('User', 'User', '123456789', '2001-01-01', '5e642d0a-94d4-4a4f-8760-cd6d63cd1038');

INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b91d7195-69a1-4073-9c95-52ceefd93cb7');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', 'b7a8dedb-bccc-4461-9b28-b60b5bf83e7f');
INSERT INTO public.users_roles (user_id, roles_id)
VALUES ('5e642d0a-94d4-4a4f-8760-cd6d63cd1038', '4d877c20-ebe9-4a7b-92e5-785873f277fe');