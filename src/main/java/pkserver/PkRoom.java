package pkserver;

import com.alibaba.fastjson.JSONObject;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import enums.Difficulty;
import enums.SocketMsgInf;
import pkserver.threads.TimeLimitThread;
import pojo.po.db.User;
import pojo.po.pk.AnswerStatus;
import pojo.po.pk.AnswersRecord;
import pojo.po.pk.UserHp;
import pojo.vo.MatchInf;
import pojo.vo.SocketMessage;
import tools.easydao.utils.Resources;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Pk房间
 * 房间用于处理比赛信息
 * 双方血量的记录
 * 胜负的判断
 *
 * @author yeyeye
 * @Date 2022/10/20 15:07
 */
public class PkRoom {
    /**
     * 延迟秒数
     */
    private static final int DELAY = 4;

    private int playerNum;
    /**
     * 玩家一
     */
    private PkUser player01;
    /**
     * 玩家二
     */
    private PkUser player02;

    /**
     * 两个玩家的血条
     */
    private final Map<PkUser, Double> hpMap = new HashMap<>();

    /**
     * 记录双方答题结果
     */
    private final Map<PkUser, AnswersRecord> answersRecords = new HashMap<>();
    /**
     * 挖空数
     */
    private int blankNum;

    /**
     * 时长限制
     */
    private long timeLimits;

    /**
     * 房间对外响应消息封装
     */
    private SocketMessage responseMessage;

    private final Thread timer = new Thread(new TimeLimitThread(this));

    public void startTimer() {
        this.timer.start();
    }

    public boolean isTimerAlive() {
        return timer.isAlive();
    }


    private PkRoom() {

    }

    /**
     * 开房间
     *
     * @param player01 玩家一
     * @param player02 玩家二
     */
    public PkRoom(PkUser player01, PkUser player02) {

        this.player01 = player01;
        this.player02 = player02;

        playerNum = 2;
        //获取双方匹配信息
        MatchInf p1Inf = player01.getMatchInf();
        MatchInf p2Inf = player02.getMatchInf();
        //初始化双方血量
        hpMap.put(player01, 100.0);
        hpMap.put(player02, 100.0);
        //获取双方文章字数
        int p1ModleNum = p1Inf.getModleNum();
        System.out.println("p1模板字数：" + p1ModleNum);
        int p2ModleNum = p2Inf.getModleNum();
        System.out.println("p2模板字数：" + p2ModleNum);

        //获取双方挖空数
        //获取难度，因为匹配双方的难度都是一样的所以只用获取p1的就可以
        double ratio = p1Inf.getDifficulty().getRatio();
        int p1BlankNum = StringUtil.getBlankNumByContentLength(p1ModleNum, ratio);
        int p2BlankNum = StringUtil.getBlankNumByContentLength(p2ModleNum, ratio);
        //获取两人挖空数的最小值
        //保存该房间挖空数
        this.blankNum = Math.min(p1BlankNum, p2BlankNum);
        if (this.blankNum <= 0) {
            blankNum = 1;
        }
        System.out.println("房间：" + this + " 挖空数为：" + this.blankNum);
//        // <div>
//        String matchStr = "</div>";
//        String content = p1Inf.getContent();
//        this.blankNum = StringUtil.subStrCount(content, matchStr);

        //根据难度和挖空数记录总时长
        //各个难度所对应每个空的时间不一样，先写死后续再优化！！！！！！
        //10s(easy),15s(normal),20s(hard)
        //根据难度获取每个空需要的时间
        int blankTimeLimits = p1Inf.getDifficulty().getTimeLimits();
        //设置该房间总时间限制
        this.timeLimits = (long) this.blankNum * blankTimeLimits + DELAY;
        //初始化answers集合
        //初始化玩家1战绩集合
        AnswersRecord answersRecord01 = new AnswersRecord();
        //设置ID
        answersRecord01.setUserId(getPlayer01().getMatchInf().getUserId());

        //初始化玩家2战绩集合
        AnswersRecord answersRecord02 = new AnswersRecord();
        //设置ID
        answersRecord02.setUserId(getPlayer02().getMatchInf().getUserId());
        //添加进总记录集合
        answersRecords.put(this.player01, answersRecord01);
        answersRecords.put(this.player02, answersRecord02);
        //输出日志
        System.out.println(p1Inf.getUserId() + " and " + p2Inf.getUserId() + "create pk room");
    }


    /**
     * 开始本房间比赛
     */
    public synchronized void excute(String json, PkUser curUser) {
        System.out.println(this + " pk room excute: " + json);
        //获取json数据
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject != null) {
            //处理Json数据
            //获取答案对错
            //{"answerName":"1111","answerValue":true}
            String answerName = (String) jsonObject.get("answerName");
            boolean isRight = (Boolean) jsonObject.get("answerValue");
            //封装答案信息
            AnswerStatus answerStatus = new AnswerStatus(answerName, isRight);
            //保存
            saveRecord(curUser, answerStatus);
            //如果是对的则需要扣对手的血量
            if (isRight) {
                PkUser enemy = getEnemy(curUser);
                //获取敌人当前血量
                double hp = getHp(enemy);
                //计算扣的血
                hp -= Math.round(100.0 / this.blankNum);
                //设置血量
                if (hp < 0) {
                    hp = 0;
                }
                setHp(enemy, hp);
            }
            //检查双方输赢状态
            boolean isContinue = true;
            if (getHp(curUser) <= 0 || getHp(getEnemy(curUser)) <= 0) {
                //有一边没血了
                //结束比赛
                isContinue = false;
            }
            //将双方血量响应回去
            SocketMessage msg = new SocketMessage();
            List<UserHp> hpLists = new ArrayList<>();
            hpLists.add(new UserHp(this.player01.getMatchInf().getToken(), getHp(this.player01)));
            hpLists.add(new UserHp(this.player02.getMatchInf().getToken(), getHp(this.player02)));
            msg.addData("hpInf", hpLists);
            roomBroadcast(msg);
            if (!isContinue) {
                //比赛结束
                this.end();
            }
        } else {
            this.responseMessage = new SocketMessage(SocketMsgInf.JSON_ERROR);
        }
    }

    /**
     * 重复挖空
     *
     * @param player 需要重复挖空的用户
     * @return 挖好的内容
     */
    public synchronized SocketMessage againDig(PkUser player) {
        SocketMessage msg;
        if (player == player01 || player == player02) {
            //验证该用户是否合法
            //为需要循环挖空的玩家挖空
            String content = player.getMatchInf().getContent();
            String handledContent = StringUtil.digBlank(content, this.blankNum);
            msg = new SocketMessage(SocketMsgInf.OPERATE_SUCCESS);
            msg.addData("digedContent", handledContent);
        } else {
            //用户不存在该房间内
            //非法请求
            msg = new SocketMessage(SocketMsgInf.SERVER_ERROR);
        }
        return msg;
    }


    /**
     * 结束本房间游戏
     */
    public synchronized void end() {

        System.out.println(this + " pk room closed ");
        //将输赢信息封装
        roomBroadcast(new SocketMessage(SocketMsgInf.MATCH_END));
        SocketMessage result = getWinner();
        roomBroadcast(result);
        //结束游戏,并关闭房间
        try {
            player01.getSession().close();
            player02.getSession().close();
            //移除池中相关信息
            StatusPool.PK_ROOM_LIST.remove(this);
            StatusPool.MATCHED_POOL.remove(player01);
            StatusPool.MATCHED_POOL.remove(player02);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户答案记录
     *
     * @param player       用户
     * @param answerStatus 封装的答案信息
     */
    private void saveRecord(PkUser player, AnswerStatus answerStatus) {
        answersRecords.get(player).getAnswersRecord().add(answerStatus);
    }


    /**
     * 房间广播
     *
     * @param msg msg
     */
    private void roomBroadcast(SocketMessage msg) {
        //给房间内两位玩家发送结果
        try {
            if (player01.getSession().isOpen()) {
                ResponseUtil.send(player01.getSession(), msg);
            }
            if (player02.getSession().isOpen()) {
                ResponseUtil.send(player02.getSession(), msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取赢家
     */
    private SocketMessage getWinner() {
        double player01Hp = getHp(player01);
        double player02Hp = getHp(player02);
        int winnerId;
        if (player01Hp < player02Hp) {
            //玩家2赢
            winnerId = player02.getMatchInf().getUserId();
            updateRank(winnerId, true);
            updateRank(player01.getMatchInf().getUserId(), false);
        } else if (player02Hp < player01Hp) {
            //玩家1赢
            winnerId = player01.getMatchInf().getUserId();
            updateRank(winnerId, true);
            updateRank(player02.getMatchInf().getUserId(), false);
        } else {
            //平局
            winnerId = -1;
        }
        SocketMessage msg = new SocketMessage();
        //封装赢者数据
        msg.addData("winnerId", winnerId);
        //封装双方战绩
        List<AnswersRecord> answersRecordList = new ArrayList<>();
        answersRecordList.add(answersRecords.get(player01));
        answersRecordList.add(answersRecords.get(player02));
        msg.addData("records", answersRecordList);
        return msg;
    }

    /**
     * 获取段位信息
     * 只会读取所以使用普通HashMap即可
     */
    private static final Map<String, Integer> rankInfos = new HashMap<>();

    static {
        InputStream input = Resources.getResourceAsStream("rankInfos.properties");
        try {
            Properties properties = new Properties();
            properties.load(input);
            String stars = (String) properties.get("stars");
            String easyPoint = (String) properties.get("easyPoint");
            String normalPoint = (String) properties.get("normalPoint");
            String difficultPoint = (String) properties.get("difficultPoint");
            String maxPoints = (String) properties.get("maxPoints");
            rankInfos.put("stars", Integer.parseInt(stars));
            rankInfos.put("easyPoint", Integer.parseInt(easyPoint));
            rankInfos.put("normalPoint", Integer.parseInt(normalPoint));
            rankInfos.put("difficultPoint", Integer.parseInt(difficultPoint));
            rankInfos.put("maxPoints", Integer.parseInt(maxPoints));
        } catch (IOException e) {
            throw new RuntimeException("读取段位配置信息失败");
        }
    }

    private final UserDao userDao = new UserDaoImpl();

    /**
     * 更新段位
     */
    private void updateRank(int userId, boolean isWin) {
        User user = userDao.selectUserById(userId);
        int userStars = user.getStars();
        Integer stars = rankInfos.get("stars");

        //首先处理积分
        int userPoints = user.getPoints();
        Difficulty difficulty = this.player01.getMatchInf().getDifficulty();
        //根据难度获取不同的积分加成
        Integer points;
        if (difficulty == Difficulty.EASY) {
            points = rankInfos.get("easyPoint");
        } else if (difficulty == Difficulty.NORMAL) {
            points = rankInfos.get("normalPoint");
        } else {
            points = rankInfos.get("difficultPoint");
        }
        //计算总积分积分
        int totalPoints = userPoints + points;
        //计算根据积分需要额外增加的星星数量
        Integer maxPoints = rankInfos.get("maxPoints");
        int extraStars = totalPoints / maxPoints;
        totalPoints -= extraStars * maxPoints;
        //计算当前可用星星数量
        userStars += extraStars;
        int totalStars = 0;
        if (isWin) {
            //成功了
            //增加星星数量和积分数量
            //星星数量 = 初始星星数 + 获胜获得星星数 + 积分额外星星数
            totalStars = userStars + stars;
        } else if (userStars - stars >= 0) {
            //失败了扣星星
            //如果还有星星可以扣
            totalStars = userStars - stars;
        } else {
            System.out.println("没有星星扣了");
        }
        //更新用户星星和积分
        int i = userDao.updateStarsByUserId(userId, totalStars);
        i = userDao.updatePointByUserId(userId, totalPoints);
        if (i > 0) {
            System.out.println("更新 " + userId + " 星星和积分数量成功");
        } else {
            System.out.println("更新 " + userId + " 星星和积分数量失败");
        }
    }

    /**
     * 判断该房间内是否存在该玩家
     *
     * @param player 玩家
     * @return bool值
     */
    private boolean containPlayer(PkUser player) {
        return player01 == player || player02 == player;
    }

    /**
     * 获取当前玩家的对手
     *
     * @param player 当前玩家
     * @return 对手
     */
    private PkUser getEnemy(PkUser player) {
        if (player.equals(player01)) {
            return player02;
        } else {
            return player01;
        }
    }

    private double getHp(PkUser player) {
        return hpMap.get(player);
    }

    /**
     * 设置用户血量
     *
     * @param player 用户
     */
    private void setHp(PkUser player, double hp) {
        this.hpMap.put(player, hp);
    }


    /**
     * 加入房间
     *
     * @param curPlayer 用户
     */
    public synchronized static void joinRoom(PkUser curPlayer) {
        //遍历房间列表房间里是否存在当前用户或他的对手
        List<PkRoom> pkRoomList = StatusPool.PK_ROOM_LIST;
        boolean findSuccess = false;
        for (PkRoom pkRoom : pkRoomList) {
            if (pkRoom.containPlayer(curPlayer)) {
                //找到玩家所在房间
                findSuccess = true;
                break;
            }
        }
        if (!findSuccess) {
            //没找到则创建房间，并把他们两个都扔进去
            //获取对手对象
            Map<PkUser, PkUser> matchedPool = StatusPool.MATCHED_POOL;
            PkUser enemy = matchedPool.get(curPlayer);
            if (enemy != null) {
                //将自己和对手创建房间并添加进房间列表中
                PkRoom pkRoom = new PkRoom(curPlayer, enemy);
                pkRoomList.add(pkRoom);
                enemy.setPkRoom(pkRoom);
                curPlayer.setPkRoom(pkRoom);
            } else {
                throw new RuntimeException("创建房间失败");
            }
        }
    }


    public PkUser getPlayer01() {
        return player01;
    }

    public PkUser getPlayer02() {
        return player02;
    }


    public int getBlankNum() {
        return blankNum;
    }


    public long getTimeLimits() {
        return timeLimits;
    }


    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
}
