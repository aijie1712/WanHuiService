package com.cmeplaza.basemodule.utils.app.crash;


import com.cmeplaza.basemodule.utils.app.crash.handlers.EndCurrenPagerHandler;
import com.cmeplaza.basemodule.utils.app.crash.handlers.IgnoreHandler;
import com.cmeplaza.basemodule.utils.app.crash.handlers.KillAppHandler;

/**
 * Created by zhangzheng on 2017/4/5.
 */

public class HandlerExceptionFactory implements IHandlerExceptionFactory {
    @Override
    public IHandlerException get(Throwable e) {
        if(e instanceof IllegalStateException){
            return new EndCurrenPagerHandler();
        }

        if(e instanceof SecurityException){
            return new KillAppHandler();
        }

        return new IgnoreHandler();
    }
}
