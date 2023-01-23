package com.boning.controller;

import com.boning.utils.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class test {


    @Autowired
    private MailClient mailClient;


    @RequestMapping(path = "/send", method = RequestMethod.GET)
    public void getIndexPage() {

        mailClient.sendMail("1096082464@qq.com", "aaa", "aaa");
    }

}
