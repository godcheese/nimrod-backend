package com.gioov.nimrod.mail.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gioov.common.mybatis.Pageable;
import com.gioov.common.mybatis.Sort;
import com.gioov.nimrod.common.Common;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.mail.entity.MailEntity;
import com.gioov.nimrod.mail.mapper.MailMapper;
import com.gioov.nimrod.mail.service.MailService;
import com.gioov.nimrod.system.service.DictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private MailMapper mailMapper;

    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private Common common;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MailService mailService;

    @JmsListener(destination = "mailQueue")
    @Override
    public void consume(String message) {
        MailEntity mailEntity = null;
        try {
            mailEntity = common.jsonToObject(message, MailEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mailEntity != null) {
            LOGGER.info("consume={}", mailEntity);
            send(mailEntity);
        }
    }

    @Override
    public void produce(String message) {
        Destination destination = new ActiveMQQueue("mailQueue");
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

    @Override
    public void initialize() {
        String host = (String) dictionaryService.get("MAIL", "HOST");
        String protocol = (String) dictionaryService.get("MAIL", "PROTOCOL");
        String port = (String) dictionaryService.get("MAIL", "PORT");
        String username = (String) dictionaryService.get("MAIL", "USERNAME");
        String password = (String) dictionaryService.get("MAIL", "PASSWORD");
        String defaultEncoding = (String) dictionaryService.get("MAIL", "DEFAULT_ENCODING");
        String smtpAuth = (String) dictionaryService.get("MAIL", "SMTP_AUTH");
        String startTlsEnable = (String) dictionaryService.get("MAIL", "STARTTLS_ENABLE");
        String startTlsRequired = (String) dictionaryService.get("MAIL", "STARTTLS_REQUIRED");

        javaMailSender = new JavaMailSenderImpl();
        if (host != null) {
            javaMailSender.setHost(host);
        }
        if (protocol != null) {
            javaMailSender.setProtocol(protocol);
        }
        if (port != null) {
            javaMailSender.setPort(Integer.valueOf(port));
        }
        if (username != null) {
            javaMailSender.setUsername(username);
        }
        if (password != null) {
            javaMailSender.setPassword(password);
        }
        if (password != null) {
            javaMailSender.setPassword(password);
        }
        if (defaultEncoding != null) {
            javaMailSender.setDefaultEncoding(defaultEncoding);
        }

        Properties properties = new Properties();
        if (smtpAuth != null) {
            properties.setProperty("mail.smtp.auth", smtpAuth);
        }
        if (startTlsEnable != null) {
            properties.setProperty("mail.starttls.enable", startTlsEnable);
        }
        if (startTlsRequired != null) {
            properties.setProperty("mail.starttls.required", startTlsRequired);
        }
        javaMailSender.setJavaMailProperties(properties);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void send(MailEntity mailEntity) {
        Integer smsStatusFail = Integer.valueOf((String) dictionaryService.get("SMS_STATUS", "FAIL"));
        Integer smsStatusSuccess = Integer.valueOf((String) dictionaryService.get("SMS_STATUS", "SUCCESS"));
        Integer isOrNotIs = Integer.valueOf((String) dictionaryService.get("IS_OR_NOT", "IS"));
        String mailSplit = ";";
        MailEntity mailEntity1 = mailMapper.getOne(mailEntity.getId());
        if (mailEntity1 != null) {
            try {
                mailEntity1.setStatus(smsStatusFail);
                String to = mailEntity1.getTo();
                if (mailEntity1.getHtml().equals(isOrNotIs)) {
                    if (to.indexOf(mailSplit) > 0) {
                        sendMimeMailMessage(mailEntity1.getFrom(), to.split(mailSplit), mailEntity1.getSubject(), mailEntity1.getText(), true);
                    } else {
                        sendMimeMailMessage(mailEntity1.getFrom(), to, mailEntity1.getSubject(), mailEntity1.getText(), true);
                    }
                } else {
                    if (to.indexOf(mailSplit) > 0) {
                        sendSimpleMailMessage(mailEntity1.getFrom(), to.split(mailSplit), mailEntity1.getSubject(), mailEntity1.getText());
                    } else {
                        sendSimpleMailMessage(mailEntity1.getFrom(), to, mailEntity1.getSubject(), mailEntity1.getText());
                    }
                }
                mailEntity1.setStatus((smsStatusSuccess));
                LOGGER.info("send success.");
            } catch (Exception e) {
                mailEntity1.setError(e.getMessage());
                mailEntity1.setStatus(smsStatusFail);
            }
            mailEntity1.setGmtModified(new Date());
            mailMapper.updateOne(mailEntity1);
        }
    }

    private void sendSimpleMailMessage(String from, String to, String subject, String text) {
        sendSimpleMailMessage(from, new String[]{to}, subject, text);
    }

    private void sendSimpleMailMessage(String from, String[] toArray, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(toArray);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }

    private void sendSimpleMailMessage(String from, List<String> toList, String subject, String text) {
        String[] toArray = new String[toList.size()];
        sendSimpleMailMessage(from, toArray, subject, text);
    }

    private void sendMimeMailMessage(String from, String[] toArray, String subject, String text, boolean html) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(toArray);
        mimeMessage.setSubject(subject);
        mimeMessageHelper.setText(text, html);
        javaMailSender.send(mimeMessage);
    }

    private void sendMimeMailMessage(String from, String to, String subject, String text, boolean html) throws MessagingException {
        sendMimeMailMessage(from, new String[]{to}, subject, text, html);
    }

    private void sendMimeMailMessage(String from, List<String> toList, String subject, String text, boolean html) throws MessagingException {
        String[] toArray = new String[toList.size()];
        sendMimeMailMessage(from, toList.toArray(toArray), subject, text, html);
    }

    @Override
    public Pagination<MailEntity> pageAll(Integer page, Integer rows, Sort sort) {
        Pagination<MailEntity> pagination = new Pagination<>();
        PageHelper.startPage(page, rows);
        Page<MailEntity> mailEntityPage = mailMapper.pageAll();
            pagination.setRows(mailEntityPage.getResult());
        pagination.setTotal(mailEntityPage.getTotal());
        return pagination;
    }

    /**
     * 设置 from 为数据字典的值
     *
     * @param mailEntity MailEntity
     */
    private void setFrom(MailEntity mailEntity) {
        String from = mailEntity.getFrom();
        if (from == null || "".equals(from)) {
            from = (String) dictionaryService.get("MAIL", "FROM");
            if (from != null) {
                mailEntity.setFrom(from);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MailEntity insertOne(MailEntity mailEntity) {
        Integer smsStatusWait = Integer.valueOf((String) dictionaryService.get("SMS_STATUS", "WAIT"));
        Date date = new Date();
        try {
            Integer status = mailEntity.getStatus();
            mailEntity.setStatus(status != null ? status : smsStatusWait);
            setFrom(mailEntity);
            mailEntity.setGmtModified(date);
            mailEntity.setGmtCreated(date);
            mailMapper.insertOne(mailEntity);
            produce(common.objectToJson(mailEntity));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mailEntity;
    }

    @Override
    public int deleteAll(List<Long> idList) {
        return mailMapper.deleteAll(idList);
    }

    @Override
    public MailEntity getOne(Long id) {
        return mailMapper.getOne(id);
    }

    @Override
    public void retry(List<MailEntity> mailEntityList) {
        for(MailEntity mailEntity : mailEntityList) {
            try {
                mailService.produce(common.objectToJson(mailEntity));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void retry(boolean fail) {
        Integer smsStatusWait = Integer.valueOf((String) dictionaryService.get("SMS_STATUS", "WAIT"));
        Integer smsStatusFail = Integer.valueOf((String) dictionaryService.get("SMS_STATUS", "FAIL"));

        List<Integer> statusList = new ArrayList<>();
        statusList.add(smsStatusWait);
        if(fail) {
            statusList.add(smsStatusFail);
        }
        List<MailEntity> mailEntityList = mailMapper.listAllByStatus(statusList);
        retry(mailEntityList);
    }

}