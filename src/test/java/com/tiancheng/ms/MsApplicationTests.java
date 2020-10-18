package com.tiancheng.ms;

import com.tiancheng.ms.dao.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@RunWith(SpringRunner.class)
class MsApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println("hello");
    }

    @Test
    void testUser() {
        System.out.println(userMapper.selectAll());
    }

    @Test
    void testInitProductProcess() throws SQLException, ClassNotFoundException, IOException {
        Connection conn = MySQLDataSource.getConnection();
        String queryProductProcess = "select * from tmp_product_process1";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(queryProductProcess);
        StringBuilder insertSqls = new StringBuilder();
        insertSqls.append("insert into tmp_product_process2 (code,process) values ");
        while (rs.next()) {
            String code = rs.getString(1);
            String processes = rs.getString(2);
            String[] processArr = processes.split(",");
            for (int i = 0;i<processArr.length;i++) {
                insertSqls.append("('" + code + "','" + processArr[i] + "'),");
            }
         }
        insertSqls.subSequence(0, insertSqls.length() - 1);
        System.out.println(insertSqls.toString());
    }



}
