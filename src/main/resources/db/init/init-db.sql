-- Удаляем старую БД и пользователя
DROP DATABASE IF EXISTS masterly_db;
DROP USER IF EXISTS masterly_user;

-- Создаём пользователя
CREATE USER masterly_user WITH PASSWORD 'masterly_password';

-- Создаём БД
CREATE DATABASE masterly_db OWNER masterly_user;
