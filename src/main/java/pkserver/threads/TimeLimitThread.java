package pkserver.threads;

import pkserver.PkRoom;
import pkserver.StatusPool;

/**
 * @author yeyeye
 * @Date 2022/10/20 21:29
 */
public class TimeLimitThread implements Runnable {
    //计时的房间
    private PkRoom pkRoom;

    public TimeLimitThread(PkRoom pkRoom) {
        this.pkRoom = pkRoom;
    }

    @Override
    public void run() {
        long timeLimits = this.pkRoom.getTimeLimits();
        //开始计时
        for (int i = 0; i < timeLimits; i++) {
            try {
                Thread.sleep(1000);
                System.out.println("Room:" + this.pkRoom + " 计时器：" + (i+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果计时结束后此房间游戏还未分出胜负
        //强制结束游戏
        if (StatusPool.PK_ROOM_LIST.contains(this.pkRoom)) {
            this.pkRoom.end();
        }
    }
}
