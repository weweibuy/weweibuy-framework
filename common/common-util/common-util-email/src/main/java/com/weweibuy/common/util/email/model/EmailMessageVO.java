package com.weweibuy.common.util.email.model;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 邮件模型
 *
 * @author durenhao
 * @date 2021/12/10 21:25
 **/
@Data
public class EmailMessageVO {

    /**
     * 发件人
     */
    private String from;

    /**
     * 收件人
     */
    private List<String> to;

    /**
     * 抄送人
     */
    private List<String> cc;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String text;

    /**
     * 是否为 Html或带附件的邮件, 默认 false
     */
    private Boolean multipart;

    /**
     * 邮件附件
     */
    private List<EmailAttachment> attachmentList;

    /**
     * 邮件附件
     */
    @Data
    public static class EmailAttachment {

        /**
         * 附件名称
         */
        private String name;

        /**
         * 附件文件  流与文件二选一
         */
        private File file;


    }


}
