package org.eenie.wgj.ui.project.attendance;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Eenie on 2017/5/23 at 15:41
 * Email: 472279981@qq.com
 * Des:
 */

public class StaticHandlerFactory {

    public static StaticHandler create(IStaticHandler iStaticHandler) {

        return new StaticHandler(iStaticHandler);
    }

    static class StaticHandler extends Handler {

        private WeakReference<IStaticHandler> weakRef;

        public StaticHandler(IStaticHandler iStaticHandler) {
            weakRef = new WeakReference<>(iStaticHandler);
        }

        @Override public void handleMessage(Message msg) {
            if (weakRef == null) {
                throw new NullPointerException("IStaticHandler reference can not be null!");
            } else {
                weakRef.get().handleMessage(msg);
            }
        }
    }
}
