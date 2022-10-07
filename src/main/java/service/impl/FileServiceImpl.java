package service.impl;

import dao.FileDao;
import dao.impl.FileDaoImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import pojo.vo.Message;
import service.FileService;
import sun.misc.CharacterDecoder;

import java.io.*;

public class FileServiceImpl implements FileService {
    FileDao fileDao = new FileDaoImpl();

    //将base64的pdf转化为txt文本
    @Override
    public Message<?> UpLoad(HttpServletRequest request) {
        Message<?> message;
        //获取从前端传过来的文件的base64编码
        String fileBase64 = request.getParameter("fileBase64");

        //获取文件的个数
        Integer integer = fileDao.selectCount();
        int fileId = integer + 1;

        //去掉base64的头部
        int start = fileBase64.indexOf(",");
        String base64String = fileBase64.substring(start + 1, fileBase64.length());

        String url = getASPdf(base64String, fileId);
        String context = rePdf(url);//放回文本内容
        String textUrl = reAsText(context, fileId);

        //或许有可能获取不到？？？
        int userId = (int) request.getSession().getAttribute("userId");//通过session获取userId

        //调用dao层
        int result = fileDao.insertFileByUserId(userId, textUrl);
        if (result > 0) {
            message = new Message<>("上传成功", true);
        } else {
            message = new Message<>("上传失败", false);
        }
        return message;

    }


    /**
     * 将base64去掉头部后，转化为txt
     *
     * @param base64String
     * @param fileId
     * @return
     */
    @Override
    public String getASPdf(String base64String, int fileId) {

        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStreamb = null;
        String fileName = "D:/pdfFile/" + fileId + ".pdf";

        try {


            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            CharacterDecoder decoder = null;
            //将base64解码变成字节型的数组
            byte[] bytes = decoder.decodeBuffer(base64String);

            //读取数据的缓冲输入流对象
            bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
            //创建到file的输出流
            fileOutputStream = new FileOutputStream(file);
            // 为文件输出流对接缓冲输出流对象
            bufferedOutputStreamb = new BufferedOutputStream(fileOutputStream);

            //读取的内容放在bufferens
            byte[] buffers = new byte[1024];
            int len = bufferedInputStream.read(buffers);
            while (len != -1) {
                bufferedOutputStreamb.write(buffers, 0, len);
                len = bufferedInputStream.read(buffers);
            }
            // 刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bufferedOutputStreamb.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
                fileOutputStream.close();
                bufferedOutputStreamb.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return fileName;
    }

    /**
     * 将pdf转化为txt
     *
     * @param url
     */
    @Override
    public String rePdf(String url) {

        String context = null;
        try {
            File file = new File(url);
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            context = stripper.getText(document);
            //删除pdf文件
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        new test3().readString(context,fileId);

        return context;

    }

    /**
     * 传进来一个文本，把他存为text
     *
     * @param context
     * @param fileId
     * @return
     */
    @Override
    public String reAsText(String context, int fileId) {
        FileOutputStream fileOutputStream = null;
        String fileName = "D:/pdfFile/" + fileId + ".txt";
        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes = new byte[1024];
            bytes = context.getBytes();


            fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(bytes, 0, bytes.length);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }
}
