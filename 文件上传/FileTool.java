package linekong.glass.pk.ftp;

import android.os.Handler;
import android.os.Message;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.NumberFormat;

import linekong.glass.pk.utils.Constant;
import linekong.glass.pk.utils.L;

public class FileTool {

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param url      FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path     FTP服务器保存目录，是linux下的目录形式,如/photo/
     * @param filename 上传到FTP服务器上的文件名,是自己定义的名字，
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, int port, String username,
                                     String password, String path, String filename, InputStream input, File file, Handler mHandler) {
        boolean success = false;
        FTPClient ftp = new FTPClient();


        int reply;
        try {
            ftp.connect(url, port);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            L.Prinf("---error2");
            e.printStackTrace();

        } catch (IOException e) {
            L.Prinf("---erro3");
            e.printStackTrace();
        }// 连接FTP服务器
//             如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
        try {
            ftp.login(username, password);
        } catch (IOException e) {
            L.Prinf("---error1");
            e.printStackTrace();
        }//登录
        reply = ftp.getReplyCode();
        L.Prinf("---reply" + reply);
        if (!FTPReply.isPositiveCompletion(reply)) {
            L.Prinf("---error4");
            try {
                ftp.disconnect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                L.Prinf("---error5");
                e.printStackTrace();
            }
            return success;
        }
        try {
            ftp.makeDirectory(path);
            ftp.changeWorkingDirectory(path);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            L.Prinf("---error6");
            e.printStackTrace();
        }
        try {
            ftp.enterLocalPassiveMode();
//          ftp.storeFile(filename, input);


             NumberFormat numberFormat = NumberFormat.getInstance();
             numberFormat.setMaximumFractionDigits(2);
             long fileLength = file.length();


//            *-----上传进度-----
                OutputStream outputStream = ftp.storeFileStream(filename);
				byte[] bytesIn = new byte[4096];
				int read = 0;
				int now_progress=0;

				while ((read = input.read(bytesIn)) != -1) {
					outputStream.write(bytesIn, 0, read);
					now_progress+=read;
//					int complete_percent = (int)((now_progress*100)/(file.length()));
                    String result = numberFormat.format((float) now_progress / (float) fileLength * 100) + "%";
                    Message m = mHandler.obtainMessage();
                    m.what = Constant.FTP_PROGRESS;
                    m.obj = "升级进度: "+result;
                    mHandler.sendMessage(m);
				}
				input.close();
				outputStream.close();
				boolean completed = ftp.completePendingCommand();
				if (completed){
					L.Prinf( " uploaded successfully.");
                    /**---------文件通过ftp上传进度通知反馈到界面---------*/
                    Message m = mHandler.obtainMessage();
                    m.what = Constant.FTP_PROGRESS;
                    m.obj = "升级进度: 100%";
                    mHandler.sendMessage(m);
                    /**---------文件通过ftp上传进度通知反馈给C20---------*/
                    mHandler.sendEmptyMessage(Constant.NOTIFY_FTP_UPLOAD_SUCCESS);
				}
            /**-----上传进度-----*/

        } catch (IOException e) {
            L.Prinf("---error7");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        try {
//            input.close();
//
//        } catch (IOException e) {
//            L.Prinf("---error8");
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        try {
            ftp.logout();
        } catch (IOException e) {
            L.Prinf("---error9");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        success = true;

        return success;
    }

    // 测试  
    public static void main(String[] args) {

//        FileInputStream in = null;
//        File dir = new File("E://ftpClient//test.txt");
//        File files[] = dir.listFiles();
//        try {
//            in = new FileInputStream(dir);
//            boolean flag = uploadFile("172.16.1.152", 2121, "a", "a",
//                    "/mnt1/", "testFile.txt", in);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }
}  