package service.impl;

import dao.ModleDao;
import dao.impl.ModleDaoImpl;
import easydao.utils.Resources;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import pojo.po.Modle;
import pojo.vo.Message;
import service.ModleService;
import utils.StringUtil;

import java.io.*;
import java.sql.Connection;
import java.util.Base64;
import java.util.UUID;

public class ModleServiceImpl implements ModleService {
    private final ModleDao modleDao = new ModleDaoImpl();

    /**
     * 2022.10.10:1.41
     * <p>
     * //尝试解决前端PDF转化Base64后请求头过长问题
     *
     * @param request
     * @return
     */
    @Override
    public Message parsePDFContent(HttpServletRequest request) {
        try {
            Part pdfFile = request.getPart("pdfFile");//获取文件
            Message msg;
            if (pdfFile != null) {
                //文件不为空
                InputStream input = pdfFile.getInputStream();

                //--------------------  有bug-----------------------------

//                //将文件通过流转成字符串
//                byte[] bytes = new byte[1024];
//                int len;
//                StringBuilder sb = new StringBuilder();
//                while ((len = input.read(bytes)) != -1) {
//                    sb.append(new String(bytes, 0, len));
//                }
//                //将字符串转换成base64字符串
//                String fileBase64 = new String(Base64.getEncoder().encode(sb.toString().getBytes()));
//                System.out.println(fileBase64);
//                //在request域中储存该base64字符串
//                request.setAttribute("fileBase64", fileBase64);
                //让upload继续根据base64逻辑处理(略有修改，parameter拿不到参数时会从attribute中拿)

                //------------------------ 尝试不转成base64------------------------------
                request.setAttribute("fileInputStream", input);

                return UpLoad(request);
            } else {
                msg = new Message("文件上传错误");
                msg.addData("uploadSuccess", false);
            }
            return msg;
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    //将base64的pdf转化为txt文本,然后提取
    @Override
    public Message UpLoad(HttpServletRequest request) {
        Message message;
//        //获取从前端传过来的文件的base64编码
//        String fileBase64 = request.getParameter("fileBase64");
//        //10.10 修改部分
//        if (fileBase64 == null) {
//            fileBase64 = (String) request.getAttribute("fileBase64");
//        }

//        int start = fileBase64.indexOf(",");
//        String base64String = fileBase64.substring(start + 1, fileBase64.length());

//        String url = getPdfPath(base64String, "pdfFile");// 获取pdf文件的存储位置,这个pdfFile只是一个中间过度的
//        String url = getPdfPath(fileBase64, "pdfFile");// 获取pdf文件的存储位置,这个pdfFile只是一个中间过度的
//        String context = rePdf(url);//拿到pdf里面的内容

        //假数据,传一个假的context
//        String context="一 中国武装力量的构成 ①《抓任命共和国国防法》规定：”中华任命共和国的武装力量，由中国人民解放军现役部 队和预备役部队，" +
//                "中国人民武装警察部队，民兵组成。” ②中华人民共和国的基本体制是“三结合”即由中国任命解放军，中国人民武装警察部队和 民兵三结合。 " +
//                "③我国的武装力量";

//        int userId = Integer.parseInt(request.getParameter("userId"));//前端可以储存userId解决跨域session失效问题


        //-----------------------------不使用base64版本---------------------
        InputStream input = (InputStream) request.getAttribute("fileInputStream");
        String url = getPdfPath(input, "pdfFile");
        String context = rePdf(url);
        context = context.replaceAll("\\r\\n", "<\\br>");

        //将文件内容封装在message返回
        message = new Message("上传成功");
        message.addData("context", context);//响应的数据为pdf文本里面的内容
        return message;

    }


    /**
     * 将base64去掉头部后，转化为pdf
     *
     * @param input
     * @param pdfFile
     * @return
     */
    @Override
    public String getPdfPath(InputStream input, String pdfFile) {
        //将String base64String参数替换成流 ->
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStreamb = null;
        File file = null;

        // ! !  报错
//        String filePath = "D:/pdfFile/" + pdfFile + ".pdf"; //拼接存储的地址（pdf只是暂时存储在这里，一旦读取就删掉）
        //10.10 尝试解决办法
        String filePath = StringUtil.getTempURL(pdfFile);
        try {
            file = new File(filePath);
            //如果没有文件，要创建这个文件
            if (!file.exists()) {
                file.createNewFile();
            }


            //将base64解码变成字节型的数组

            //我这边会报错不知道为什么
//            CharacterDecoder decoder = null;
//            byte[] bytes = decoder.decodeBuffer(base64String);

//            //10.10 解决方案
//            byte[] bytes = Base64.getDecoder().decode(base64String);
//
//            //读取数据的缓冲输入流对象
//            bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
//            //创建到file的输出流
//            fileOutputStream = new FileOutputStream(file);
//            // 为文件输出流对接缓冲输出流对象
//            bufferedOutputStreamb = new BufferedOutputStream(fileOutputStream);
//
//            //读取的内容放在bufferens
//            byte[] buffers = new byte[1024];
//            int len = bufferedInputStream.read(buffers);
//            while (len != -1) {
//                bufferedOutputStreamb.write(buffers, 0, len);
//                len = bufferedInputStream.read(buffers);
//            }
//            // 刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
//            bufferedOutputStreamb.flush();


            //------------------------不使用base64------------
            OutputStream out = new FileOutputStream(filePath);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = input.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bufferedOutputStreamb != null) {
                    bufferedOutputStreamb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    /**
     * 获取pdf文本里面的内容
     *
     * @param url
     */
    @Override
    public String rePdf(String url) {

        String context = null;
        PDDocument document = null;
        try {
            File file = new File(url);
            document = PDDocument.load(file);//会报错？？？？？？？

//            document.loadFromFile("test.pdf");
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
     * 创建模板
     *
     * @param request
     * @return
     */
    //逻辑解释：先获取三种方式创建模板都应该有的东西context，userId，modleTitle，overWrite（只有选择已有模板创作才能选1或0，否则都应该是0）
    //如果是1则说明覆盖模板，那么获取模板id,这时候只需要替换原模板路径的txt文本内容就好
    //否则还应该比对改作者的模板名称是否有和此次想同的，没有则创建成功，否则创建失败，
    @Override
    public Message createModle(HttpServletRequest request) {
        Message message;
        Modle modle = new Modle();
//        ModleService modleService = new ModleServiceImpl();//去掉没用
        //只需要获取文本内容和模板制作者，为该模板起名的标题即可
        String context = request.getParameter("context");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String modleTitle = request.getParameter("modleTitle");

        modle.setUserId(userId);//设置模板作者
        modle.setModleTitle(modleTitle);//设置模板标题

        String overWrite = request.getParameter("overWrite");//覆盖值为1，不覆盖值为0
        if ("1".equals(overWrite)) {
            //此时为覆盖的情况下
            //获取原模板的id
            int modleId = Integer.parseInt(request.getParameter("modleId"));
            modle.setModleId(modleId);//设置模板的id

            //这时候只需要将原模板里面的东西替换成context就行

            replaceContext(context, modleId);

            //结束覆盖过程
            message = new Message("成功覆盖原模板");
            message.addData("modle", modle);//？需不需要返回模板对象
        } else {
            //对比该模板制作者的作于模板标题，不允许有有重复的标题
            int sum = modleDao.selectNumByTitle(modle);
            if (sum > 0) {
//                说明此时已有名字叫xx的模板,此时生成模板失败，因为名称重复
                message = new Message("模板标题不能重复");
            } else {
                //获取此时的总模板个数，然后生成下一条模板
                Integer num = modleDao.selectCount();
                int modleId = num + 1;//为新模板所应该对应的模板id
                modle.setModleId(modleId);
                //将模板内容存为txt文本,返回模板路径，封装在modle对象里
                String modlePath = WriteAsTxt(context, modleId);
                modle.setModlePath(modlePath);

                int result = modleDao.insertModle(modle);
                if (result > 0) {
                    //说明此时插入成功
                    message = new Message("生成新模板成功");
                    message.addData("modle", modle);//？需不需要返回模板对象
                } else {
                    message = new Message("生成新模板失败");
                }

            }
        }
        return message;
    }

    /**
     * 根据获取的模板id,来读取txt文本
     *
     * @param request
     * @return
     */
    @Override
    public Message reTxt(HttpServletRequest request) {
        //这里会出现空指针异常，更改方法里面的尝试

//        Message message;
//        String modleId = request.getParameter("modleId");//获取模板id
////        String fileName = "D:/pdfFile/" + modleId + ".txt";
//        String fileName = Resources.getResource(modleId+" .txt ");
//        File file = new File(fileName);
//
////        StringBuilder result = new StringBuilder();
//        String result = null;
//        String line = null;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file)); //构造一个BufferedReader类来读取文件
//
//            while ((line = br.readLine()) != null) { //使用readLine方法，一次读一行
//                result += line + '\n';
//            }
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //这样读取前面会有null;因此剔除null
//        String context = result.substring(4, result.length());
//        message = new Message("读取模板内容成功");
//        message.addData("modleContext", context);//返回响应数据，模板内容
//        return message;

        Message message;
        String modleId = request.getParameter("modleId");//获取模板id

        String modlePath = Resources.getResource(modleId + ".txt");
        FileReader fileReader = null;
        BufferedReader br=null;
        StringBuilder sb=null;
        String temp = "";


        try {
            File  file = new File(modlePath);
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
            sb = new StringBuilder();
            while ((temp = br.readLine()) != null) {
                // 拼接换行符
                sb.append(temp + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String context = sb.toString();
        System.out.println(context);

        message = new Message("读取模板内容成功");
        message.addData("modleContext", context);//返回响应数据，模板内容
        return message;


    }

    /**
     * 将String类型的字符串存为txt文本，并且返回文件的地址
     *
     * @param context
     * @param modleId
     * @return
     */
    @Override
    public String WriteAsTxt(String context, int modleId) {
        FileOutputStream fileOutputStream = null;
//        String filePath = "D:/pdfFile/" + modleId + ".txt";
//        String filePath = StringUtil.getTempURL(modleId + "");
        String filePath = Resources.getResource(modleId + ".txt");
        try {

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes;
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
        return filePath;
    }

    @Override
    public boolean replaceContext(String context, int modleId) {
        //覆盖成功返回true，失败返回false
        try {
            String modlePath = Resources.getResource(modleId + ".txt");
            PrintWriter printWriter = new PrintWriter(modlePath);
            printWriter.write(context);
            printWriter.flush();
            printWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;

    }
}