
package com.jovision.tools;

/**
 * 类似 Handler 的事件通知接口
 * 
 * @author neo
 */
public interface IHandlerLikeNotify {

    /**
     * 消息通知
     * 
     * @param what 分类
     * @param arg1 参数1
     * @param arg2 参数2
     * @param obj 附加对象
     */
    public void onNotify(int what, int arg1, int arg2, Object obj);
}
