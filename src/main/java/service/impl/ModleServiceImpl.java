package service.impl;

import dao.LabelDao;
import dao.ModleDao;
import dao.UMRDao;
import dao.UserDao;
import dao.impl.LabelDaoImp;
import dao.impl.ModleDaoImpl;
import dao.impl.UMRDaoImpl;
import enums.Difficulty;
import dao.impl.UserDaoImpl;
import pojo.po.db.User;
import pojo.vo.Community;
import tools.easydao.utils.Resources;
import enums.MsgInf;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import pojo.po.db.Label;
import pojo.po.db.Modle;
import pojo.po.db.Umr;
import pojo.vo.Message;
import pojo.vo.ShowModle;
import service.ModleService;
import tools.utils.StringUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ModleServiceImpl implements ModleService {
    private final ModleDao modleDao = new ModleDaoImpl();

    private final UMRDao umrDao = new UMRDaoImpl();
    private final LabelDao labelDao=new LabelDaoImp();
    private final UserDao userDao=new UserDaoImpl();

//    private final FileHandlerFactory fileHandlerFactory = new FileHandlerFactory();

//
//    /**
//     * 取消用户收藏的模板
//     * @return
//     */
//    @Override
//    public Message cancelModleCollect(HttpServletRequest request) {
//        Message message;
//        int userId = Integer.parseInt(request.getParameter("userId"));
//        int modleId = Integer.parseInt(request.getParameter("modleId"));
////        int mStatus=Integer.parseInt(request.getParameter("mStatus"));
//        int mStatus = Integer.parseInt(request.getParameter("mStatus"));
//
//        if(1==mStatus){
//            message = new Message("取消失败");
//        }
//        else {
//            int i = modleDao.collectModleById(userId, modleId, mStatus);
//            if (i > 0) {
//                //说明此时成功
//                message = new Message("取消收藏成功");
//            } else {
//                message = new Message("取消失败");
//            }
//        }
//        return message;
//
//    }

    //好困好困好困好困好困好困好困好困好困好困
    @Override
    public Message collectModle(HttpServletRequest request) {
        Message message;
        //用户收藏非自己的模板
        int userId = Integer.parseInt(request.getParameter("userId"));
        int modleId = Integer.parseInt(request.getParameter("modleId"));
        int mStatus = Integer.parseInt(request.getParameter("mStatus"));
        Umr umr=new Umr();
        umr.setUserId(userId);
        umr.setModleId(modleId);
        umr.setMStatus(mStatus);

        //先去modle表里找用户收藏的是不是自己的模板
        int integer1 = modleDao.selectIfContain(umr);
        if(integer1>0){
            //说明是用户自己的模板,用户不能操作自己的模板
            message=new Message("该操作违法！");
            return message;
        }else {


            //先查看下该模板是否已被该用户所收藏了
            //是为umr有该条表，说明此时已经收藏成功
//        boolean b = umrDao.slelectIfCollect(umr);
            int integer = umrDao.slelectIfCollect(umr);

            //大于0为有这条记录
            System.out.println(integer);
            //第一种，用户自己收藏过
            if (integer > 0) {
                //umr 表中已经有对应的数据，说明此时已经是收藏了的
                if (mStatus == 1) {
                    //收藏了又收藏，报错
                    message = new Message("收藏失败，该模板已被收藏");
                } else {
                    //此时如果不收藏，那就是取消收藏
                    int i = umrDao.deleteUMRByModleId(umr);
                    if (i > 0) {
                        //取消收藏成功
                        message = new Message("取消收藏成功");
                    } else {
                        message = new Message("取消收藏失败");
                    }
                }

                //未收藏过
            } else {
                //用户想要收藏
                if (mStatus == 1) {
                    System.out.println("我来啦");
                    //说明此时用户想要收藏
                    int i = umrDao.insertUMR(umr);
                    if (i > 0) {
                        //说明收藏成功
                        message = new Message("收藏成功");
                    } else {
                        message = new Message("收藏失败");
                    }
                } else {
                    //umr表都没这个收藏，（用户又取消收藏，这时候要报错）
                    message = new Message("例表里没有这个收藏呢，请收藏后再进行操作");
                }
            }
        }

//        //调用modleDao来将收藏的东西insert
//        int i = modleDao.collectModleById(userId, modleId,mStatus);
//        if(i>0){
//            //成功插入
//            message=new Message("收藏成功");
//        }else{
//            message=new Message("收藏失败");
//        }
//        return message;
        return message;
    }



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
                String fileType = upLoadFile.getSubmittedFileName();
                InputStream input = upLoadFile.getInputStream();
                //根据文件类型获得文件处理器
                fileType = fileType.substring(fileType.indexOf(".") + 1);
                FileHandler handler = FileHandlerFactory.getHandler(fileType, input);
                String context = handler.parseContent();
                System.out.println(context);
                //将换行转换为前端html换行标签
                context = context.replaceAll("\\r\\n", "<\\br>");

                msg = new Message("文件解析成功");
                msg.addData("context", context);
            } else {
                msg = new Message("文件上传失败");
            }

        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }


    /**
     * 创建模板
     * <p>
     * 逻辑解释：先获取三种方式创建模板都应该有的东西context，userId，modleTitle，overWrite（只有选择已有模板创作才能选1或0，否则都应该是0）
     * 如果是1则说明覆盖模板，那么获取模板id,这时候只需要替换原模板路径的txt文本内容就好
     * 否则还应该比对改作者的模板名称是否有和此次想同的，没有则创建成功，否则创建失败，
     *
     * @param request 请求
     * @return 响应数据封装
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
        //获取标签id
        String modleLabel = request.getParameter("modleLabel");

        //设置模板作者
        modle.setUserId(userId);
        //设置模板标题
        modle.setModleTitle(modleTitle);
        //设置模板标签
        modle.setModleLabel(Integer.parseInt(modleLabel));

        //先看标题有没有重复的
        //覆盖值为1，不覆盖值为0
        String overWrite = request.getParameter("overWrite");
        if ("1".equals(overWrite)) {
            //此时为覆盖的情况下
            //获取原模板的id
            int modleId = Integer.parseInt(request.getParameter("modleId"));
            modle.setModleId(modleId);//设置模板的id

            //这时候只需要将原模板里面的东西替换成context就行
            //标题分为两种情况，一种是改了名字的，一种是没改的
            //查找要覆盖的模板的标题
            String s = modleDao.selectTitleByModleId(modle);
            if(modleTitle.equals(s)){
                //说明此时没有改名字
                modle.setModlePath(modleLabel);
            }else {

                int sum = modleDao.selectNumByTitle(modle);

                if (sum > 0) {
//                    说明此时已有名字叫xx的模板,此时生成模板失败，因为名称重复
                    message = new Message("模板标题不能重复 ");
                }

            }
            //根据modleId查路径
            boolean b = replaceContext(context, modleId);
            //这个时候得更新模板标签和标题
            //更改模板的标签

            boolean b1 = modleDao.changeModleTag(modle);
            if (b&&b1) {

                //结束覆盖过程
                message = new Message("成功覆盖原模板");
                modle.setModlePath(null);
                //？需不需要返回模板对象
                message.addData("modle", modle);
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
                modle.setModleLabel(Integer.parseInt(modleLabel));
                //保存进数据库
                int result = modleDao.insertModle(modle);
                //获取modleId
                int modleId = modleDao.selectModleIdByUserIdAndTitle(modle).getModleId();
                modle.setModleId(modleId);
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
     * 删除模板
     * @param request
     * @return
     */
    @Override
    public Message deleteModle(HttpServletRequest request) {
        int modleId = Integer.parseInt(request.getParameter("modleId"));
        String path = modleDao.selectPathByModleId(modleId);

        Umr umr=new Umr();
        umr.setModleId(modleId);
//        int deleteUmr = umrDao.deleteUMRByModleId(umr);
//        //查看在umr表中是否删除成功
//        System.out.println("deleteUmr:"+deleteUmr);

        int deleteModle = modleDao.deleteModle(modleId);

        File file = new File(path);
        boolean deleteFile = file.delete();
        //没有用事务,可能会有bug
        Message msg;
//        if (deleteUmr != 0 && 0 != deleteModle && deleteFile) {
        if (0 != deleteModle && deleteFile) {
            msg = new Message("删除成功");
            msg.addData("deleteSuccess", true);
        } else {
            msg = new Message("删除失败");
            msg.addData("deleteSuccess", false);
        }
        return msg;
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
        //获取模板路径
        String modlePath = modleDao.selectPathByModleId(Integer.parseInt(modleId));
        Modle modle = modleDao.selectPathTitlAndTag(Integer.parseInt(modleId));

        try {
            //获取文件位置
            InputStream input = new FileInputStream(modlePath);
            //获取模板文件处理器
            FileHandler txtHandler = FileHandlerFactory.getHandler("txt", input);
            //解析文件内容
            String context = txtHandler.parseContent();
            ShowModle showModle = new ShowModle();
            showModle.setContext(context);//存模板内容
            showModle.setTitle(modle.getModleTitle());//存模板标题

            //查找模板的标签名字,并且封装
            int modleLabel = modle.getModleLabel();
            showModle.setLabelValue(modleLabel);//将模板标签编号存进去

            //将模板标签名字存进去
            String lableName = labelDao.selectLableName(modleLabel);
            showModle.setLabelName(lableName);

            message = new Message("读取模板内容成功");
            message.addData("modleContext", showModle);//返回响应数据，模板内容
        } catch (FileNotFoundException e) {
            message = new Message(MsgInf.SERVER_ERROR);
        }
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
        String filePath = Resources.getResource("static/modles/" + System.currentTimeMillis() + modleTitle + ".txt");
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileHandler txtHandler = FileHandlerFactory.getHandler("txt", null);
            String path = txtHandler.saveFile(filePath, context);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改模板内容,根据传进来的modleId查找modlePath，从而修改文本
     * @param context 内容
     * @param modleId 模板ID
     * @return 返回
     */
    @Override
    public boolean replaceContext(String context, int modleId) {
        String modlePath = modleDao.selectPathByModleId(modleId);
        //覆盖成功返回true，失败返回false
        try {
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
     * @param request 请求
     * @return 返回
     */
    @Override
    public Message getModlesByTag(HttpServletRequest request) {
        String pageIndexStr = request.getParameter("pageIndex");
        String modleLabelStr = request.getParameter("modleLabel");
        Message msg=null;
        if (pageIndexStr != null && modleLabelStr != null) {
            //获取分页起始处和模板分类标签
            int pageIndex = Integer.parseInt(pageIndexStr);
            int modleLabel = Integer.parseInt(modleLabelStr);
            //封装查询数据
            Modle modle = new Modle();
            modle.setModleLabel(modleLabel);
            modle.setPageIndex(pageIndex);

            //获得查询信息

            //返回一个Community类型（包含modle里面的 所有属性）
            List<Community> modleList = modleDao.selectModlesByTag(modle);
            InputStream input;
            if (modleList.size() > 0) {
                for (int i = 0; i < modleList.size(); i++) {
                    //根据路径读取文件内容
                    //获取改模板的路径；根据路径读取文件内容
                    String modlePath = modleList.get(i).getModlePath();
                    try {
                        input = new FileInputStream(modlePath);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    //读取文本
                    FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
                    String content = txtFileHandler.parseContent();
                    modleList.get(i).setContent(content);
                    modleList.get(i).setModlePath("");

                    User user = userDao.selectNameImgById(modleList.get(i));
                    if(user!=null){
                        //传进来昵称和头像
                        modleList.get(i).setNickName(user.getNickName());
                        //获取用户头像的显示地址
                        String imagepath = user.getImage();

                        //从我开始
                        if ("".equals(imagepath)) {
                            //说明此时头像为默认头像，不需要重新读取
                            //将响应的数据封装到message里
                            user.setBase64("");
                        } else {
//                            //说明头像已经改变过了，需要重新读取
                            try {
//                                inputStream = new FileInputStream(imagepath);
                                input = Files.newInputStream(Paths.get(imagepath));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //读取文本,这里表现为读取头像的base64路径
                            FileHandler imgHandler = FileHandlerFactory.getHandler("img",input);
                            String base64 = imgHandler.parseContent();
                            modleList.get(i).setBase64(base64);
                        }

                    }




//            个Community类型（包含modle里面的 所有属性）
//            List<Community> modleList = modleDao.selectModlesByTag(modle);
//            if (modleList.size() > 0) {
//                for (int i = 0; i < modleList.size(); i++) {
//                    //根据路径读取文件内容
//                    String modlePath = modleList.get(i).getModlePath();//获取改模板的路径；根据路径读取文件内容
//                    InputStream input;
//                    try {
//                        input = new FileInputStream(modlePath);
//
//                    //读取文本
//                    FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
//                    String content = txtFileHandler.parseContent();
////                    modleList.get(i).setContent(content);
//                    modleList.get(i).setContent(content);
//                    modleList.get(i).setModlePath("");
//
//                    User user = userDao.selectNameImgById(modleList.get(i));
//                    if(user!=null){
//                        //传进来昵称和头像
//                        modleList.get(i).setNickName(user.getNickName());
////                        modleList.get(i).setImg(user.getImage());
//                        String imagepath = user.getImage();//获取用户头像的显示地址\
//
//                        //从我开始
//                        if ("".equals(user.getImage())) {
//                            //说明此时头像为默认头像，不需要重新读取
//                            //将响应的数据封装到message里
//                            user.setBase64("");
//                        } else {
//                            //说明头像已经改变过了，需要重新读取
//                            String imagePath = user.getImage();//头像的存放路径
//                            InputStream inputStream;
//
//                                inputStream = new FileInputStream(imagePath);
//                                inputStream.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            //读取文本,这里表现为读取头像的base64路径
//                            FileHandler imgHandler = FileHandlerFactory.getHandler("img",input);
//                            String base64 = imgHandler.parseContent();
//                            modleList.get(i).setImg(base64);
//                        }
//
//                    }


            //从我结束
                    //不回显给前端路径
                    modleList.get(i).setModlePath(null);
                }


//                System.out.println(modleList.get(0));
                //封装响应信息
                msg = new Message("获取成功");
                msg.addData("modleList", modleList);
//
            } else {
                //没有获取到参数
                msg = new Message("参数获取失败");
            }
        }
        return msg;
    }

    /**
     *
     * 给模板打赏
     *
     * @param request 请求
     * @return 响应
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
            //查询用临时变量
            int modleIdTemp;
            //查询用临时modle对象
            Modle tempMod = new Modle();
            for (Umr value : umrs) {
                modleIdTemp = value.getModleId();
                tempMod.setModleId(modleIdTemp);
                //封装查询的modle数据
                Modle modle = modleDao.selectModleByModleId(modleIdTemp);
                modle.setMStatus(value.getMStatus());
                //获取模板内容
                //下面内容考虑下封装
                String modlePath = modle.getModlePath();
                InputStream input;
                try {
                    input = new FileInputStream(modlePath);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //读取文本
                FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
                String content = txtFileHandler.parseContent();
                modle.setContent(content);
                modle.setModlePath(null);
                //放modle和状态
                modles.add(modle);
            }
            message = new Message();
            message.addData("userModle", modles);

        }
        return message;
    }


    /**
     * 获取所有标签信息
     *
     * @return 所有标签信息
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


    /**
     * 系统自动挖空
     *
     * @param request
     * @return
     */
    @Override
    public Message autoDig(HttpServletRequest request) {
        String difficultyStr = request.getParameter("difficulty");
        String modleIdStr = request.getParameter("modleId");
        Message msg;
        if (difficultyStr != null && modleIdStr != null) {
            //获取挖空比例
            Difficulty difficulty = Difficulty.getRatio(difficultyStr);
            //获取模板ID
            int modleId = Integer.parseInt(modleIdStr);
            //挖空好的内容
            String content = StringUtil.autoDig(modleId, difficulty);

            msg = new Message("挖空成功");
            msg.addData("content",content);
        } else {
            msg = new Message("难度或模板id不能为空");
        }
        return msg;
    }


    /**
     * 计算每个空需要多少字
     *
     * @param totalChar
     * @param count
     * @return
     */
    private ArrayList<Integer> getCharNums(int totalChar, int count) {
        ArrayList<Integer> list = new ArrayList<>();
        int min = count - 1;
        Random random = new Random();
        for (int i = 0; i < count - 1; i++) {
            int num = random.nextInt(totalChar - min) + 1;
            list.add(num);
            totalChar -= num;
            min--;
        }
        list.add(totalChar);
        return list;
    }

    /**
     * 上传至模板社区
     *
     * @param request 请求
     * @return 响应数据封装
     */
    @Override
    public Message toCommunity(HttpServletRequest request) {
        Message msg;
        ;
        int modleId = Integer.parseInt(request.getParameter("modleId"));
        int common = Integer.parseInt(request.getParameter("common"));
        if (common == 1) {
            //说明用户想要上传模板
            int success = modleDao.updateModleCommon(modleId, common);
            if (success > 0) {
                msg = new Message("发布成功");
                msg.addData("isPublic", true);
            } else {
                msg = new Message("发布失败");
                msg.addData("isPublic", false);
            }
        } else {
            //说明用户想要下架模板
            int success = modleDao.updateModleCommon(modleId, common);
            if (success > 0) {
                msg = new Message("删除成功");
                msg.addData("isPublic", false);
            } else {
                msg = new Message("删除失败");
                msg.addData("isPublic", true);
            }
        }

        return msg;
    }

}