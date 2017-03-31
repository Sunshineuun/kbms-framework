package com.winning.dao.test;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.winning.kbms.core.service.AuthoritzationService;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (
{ "classpath*:spring-configs/applicationContext*.xml" })
public class TestBatchSqlSession
{
    @Resource (name = "authoritzationService")
    private AuthoritzationService authoritzationService;

    @Test
    public void testAuthorizeToUser (){
        
    }
}
