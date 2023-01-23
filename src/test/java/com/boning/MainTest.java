package com.boning;

import com.boning.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MainTest {


    @Autowired
    private MailClient mailClient;

    @Test
    public void test() {
        mailClient.sendMail("1096082464@qq.com", "aaa", "aaa");
    }
}
