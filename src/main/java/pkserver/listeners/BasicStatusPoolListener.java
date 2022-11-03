package pkserver.listeners;

import pkserver.PkUser;

/**
 * @author yeyeye
 * @Date 2022/10/28 19:12
 */
public class BasicStatusPoolListener implements StatusPoolListener{
    /**
     * 谁在使用该监听器
     */
    private PkUser pkUser;

    public BasicStatusPoolListener(PkUser pkUser) {
        this.pkUser = pkUser;
    }

    @Override
    public void matchedPoolAdded(PkUser user) {

    }

    @Override
    public void matcherPoolRemoved() {

    }

    @Override
    public void matchingPoolAdded(PkUser user) {

    }

    @Override
    public void matchingPoolRemoved() {

    }

    @Override
    public void cancelMatchingPoolAdded() {

    }

    @Override
    public void cancelMatchingPoolRemoved() {

    }

    public PkUser getPkUser() {
        return pkUser;
    }
}
