-- Inspired by https://stackoverflow.com/a/6508293/285571
------------------------------------------------------------------------------------------------------------------------
-- Primary
-- Primary DB for dev
create database dev_jdbi_playground encoding 'utf8';
\connect dev_jdbi_playground;
create schema dev_jdbi_playground;

CREATE TABLE dev_jdbi_playground.message (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256),
    message VARCHAR(256),
    message_type VARCHAR(256),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX user_id_idx ON dev_jdbi_playground.message USING btree (user_id) WITH (fillfactor = 30);
CREATE INDEX user_id_message_type_idx ON dev_jdbi_playground.message USING btree (user_id, message_type) WITH (fillfactor = 30);