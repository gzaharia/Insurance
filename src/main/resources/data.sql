-- Insert admin
INSERT INTO employees (created, updated, password, username)
SELECT current_timestamp, current_timestamp, '$2a$10$LYOnoAtaetxMbbmo8EhYzO2RjnOoZlSPXwn6YJswJ2clQF.kgCo8O', 'admin'
WHERE NOT EXISTS (
        SELECT * FROM employees WHERE username = 'admin'
    );

-- Insert roles
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
        SELECT * FROM roles WHERE name = 'ROLE_ADMIN'
    );

INSERT INTO roles (name)
SELECT 'ROLE_MODERATOR'
WHERE NOT EXISTS (
        SELECT * FROM roles WHERE name = 'ROLE_MODERATOR'
    );

-- Insert user roles
INSERT INTO employee_roles (role_id, employee_id)
SELECT roles.id, employees.id FROM roles, employees
WHERE roles.name = 'ROLE_ADMIN' AND employees.username = 'admin'
ON CONFLICT DO NOTHING;

INSERT INTO employee_roles (role_id, employee_id)
SELECT roles.id, employees.id FROM roles, employees
WHERE roles.name = 'ROLE_MODERATOR' AND employees.username = 'admin'
ON CONFLICT DO NOTHING;
