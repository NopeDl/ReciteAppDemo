package PKServer;

import pojo.vo.MatchInf;

public class UserThread implements Runnable{
    private static final StatusPool STATUS_POOL = new StatusPool();
   private  MatchInf matchInf=null;

    public UserThread(MatchInf matchInf) {
        this.matchInf=matchInf;
    }

    @Override
    public void run() {
        //在此进行循环遍历map，用来匹配对手
        // 打印值集合
        Boolean flag=true;
        while(flag) {
            if (STATUS_POOL.MATCHING_POOL.containsKey(matchInf)) {//判断以下用户是否还在池子里
                for (MatchInf key : STATUS_POOL.MATCHING_POOL.keySet()) {
                    double result = (double) key.getModleNum() / matchInf.getModleNum();//计算两者的关系
                    //当字数在范围内且选择难度一样的时候，是俩个人成功匹配的前提
                    if ((result > 0.5 || (result > 0 && result < 2))
                            && (key.getDifficulty() == matchInf.getDifficulty())) {
                        //说明两个人的的能够匹配，此时还得确认是匹配到的会不会是自己
                        if (matchInf.getUserId() == key.getUserId()) {
                            //说明匹配到了自己，应该继续循环
                            continue;
                        } else {
                            //先将他放在比赛池中
                            STATUS_POOL.MATCHED_POOL.put(key, STATUS_POOL.MATCHING_POOL.get(key));
                            STATUS_POOL.MATCHED_POOL.put(matchInf, STATUS_POOL.MATCHING_POOL.get(matchInf));

                            //将匹配的两个人凡在map里面
                            STATUS_POOL.PK.put(matchInf.getUserId(), key.getUserId());

                            //匹配到的不是自己，应该移除自己和匹配到的对象，进入比赛的池子
                            STATUS_POOL.MATCHING_POOL.remove(key);
                            STATUS_POOL.MATCHING_POOL.remove(matchInf);
                            flag = false;
                            break;
                        }
                    }
                }
            }else {
                flag=false;
            }
        }
    }
}
