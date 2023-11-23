/*
 * Author Aron Taddese.
 *
 * This controller Will help to fetch languages.
 *
 */

package com.example.gebeya.ussd.ussdbankingdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController

public class Language {
    @Autowired
    ResourceBundleMessageSource messageSource;

    @RequestMapping(value = "/",method =  RequestMethod.GET)
    public String[] getSource(@RequestHeader("Accept-Language") String locale){
        Locale locale1 = new Locale(locale);
        String[] messages = new String[8];
        for(int i= 0 ; i < messages.length; i++){
            String messageKey = "message" + i;
            messages[i] = messageSource.getMessage(messageKey,null,locale1);
        }
        return messages;

    }
}
