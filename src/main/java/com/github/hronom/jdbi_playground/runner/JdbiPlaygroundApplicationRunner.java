package com.github.hronom.jdbi_playground.runner;

import com.github.hronom.jdbi_playground.dao.Message;
import com.github.hronom.jdbi_playground.dao.MessageDao;
import com.github.hronom.jdbi_playground.dao.MessageDaoV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JdbiPlaygroundApplicationRunner implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MessageDao messageDao;
    private final MessageDaoV2 messageDaoV2;

    @Autowired
    public JdbiPlaygroundApplicationRunner(
            MessageDao messageDao,
            MessageDaoV2 messageDaoV2
    ) {
        this.messageDao = messageDao;
        this.messageDaoV2 = messageDaoV2;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Message> messageList = new ArrayList<>(10);
        messageList.add(new Message(
                "1",
                "u1",
                "test1",
                "test1",
                null,
                null
        ));
        messageList.add(new Message(
                "1",
                "u1",
                "test2",
                "test2",
                null,
                null
        ));
        messageList.add(new Message(
                "1",
                "u1",
                "test3",
                "test3",
                null,
                null
        ));
//        for (int i = 2; i < 100; i++) {
//            messageList.add(new Message(
//                    String.valueOf(i),
//                    "u" + i,
//                    UUID.randomUUID().toString(),
//                    UUID.randomUUID().toString(),
//                    null,
//                    null
//            ));
//        }

        for (int i = 0; i < 5; i++) {
            try {
                messageDao.batchUpsertV2(messageList);
                LOGGER.info("Insert successfully on {} iterations", i);
                break;
            } catch (Exception e) {
                LOGGER.error("Error", e);
            }
        }
    }
}
