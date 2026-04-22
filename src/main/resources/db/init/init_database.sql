-- init_database.sql
-- Создание пользователя и базы данных для Masterly

-- Создаём пользователя (если не существует)
DO $$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'masterly_user') THEN
            CREATE USER masterly_user WITH PASSWORD 'masterly_password';
        END IF;
    END
$$;

-- Создаём базу данных (если не существует)
DO $$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'masterly_db') THEN
            CREATE DATABASE masterly_db OWNER masterly_user;
        END IF;
    END
$$;

-- Подключаемся к новой базе данных
\c masterly_db

-- Настраиваем схему public
ALTER SCHEMA public OWNER TO masterly_user;
GRANT ALL ON SCHEMA public TO masterly_user;
GRANT ALL PRIVILEGES ON DATABASE masterly_db TO masterly_user;