package com.weweibuy.framework.common.util.sftp;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.apache.sshd.sftp.client.fs.SftpPath;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class StfpTest {

    @Test
    public void test01() throws Exception {
        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.addPasswordIdentity("ak402507979");
        sshClient.start();
        ClientSession session = sshClient.connect("sftp-user01",
                        "223.247.141.69", 10022)
                .verify(10L, TimeUnit.SECONDS)
                .getSession();
        session.auth().verify();

        SftpFileSystem sftpFileSystem = SftpClientFactory.instance()
                .createSftpFileSystem(session);

        SftpPath defaultDir = sftpFileSystem.getDefaultDir();
        System.err.println(defaultDir.toString());

    }


}