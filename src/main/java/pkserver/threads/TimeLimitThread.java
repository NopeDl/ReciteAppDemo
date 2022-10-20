package pkserver.threads;

import pkserver.PkRoom;

/**
 * @author yeyeye
 * @Date 2022/10/20 21:29
 */
public class TimeLimitThread implements Runnable{
    //计时的房间
    private PkRoom pkRoom;

    public TimeLimitThread(PkRoom pkRoom) {
        this.pkRoom = pkRoom;
    }

    @Override
    public void run() {
        long timeLimits = this.pkRoom.getTimeLimits();
        //开始计时
        for (int i=0;i< timeLimits;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //计时结束应该结束此房间游戏
        this.pkRoom.end();
    }
}
