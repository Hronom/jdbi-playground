package com.github.hronom.jdbi_playground.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageDaoV2 {

    private final Jdbi primaryJdbi;

    @Autowired
    public MessageDaoV2(Jdbi primaryJdbi) {
        this.primaryJdbi = primaryJdbi;
    }

    public void batchUpsert(@BindBean List<Message> messages) {
        primaryJdbi.useTransaction(handle -> {
            PreparedBatch preparedBatch = handle.prepareBatch(
                    """
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
                    """
            );
            for (Message message : messages) {
                preparedBatch.bind("id", message.getId());
                preparedBatch.bind("userId", message.getUserId());
                preparedBatch.bind("message", message.getMessage());
                preparedBatch.bind("messageType", message.getMessageType());
                preparedBatch.add();
            }
            preparedBatch.execute();
        });
    }
}