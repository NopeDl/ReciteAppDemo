package service.impl;

import dao.LabelDao;
import dao.ModleDao;
import dao.UMRDao;
import dao.impl.LabelDaoImp;
import dao.impl.ModleDaoImpl;
import dao.impl.UMRDaoImpl;
import easydao.utils.Resources;
import handlers.FileHandler;
import handlers.FileHandlerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import pojo.po.Label;
import pojo.po.Modle;
import pojo.po.Umr;
import pojo.vo.Message;
import pojo.vo.ShowModle;
import service.ModleService;

import java.io.*;
import java.util.*;

public class ModleServiceImpl implements ModleService {
    private final ModleDao modleDao = new ModleDaoImpl();

    private final UMRDao umrDao = new UMRDaoImpl();
    private final LabelDao labelDao=new LabelDaoImp();

    private final FileHandlerFactory fileHandlerFactory = new FileHandlerFactory();

    /**
     * 解析文件
     *
     * @param request
     * @return
     */
    @Override
    public Message parseFile(HttpServletRequest request) {
        Message msg;
        try {
            //获取上传的文件
            Part upLoadFile = request.getPart("upLoadFile");
            //获取输入流
            if (upLoadFile != null) {
                String fileType = upLoadFile.getContentType();
                InputStream input = upLoadFile.getInputStream();
                //根据文件类型获得文件处理器
                FileHandler handler = fileHandlerFactory.getHandler(fileType, input);
                String context = handler.parseContent();
                //将换行转换为前端html换行标签
                context = context.replaceAll("\\r\\n", "<\\br>");
                msg = new Message("文件解析成功");
                msg.addData("context", context);
            } else {
                msg = new Message("文件上传失败");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return msg;
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
        //只需要获取文本内容和模板制作者，为该模板起名的标题即可
        String context = request.getParameter("context");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String modleTitle = request.getParameter("modleTitle");
        String modleLabel = request.getParameter("modleLabel");//获取标签


        modle.setUserId(userId);//设置模板作者
        modle.setModleTitle(modleTitle);//设置模板标题
        modle.setModleLabel(Integer.parseInt(modleLabel));//设置模板标签


        String overWrite = request.getParameter("overWrite");//覆盖值为1，不覆盖值为0
        if ("1".equals(overWrite)) {
            //此时为覆盖的情况下
            //获取原模板的id
            int modleId = Integer.parseInt(request.getParameter("modleId"));
            modle.setModleId(modleId);//设置模板的id

            //这时候只需要将原模板里面的东西替换成context就行

            //根据modleId查路径
            boolean b = replaceContext(context, modleId);
            if (b) {
                //结束覆盖过程
                message = new Message("成功覆盖原模板");
                message.addData("modle", modle);//？需不需要返回模板对象
            } else {
                message = new Message("覆盖失败");
            }


        } else {
            //对比该模板制作者的作于模板标题，不允许有有重复的标题
            int sum = modleDao.selectNumByTitle(modle);
            if (sum > 0) {
                //说明此时已有名字叫xx的模板,此时生成模板失败，因为名称重复
                message = new Message("模板标题不能重复 ");
            } else {
                //将模板内容存为txt文本,返回模板路径，封装在modle对象里
                String modlePath = WriteAsTxt(context, modleTitle);
                modle.setModlePath(modlePath);
                //获取标签id
                String lableId = request.getParameter("lableId");
                modle.setModleLabel(Integer.parseInt(lableId));
                //保存进数据库
                int result = modleDao.insertModle(modle);
                //获取modleId
                int modleId = modleDao.selectModleIdByUserIdAndTitle(modle).getModleId();
                //保存um关系
                Umr umr = new Umr();
                umr.setUserId(userId);
                umr.setModleId(modleId);
                umr.setMStatus(0);//自己创建是0，收藏是1
                int i = umrDao.insertUMR(umr);
                //可能存在并发问题，需要事务
                if (result > 0 && i > 0) {
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
        Message message;
        String modleId = request.getParameter("modleId");//获取模板id


        //获得模板,包括模板路径，模板标签，模板标题
        Modle modle = modleDao.selectPathTitlAndTag(Integer.parseInt(modleId));

        FileReader fileReader = null;
        BufferedReader br = null;
        StringBuilder sb = null;
        String temp = "";



        try {
            File file = new File(modle.getModlePath());
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
            sb = new StringBuilder();
            while ((temp = br.readLine()) != null) {
                // 拼接换行符
                sb.append(temp + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String context = sb.toString();
//        System.out.println(context);

        ShowModle showModle=new ShowModle();
        showModle.setContext(context);//存模板内容
        showModle.setTitle(modle.getModleTitle());//存模板标题

        //查找模板的标签名字,并且封装
        showModle.getLabelName(labelDao.selectLableName(modle.getModleLabel()));


        message = new Message("读取模板内容成功");
        message.addData("modleContext", showModle);//返回响应数据，模板内容
        return message;
    }

    /**
     * 将String类型的字符串存为txt文本，并且返回文件的地址
     *
     * @param context
     * @param modleTitle
     * @return
     */
    @Override
    public String WriteAsTxt(String context, String modleTitle) {
        FileOutputStream fileOutputStream = null;
        String filePath = Resources.getResource("static/modles/" + System.currentTimeMillis() + modleTitle + ".txt");
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
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    //修改模板内容,根据传进来的modleId查找modlePath，从而修改文本
    @Override
    public boolean replaceContext(String context, int modleId) {
        String modlePath = modleDao.selectPathByModleId(modleId);
        //覆盖成功返回true，失败返回false
        try {
//            String modlePath = Resources.getResource(modleId + ".txt");
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

    /**
     * 获取标签下所有模板
     *
     * @param request
     * @return
     */
    @Override
    public Message getModlesByTag(HttpServletRequest request) {
        String pageIndexStr = request.getParameter("pageIndex");
        String modleLabelStr = request.getParameter("modleLabel");
        Message msg;
        if (pageIndexStr != null && modleLabelStr != null) {
            //获取分页起始处和模板分类标签
            int pageIndex = Integer.parseInt(pageIndexStr);
            int modleLabel = Integer.parseInt(modleLabelStr);
            //封装查询数据
            Modle modle = new Modle();
            modle.setModleLabel(modleLabel);
            modle.setPageIndex(pageIndex);
            //获得查询信息
            List<Modle> modleList = modleDao.selectModlesByTag(modle);
            //封装响应信息
            msg = new Message("获取成功");
            msg.addData("modleList", modleList);
        } else {
            //没有获取到参数
            msg = new Message("参数获取失败");
        }
        return msg;
    }

    /**
     * '
     * 给模板打赏
     *
     * @param request
     * @return
     */
    @Override
    public Message reward(HttpServletRequest request) {
        String coinsStr = request.getParameter("coins");
        String modleIdStr = request.getParameter("modleId");
        Message msg;
        if (coinsStr != null && modleIdStr != null) {
            int coins = Integer.parseInt(coinsStr);
            int modleId = Integer.parseInt(modleIdStr);
            //封装修改数据
            Modle modle = new Modle();
            modle.setModleId(modleId);
            modle.setCoins(coins);
            //执行修改
            int success = modleDao.updateModleCoins(modle);
            if (success > 0) {
                msg = new Message("打赏成功");
                msg.addData("rewardSuccess", true);
            } else {
                msg = new Message("打赏失败");
                msg.addData("rewardSuccess", false);
            }
        } else {
            msg = new Message("操作失败,参数不能为空");
            msg.addData("rewardSuccess", false);
        }
        return msg;
    }

    @Override
    public Message getUserMemory(HttpServletRequest request) {
        Message message;

        //获取用户的id,从而获取用户的模板
        int userId = Integer.parseInt(request.getParameter("userId"));
        Umr umr = new Umr();
        umr.setUserId(userId);

        List<Umr> umrs = umrDao.selectModleByUserId(umr);
        if (umrs == null) {
            //说明该用户的记忆库啥也没有
            message = new Message();
            message.addData("userMemory", "这里空空如也，快去模板社区进行探索吧！");

        } else {
            //返回有modle的信息和它的状态（已收藏还是未收藏）
            //创建一个hashmap来解决
            ArrayList<Modle> modles = new ArrayList<>();
            int modleIdTemp;//查询用临时变量
            Modle tempMod = new Modle();//查询用临时modle对象
            for (int i = 0; i < umrs.size(); i++) {
                modleIdTemp = umrs.get(i).getModleId();
                tempMod.setModleId(modleIdTemp);
                //封装查询的modle数据
                Modle modle = modleDao.selectModleByModleId(tempMod);
                modle.setMStatus(umrs.get(i).getMStatus());
                modles.add(modle);//放modle和状态
            }
            message = new Message();
            message.addData("userModle", modles);

        }
        return message;
    }

    /**
     * 获取所有标签信息
     *
     * @return
     */
    @Override
    public Message getLabels() {
        List<Label> labels = modleDao.selectLabels();
        Message msg;
        if (labels != null) {
            msg = new Message("获取成功");
            msg.addData("labelList", labels);
        } else {
            msg = new Message("暂无标签");
        }
        return msg;
    }
}