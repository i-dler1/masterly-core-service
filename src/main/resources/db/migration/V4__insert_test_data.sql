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
ALTER SEQUENCE IF EXISTS availability_slots_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS appointments_id_seq RESTART WITH 1;

-- =====================================================
-- МАСТЕРА (пароль: 123)
-- =====================================================
INSERT INTO masters (email, password_hash, full_name, phone, business_name, specialization, role, is_active, created_at, updated_at)
VALUES
    ('master1@masterly.com', '123', 'Анна Визажист', '+375291112231', 'Anna Beauty', 'Визажист', 'MASTER', true, NOW(), NOW()),
    ('master2@masterly.com', '123', 'Ольга Парикмахер', '+375291112232', 'Olga Style', 'Парикмахер', 'MASTER', true, NOW(), NOW()),
    ('master3@masterly.com', '123', 'Мария Маникюр', '+375291112233', 'Maria Nails', 'Маникюр', 'MASTER', true, NOW(), NOW());

-- =====================================================
-- КЛИЕНТЫ (по 2 на каждого мастера)
-- =====================================================
INSERT INTO clients (master_id, full_name, phone, email, role, created_at, updated_at)
VALUES
    -- Клиенты мастера 1 (Визажист)
    (1, 'Татьяна Иванова', '+375331234561', 'tatiana@mail.com', 'CLIENT', NOW(), NOW()),
    (1, 'Ирина Козлова', '+375331234562', 'irina@mail.com', 'CLIENT', NOW(), NOW()),
    -- Клиенты мастера 2 (Парикмахер)
    (2, 'Светлана Петрова', '+375331234563', 'svetlana@mail.com', 'CLIENT', NOW(), NOW()),
    (2, 'Анна Соколова', '+375331234564', 'anna@mail.com', 'CLIENT', NOW(), NOW()),
    -- Клиенты мастера 3 (Маникюр)
    (3, 'Наталья Сидорова', '+375331234565', 'natalia@mail.com', 'CLIENT', NOW(), NOW()),
    (3, 'Екатерина Морозова', '+375331234566', 'ekaterina@mail.com', 'CLIENT', NOW(), NOW());

-- =====================================================
-- МАТЕРИАЛЫ (по 5 на каждого мастера)
-- =====================================================
INSERT INTO materials (master_id, name, unit, quantity, min_quantity, price_per_unit, last_updated)
VALUES
    -- Мастер 1 (Визажист)
    (1, 'Тональная основа', 'мл', 200, 30, 25.0, NOW()),
    (1, 'Пудра', 'г', 100, 20, 15.0, NOW()),
    (1, 'Тени для век', 'шт', 15, 3, 10.0, NOW()),
    (1, 'Тушь для ресниц', 'шт', 10, 2, 20.0, NOW()),
    (1, 'Кисти для макияжа', 'шт', 20, 5, 8.0, NOW()),
    -- Мастер 2 (Парикмахер)
    (2, 'Краска для волос', 'мл', 500, 50, 12.0, NOW()),
    (2, 'Оксидант 3%', 'мл', 1000, 100, 5.0, NOW()),
    (2, 'Оксидант 6%', 'мл', 800, 80, 5.0, NOW()),
    (2, 'Шампунь', 'мл', 2000, 200, 8.0, NOW()),
    (2, 'Маска для волос', 'мл', 1000, 100, 15.0, NOW()),
    -- Мастер 3 (Маникюр)
    (3, 'Биогель', 'мл', 300, 50, 18.0, NOW()),
    (3, 'Праймер', 'мл', 150, 20, 8.0, NOW()),
    (3, 'Лак для ногтей', 'мл', 400, 80, 6.0, NOW()),
    (3, 'Жидкость для снятия лака', 'мл', 600, 100, 4.0, NOW()),
    (3, 'Пилки для ногтей', 'шт', 30, 10, 3.0, NOW());

-- =====================================================
-- УСЛУГИ (по 3 на каждого мастера)
-- =====================================================
INSERT INTO services (master_id, name, description, duration_minutes, price, is_active, created_at, updated_at)
VALUES
    -- Мастер 1 (Визажист)
    (1, 'Дневной макияж', 'Естественный дневной макияж', 60, 50.0, true, NOW(), NOW()),
    (1, 'Вечерний макияж', 'Яркий вечерний макияж', 90, 80.0, true, NOW(), NOW()),
    (1, 'Свадебный макияж', 'Профессиональный свадебный макияж', 120, 120.0, true, NOW(), NOW()),
    -- Мастер 2 (Парикмахер)
    (2, 'Женская стрижка', 'Модельная стрижка с укладкой', 60, 40.0, true, NOW(), NOW()),
    (2, 'Окрашивание', 'Окрашивание волос любой сложности', 120, 80.0, true, NOW(), NOW()),
    (2, 'Укладка', 'Повседневная или вечерняя укладка', 45, 30.0, true, NOW(), NOW()),
    -- Мастер 3 (Маникюр)
    (3, 'Классический маникюр', 'Обрезной маникюр с покрытием', 60, 25.0, true, NOW(), NOW()),
    (3, 'Маникюр с дизайном', 'Маникюр с художественным дизайном', 90, 40.0, true, NOW(), NOW()),
    (3, 'Педикюр', 'Классический педикюр', 90, 45.0, true, NOW(), NOW());

-- =====================================================
-- СВОБОДНЫЕ СЛОТЫ (по 2 на каждую услугу своего мастера)
-- =====================================================
INSERT INTO availability_slots (master_id, service_id, slot_date, start_time, end_time, is_booked, created_at, updated_at)
VALUES
    -- Мастер 1, услуга 1 (Дневной макияж)
    (1, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', false, NOW(), NOW()),
    (1, 1, CURRENT_DATE + INTERVAL '2 days', '11:00:00', '12:00:00', false, NOW(), NOW()),
    -- Мастер 1, услуга 2 (Вечерний макияж)
    (1, 2, CURRENT_DATE + INTERVAL '1 day', '14:00:00', '15:30:00', false, NOW(), NOW()),
    (1, 2, CURRENT_DATE + INTERVAL '3 days', '15:00:00', '16:30:00', false, NOW(), NOW()),
    -- Мастер 1, услуга 3 (Свадебный макияж)
    (1, 3, CURRENT_DATE + INTERVAL '5 days', '12:00:00', '14:00:00', false, NOW(), NOW()),
    (1, 3, CURRENT_DATE + INTERVAL '7 days', '10:00:00', '12:00:00', false, NOW(), NOW()),

    -- Мастер 2, услуга 4 (Женская стрижка)
    (2, 4, CURRENT_DATE + INTERVAL '1 day', '09:00:00', '10:00:00', false, NOW(), NOW()),
    (2, 4, CURRENT_DATE + INTERVAL '2 days', '10:00:00', '11:00:00', false, NOW(), NOW()),
    -- Мастер 2, услуга 5 (Окрашивание)
    (2, 5, CURRENT_DATE + INTERVAL '1 day', '13:00:00', '15:00:00', false, NOW(), NOW()),
    (2, 5, CURRENT_DATE + INTERVAL '3 days', '14:00:00', '16:00:00', false, NOW(), NOW()),
    -- Мастер 2, услуга 6 (Укладка)
    (2, 6, CURRENT_DATE + INTERVAL '2 days', '11:00:00', '12:00:00', false, NOW(), NOW()),
    (2, 6, CURRENT_DATE + INTERVAL '4 days', '16:00:00', '17:00:00', false, NOW(), NOW()),

    -- Мастер 3, услуга 7 (Классический маникюр)
    (3, 7, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', false, NOW(), NOW()),
    (3, 7, CURRENT_DATE + INTERVAL '2 days', '11:00:00', '12:00:00', false, NOW(), NOW()),
    -- Мастер 3, услуга 8 (Маникюр с дизайном)
    (3, 8, CURRENT_DATE + INTERVAL '1 day', '14:00:00', '15:30:00', false, NOW(), NOW()),
    (3, 8, CURRENT_DATE + INTERVAL '3 days', '15:00:00', '16:30:00', false, NOW(), NOW()),
    -- Мастер 3, услуга 9 (Педикюр)
    (3, 9, CURRENT_DATE + INTERVAL '4 days', '09:00:00', '10:30:00', false, NOW(), NOW()),
    (3, 9, CURRENT_DATE + INTERVAL '5 days', '10:00:00', '11:30:00', false, NOW(), NOW());

-- =====================================================
-- ЗАПИСИ (по 2 на каждого мастера к его клиентам)
-- =====================================================
INSERT INTO appointments (master_id, client_id, service_id, appointment_date, start_time, end_time, status, notes, created_at, updated_at)
VALUES
    -- Мастер 1 (клиенты 1 и 2)
    (1, 1, 1, CURRENT_DATE + INTERVAL '1 day', '15:00:00', '16:00:00', 'CONFIRMED', 'Дневной макияж', NOW(), NOW()),
    (1, 2, 2, CURRENT_DATE + INTERVAL '1 day', '16:30:00', '18:00:00', 'CONFIRMED', 'Вечерний макияж', NOW(), NOW()),
    -- Мастер 2 (клиенты 3 и 4)
    (2, 3, 4, CURRENT_DATE + INTERVAL '1 day', '09:00:00', '10:00:00', 'CONFIRMED', 'Стрижка', NOW(), NOW()),
    (2, 4, 5, CURRENT_DATE + INTERVAL '1 day', '13:00:00', '15:00:00', 'PENDING', 'Окрашивание', NOW(), NOW()),
    -- Мастер 3 (клиенты 5 и 6)
    (3, 5, 7, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:00:00', 'COMPLETED', 'Маникюр', NOW(), NOW()),
    (3, 6, 8, CURRENT_DATE + INTERVAL '1 day', '14:00:00', '15:30:00', 'CONFIRMED', 'Маникюр с дизайном', NOW(), NOW());

-- =====================================================
-- СВЯЗИ УСЛУГА-МАТЕРИАЛ
-- =====================================================
INSERT INTO service_materials (service_id, material_id, quantity_used)
VALUES
    -- Услуги мастера 1
    (1, 1, 2.0),   -- Дневной макияж -> Тональная основа
    (2, 2, 1.0),   -- Вечерний макияж -> Пудра
    (3, 3, 3.0),   -- Свадебный макияж -> Тени для век
    -- Услуги мастера 2
    (4, 6, 10.0),  -- Женская стрижка -> Краска для волос
    (5, 7, 50.0),  -- Окрашивание -> Оксидант 3%
    (6, 9, 20.0),  -- Укладка -> Шампунь
    -- Услуги мастера 3
    (7, 11, 5.0),  -- Классический маникюр -> Биогель
    (8, 12, 2.0),  -- Маникюр с дизайном -> Праймер
    (9, 13, 3.0);  -- Педикюр -> Лак для ногтей