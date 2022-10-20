package pkserver;

import com.alibaba.fastjson.JSONObject;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import enums.SocketMsgInf;
import pojo.po.User;
import pojo.vo.MatchInf;
import pojo.vo.Message;
import pojo.vo.SocketMessage;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.IOException;
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
     * 记录双方答题的对错
     * 里面的list记录对应用户的题目对错状态
     */
    private final Map<PkUser, List<Boolean>> answers = new HashMap<>();

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
        //获取双方匹配信息
        MatchInf p1Inf = player01.getMatchInf();
        MatchInf p2Inf = player02.getMatchInf();
        //初始化双方血量
        hpMap.put(player01, 100.0);
        hpMap.put(player02, 100.0);
        //获取双方文章字数
        int p1ModleNum = p1Inf.getModleNum();
        int p2ModleNum = p2Inf.getModleNum();
        //获取双方挖空数
        //获取难度，因为匹配双方的难度都是一样的所以只用获取p1的就可以
        double ratio = p1Inf.getDifficulty().getRatio();
        int p1BlankNum = StringUtil.getBlankNumByContentLength(p1ModleNum, ratio);
        int p2BlankNum = StringUtil.getBlankNumByContentLength(p2ModleNum, ratio);
        //获取两人挖空数的最小值
        //保存该房间挖空数
        this.blankNum = Math.min(p1BlankNum, p2BlankNum);
        //根据难度和挖空数记录总时长
        //各个难度所对应每个空的时间不一样，先写死后续再优化！！！！！！
        //10s(easy),15s(normal),20s(hard)
        //根据难度获取每个空需要的时间
        int blankTimeLimits = p1Inf.getDifficulty().getTimeLimits();
        //设置该房间总时间限制
        this.timeLimits = (long) this.blankNum * blankTimeLimits;
    }

    /**
     * 开始本房间比赛
     */
    public void excute(String json, PkUser curUser) {
        //获取json数据
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject != null) {
            //处理Json数据
            //获取答案对错
            boolean isRight = (Boolean) jsonObject.get("answer");
            //将答案对错存在对应表中
            savePlayerAnswer(curUser, isRight);
            //如果是对的则需要扣对手的血量
            if (isRight) {
                PkUser enemy = getEnemy(curUser);
                //获取敌人当前血量
                double hp = getHp(enemy);
                //计算扣的血
                hp -= (1.0 / this.blankNum) * 100;
                //设置血量
                setHp(curUser,hp);
            }
            //检查双方输赢状态


        } else {
            this.responseMessage = new SocketMessage(SocketMsgInf.JSON_ERROR);
        }
    }



    /**
     * 结束本房间游戏
     */
    public void end() {
        //将输赢信息封装
        SocketMessage result = getWinner();
        roomBroadcast(result);
       //结束游戏,并关闭房间
        try {
            player01.getSession().close();
            player02.getSession().close();
            StatusPool.PK_ROOM_LIST.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void roomBroadcast(SocketMessage msg){
        //给房间内两位玩家发送结果和战绩
        try {
            ResponseUtil.send(player01.getSession(),msg);
            ResponseUtil.send(player02.getSession(),msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取赢家
     */
    private SocketMessage getWinner(){
        double player01Hp = getHp(player01);
        double player02Hp = getHp(player02);
        MatchInf winnerInf;
        if (player01Hp < player01Hp){
            //玩家2赢
            winnerInf = player02.getMatchInf();
        } else if (player02Hp < player01Hp) {
            //玩家1赢
            winnerInf = player01.getMatchInf();
        }else {
            //平局
            winnerInf = null;
        }

        SocketMessage msg = new SocketMessage();
        //封装赢者数据
        msg.addData("winner", winnerInf);
        //封装双方战绩
        List<Boolean> player01Records = answers.get(player01);
        List<Boolean> player02Records = answers.get(player02);
        Map<User,List<Boolean>> records = new HashMap<>();
        UserDao userDao = new UserDaoImpl();
        //查询双方信息
        User user01 = userDao.selectUserById(player01.getMatchInf().getUserId());
        User user02 = userDao.selectUserById(player02.getMatchInf().getUserId());
        records.put(user01,player01Records);
        records.put(user02,player02Records);
        msg.addData("record", records);
        return msg;
    }

    /**
     * 判断该房间内是否存在该玩家
     *
     * @param player 玩家
     * @return bool值
     */
    private boolean containPlayer(PkUser player) {
        return player01.equals(player) || player02.equals(player);
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
     * 保存用户答案
     *
     * @param player 用户
     * @param b      题目对错
     */
    private void savePlayerAnswer(PkUser player, boolean b) {
        this.answers.get(player).add(b);
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
            //先获取对手id
            int enemyId = StatusPool.PK.get(curPlayer.getMatchInf().getUserId());
            //根据对手ID获取对手对象
            PkUser enemy = null;
            Map<MatchInf, PkUser> matchedPool = StatusPool.MATCHED_POOL;
            for (MatchInf enemyInf : matchedPool.keySet()) {
                if (enemyInf.getUserId() == enemyId) {
                    enemy = matchedPool.get(enemyInf);
                    break;
                }
            }
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

    public void setPlayer01(PkUser player01) {
        this.player01 = player01;
    }

    public PkUser getPlayer02() {
        return player02;
    }

    public void setPlayer02(PkUser player02) {
        this.player02 = player02;
    }

    public int getBlankNum() {
        return blankNum;
    }

    public void setBlankNum(int blankNum) {
        this.blankNum = blankNum;
    }

    public long getTimeLimits() {
        return timeLimits;
    }

    public void setTimeLimits(long timeLimits) {
        this.timeLimits = timeLimits;
    }

    public Map<PkUser, Double> getHpMap() {
        return hpMap;
    }

    public SocketMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(SocketMessage responseMessage) {
        this.responseMessage = responseMessage;
    }
}
