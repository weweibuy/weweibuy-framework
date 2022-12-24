package com.weweibuy.common.util.email;

import com.weweibuy.common.util.email.model.EmailMessageVO;
import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/12/10 21:22
 **/
@Slf4j
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;


    public void sendEmail(EmailMessageVO emailMessageVO) {
        if (CollectionUtils.isEmpty(emailMessageVO.getTo())) {
            log.warn("邮件信息: {}, 收件人为空", emailMessageVO);
            return;
        }
        Boolean multipart = Optional.ofNullable(emailMessageVO.getMultipart())
                .orElse(false);
        if (CollectionUtils.isEmpty(emailMessageVO.getAttachmentList())
                && !multipart) {
            sendSimpleEmail(emailMessageVO);
        } else {
            try {
                sendMimeEmail(emailMessageVO);
            } catch (MessagingException e) {
                throw Exceptions.business("邮件信息异常", e);
            }
        }
    }


    private void sendSimpleEmail(EmailMessageVO emailMessageVO) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailMessageVO.getFrom());
        simpleMailMessage.setTo(emailMessageVO.getTo().toArray(new String[emailMessageVO.getTo().size()]));
        Optional.ofNullable(emailMessageVO.getCc())
                .filter(CollectionUtils::isNotEmpty)
                .map(l -> l.toArray(new String[l.size()]))
                .ifPresent(simpleMailMessage::setCc);
        Optional.ofNullable(emailMessageVO.getSubject())
                .ifPresent(simpleMailMessage::setSubject);
        Optional.ofNullable(emailMessageVO.getText())
                .ifPresent(simpleMailMessage::setText);
        mailSender.send(simpleMailMessage);
    }


    private void sendMimeEmail(EmailMessageVO emailMessageVO) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //创建一个复杂的消息邮件
        //用MimeMessageHelper来包装MimeMessage
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(emailMessageVO.getFrom());
        mimeMessageHelper.setTo(emailMessageVO.getTo().toArray(new String[emailMessageVO.getTo().size()]));
        List<String> cc = emailMessageVO.getCc();
        if (CollectionUtils.isNotEmpty(cc)) {
            mimeMessageHelper.setCc(cc.toArray(new String[cc.size()]));
        }

        String subject = emailMessageVO.getSubject();
        if (StringUtils.isNotBlank(subject)) {
            mimeMessageHelper.setSubject(subject);
        }

        String text = emailMessageVO.getText();
        if (StringUtils.isNotBlank(text)) {
            mimeMessageHelper.setText(text);
        }

        List<EmailMessageVO.EmailAttachment> attachmentList = emailMessageVO.getAttachmentList();
        for (int i = 0; i < attachmentList.size(); i++) {
            EmailMessageVO.EmailAttachment emailAttachment = attachmentList.get(i);
            mimeMessageHelper.addAttachment(emailAttachment.getName(),
                    emailAttachment.getFile());
        }
        mailSender.send(mimeMessage);
    }


}
