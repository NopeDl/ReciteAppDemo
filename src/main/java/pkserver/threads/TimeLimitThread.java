package pkserver.threads;

import pkserver.PkRoom;
import pkserver.StatusPool;

import java.util.List;

/**
 * @author yeyeye
 * @Date 2022/10/20 21:29
 */
public class TimeLimitThread implements Runnable {
    /**
     * 计时的房间
     */
    private final PkRoom pkRoom;

    public TimeLimitThread(PkRoom pkRoom) {
        this.pkRoom = pkRoom;
    }

    @Override
    public void run() {
        long timeLimits = this.pkRoom.getTimeLimits();
        List<PkRoom> pkRoomList = StatusPool.PK_ROOM_LIST;
        //开始计时
        for (int i = 0; i < timeLimits && pkRoomList.contains(this.pkRoom); i++) {
            try {
                Thread.sleep(1000);
                System.out.println("Room:" + this.pkRoom + " 计时器：" + (i+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果计时结束后此房间游戏还未分出胜负
        //强制结束游戏
        if (pkRoomList.contains(this.pkRoom)) {
            this.pkRoom.end();
        }
    }
}
