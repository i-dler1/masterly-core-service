-- Очистка таблиц
TRUNCATE TABLE service_materials CASCADE;
TRUNCATE TABLE appointments CASCADE;
TRUNCATE TABLE availability_slots CASCADE;
TRUNCATE TABLE services CASCADE;
TRUNCATE TABLE materials CASCADE;
TRUNCATE TABLE clients CASCADE;
TRUNCATE TABLE masters CASCADE;

-- Сброс последовательностей
ALTER SEQUENCE IF EXISTS masters_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS clients_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS materials_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS services_id_seq RESTART WITH 1;

INSERT INTO masters (email, password_hash, full_name, phone, business_name, specialization, role, is_active, created_at, updated_at)
VALUES
    ('elena.master@masterly.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Елена Прекрасная', '+375291112234', 'Elena Beauty', 'Визажист', 'MASTER', true, NOW(), NOW()),
    ('olga.master@masterly.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Ольга Мудрая', '+375291112235', 'Olga Style', 'Парикмахер', 'MASTER', true, NOW(), NOW()),
    ('maria.master@masterly.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Мария Искусная', '+375291112236', 'Maria Nails', 'Маникюр', 'MASTER', true, NOW(), NOW());

INSERT INTO clients (master_id, full_name, phone, email, role, created_at, updated_at)
VALUES
    (1, 'Татьяна Иванова', '+375331234568', 'tatiana@mail.com', 'CLIENT', NOW(), NOW()),
    (2, 'Светлана Петрова', '+375331234569', 'svetlana@mail.com', 'CLIENT', NOW(), NOW()),
    (3, 'Наталья Сидорова', '+375331234570', 'natalia@mail.com', 'CLIENT', NOW(), NOW()),
    (1, 'Ирина Козлова', '+375331234571', 'irina@mail.com', 'CLIENT', NOW(), NOW()),
    (2, 'Анна Соколова', '+375331234572', 'anna@mail.com', 'CLIENT', NOW(), NOW());

INSERT INTO materials (master_id, name, unit, quantity, min_quantity, price_per_unit, last_updated)
VALUES
    (2, 'Лак для ногтей', 'мл', 300, 50, 8.0, NOW()),
    (2, 'Жидкость для снятия лака', 'мл', 500, 100, 3.0, NOW()),
    (3, 'Биогель', 'мл', 200, 30, 15.0, NOW()),
    (3, 'Праймер', 'мл', 150, 20, 5.0, NOW()),
    (1, 'Плойка для волос', 'шт', 3, 1, 50.0, NOW());

INSERT INTO services (master_id, name, description, duration_minutes, price, is_active, created_at, updated_at)
VALUES
    (2, 'Вечерний макияж', 'Яркий вечерний макияж с наращиванием ресниц', 120, 80.0, true, NOW(), NOW()),
    (2, 'Дневной макияж', 'Естественный дневной макияж', 60, 45.0, true, NOW(), NOW()),
    (3, 'Маникюр с дизайном', 'Классический маникюр с дизайном', 120, 45.0, true, NOW(), NOW()),
    (3, 'Педикюр', 'Классический педикюр', 90, 55.0, true, NOW(), NOW()),
    (1, 'Биозавивка', 'Биозавивка ресниц', 90, 70.0, true, NOW(), NOW());

INSERT INTO availability_slots (master_id, service_id, slot_date, start_time, end_time, is_booked, created_at, updated_at)
VALUES
    (1, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', false, NOW(), NOW()),
    (1, 2, CURRENT_DATE + INTERVAL '1 day', '14:00:00', '15:30:00', false, NOW(), NOW()),
    (2, 5, CURRENT_DATE + INTERVAL '2 days', '11:00:00', '12:00:00', false, NOW(), NOW()),
    (3, 7, CURRENT_DATE + INTERVAL '3 days', '09:00:00', '10:00:00', false, NOW(), NOW()),
    (2, 6, CURRENT_DATE + INTERVAL '2 days', '15:00:00', '16:00:00', true, NOW(), NOW());

INSERT INTO appointments (master_id, client_id, service_id, appointment_date, start_time, end_time, status, notes, created_at, updated_at)
VALUES
    (1, 1, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', 'CONFIRMED', 'Первое посещение', NOW(), NOW()),
    (1, 4, 2, CURRENT_DATE + INTERVAL '1 day', '14:00:00', '15:30:00', 'PENDING', 'Хочу блонд', NOW(), NOW()),
    (2, 2, 5, CURRENT_DATE + INTERVAL '2 days', '11:00:00', '12:00:00', 'CONFIRMED', 'Свадебный макияж', NOW(), NOW()),
    (3, 3, 7, CURRENT_DATE + INTERVAL '3 days', '09:00:00', '10:00:00', 'PENDING', 'Классика', NOW(), NOW()),
    (2, 5, 6, CURRENT_DATE + INTERVAL '2 days', '15:00:00', '16:00:00', 'COMPLETED', 'Повторный клиент', NOW(), NOW()),
    (1, 4, 3, CURRENT_DATE + INTERVAL '4 days', '12:00:00', '14:00:00', 'SCHEDULED', 'Сложное окрашивание', NOW(), NOW());

INSERT INTO service_materials (service_id, material_id, quantity_used)
VALUES
    (5, 6, 20.0),
    (6, 7, 15.0),
    (7, 8, 10.0),
    (8, 9, 5.0);