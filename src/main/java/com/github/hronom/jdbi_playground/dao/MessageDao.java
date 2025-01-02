package com.github.hronom.jdbi_playground.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;

import java.util.List;

@RegisterBeanMapper(Message.class)
public interface MessageDao {
    @SqlBatch("""
              INSERT INTO message(
                id,
                user_id,
                message,
                message_type,
                created_at,
                updated_at
              )
              VALUES(
                :id,
                :userId,
                :message,
                :messageType,
                current_timestamp,
                current_timestamp
              )
              ON CONFLICT(id) DO UPDATE
              SET user_id = EXCLUDED.user_id,
                  message = EXCLUDED.message,
                  message_type = EXCLUDED.message_type,
                  updated_at = current_timestamp
              RETURNING
                id,
                user_id,
                message,
                message_type,
                created_at,
                updated_at;
              """)
    void batchUpsertV1(@BindBean List<Message> messages);

    @SqlBatch("""
              INSERT INTO message(
                id,
                user_id,
                message,
                message_type,
                created_at,
                updated_at
              )
              VALUES(
                :id,
                :userId,
                :message,
                :messageType,
                current_timestamp,
                current_timestamp
              )
              ON CONFLICT(id) DO UPDATE
              SET user_id = EXCLUDED.user_id,
                  message = EXCLUDED.message,
                  message_type = EXCLUDED.message_type,
                  updated_at = current_timestamp;
              """)
    void batchUpsertV2(@BindBean List<Message> messages);
}