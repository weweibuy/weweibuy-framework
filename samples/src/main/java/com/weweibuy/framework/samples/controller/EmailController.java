package com.weweibuy.framework.samples.controller;

import com.weweibuy.common.util.email.EmailSender;
import com.weweibuy.common.util.email.model.EmailMessageVO;
import com.weweibuy.framework.common.core.utils.IdWorker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * @author durenhao
 * @date 2021/12/10 22:07
 **/
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailSender emailSender;

    @PostMapping("/send")
    public String sendMail(EmailMessageVO emailMessageVO, MultipartFile file) throws IOException {
        if (file != null) {
            EmailMessageVO.EmailAttachment emailAttachment = new EmailMessageVO.EmailAttachment();
            emailAttachment.setName(file.getOriginalFilename());
            InputStream inputStream = file.getInputStream();
            File file1 = new File("tmp/" + IdWorker.nextStringId());
            FileUtils.copyInputStreamToFile(inputStream, file1);
            emailMessageVO.setAttachmentList(Collections.singletonList(emailAttachment));
        }
        emailSender.sendEmail(emailMessageVO);
        return "success";
    }


}
