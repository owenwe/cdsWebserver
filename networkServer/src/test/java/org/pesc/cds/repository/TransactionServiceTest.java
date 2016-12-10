package org.pesc.cds.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesc.cds.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by prabhu on 12/9/16.
 */

@ActiveProfiles({"mysql"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionServiceTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    TransactionService transactionService;


    @Test
    public void createTransaction() {

        String userName = "junit";
        Timestamp timestamp =  new Timestamp(new Date().getTime());
         int recepientId = 12345;

        Transaction transaction = new Transaction();
        transaction.setRecipientId(recepientId);
        transaction.setSenderId(124);
        transaction.setFileFormat("PDF");
        transaction.setDocumentType("Conf");
        transaction.setDepartment("EDU");
        transaction.setFileSize(3250689l);
        transaction.setOperation("SEND");
        transaction.setOccurredAt(timestamp);
        transaction.setAcknowledgedAt(timestamp);
        transaction.setSignerId(95004);
        transaction.setAcknowledged(Boolean.TRUE);



        int countRowsInTable =  JdbcTestUtils.countRowsInTable(jdbc,"transactions");
        System.err.println("TransactionCount " + countRowsInTable);
        Assert.assertNotEquals(0, countRowsInTable);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbc,"transactions","recipient_id = '" + recepientId + "' and acknowledged = false "));
        transactionService.create(transaction);
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbc,"transactions","recipient_id = '" + recepientId + "'"));


    }
}
