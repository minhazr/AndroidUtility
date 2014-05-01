
package com.android.utility.protocol;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;

public class Ftp {
    private static final String TAG = Ftp.class.getSimpleName();
    /********* work only for Dedicated IP ***********/
    private static final String FTP_HOST = "50.63.92.56";

    /********* FTP USERNAME ***********/
    private static String user = "XXXXXX";

    /********* FTP PASSWORD ***********/
    private static String pass = "XXXXXXX";

    public void setLogin(String user_name, String pass) {
        user = user_name;
        this.pass = pass;
    }

    public static void upload(File fileName) {
        FTPClient ftpClient = new FTPClient();

        try
        {
            ftpClient.connect(InetAddress.getByName(FTP_HOST));
            ftpClient.login(user, pass);

            if (FTPReply.isPositiveCompletion(ftpClient.getReply()))
            {

                FileOutputStream desFileStream = new FileOutputStream(fileName);
                FileInputStream srcFileStream = new FileInputStream(fileName);
                // ftpClient.storeFile(fileName, srcFileStream);

            }

            // ftpClient.changeWorkingDirectory(PATH);

            if (ftpClient.getReplyString().contains("250"))
            {
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                BufferedInputStream buffIn = null;
                buffIn = new BufferedInputStream(new FileInputStream(fileName));
                ftpClient.enterLocalPassiveMode();

                // boolean result = ftpClient.s
                buffIn.close();
                ftpClient.logout();
                ftpClient.disconnect();
            }

        }
        catch (SocketException e)
        {
            Logger.e(LogModule.NETWORK, TAG, e.getStackTrace().toString());
        }
        catch (UnknownHostException e)
        {
            Logger.e(LogModule.NETWORK, TAG, e.getStackTrace().toString());
        }
        catch (IOException e)
        {
            Logger.e(LogModule.NETWORK, TAG, e.getStackTrace().toString());
        }

    }

    public static void download() {

    }
}
