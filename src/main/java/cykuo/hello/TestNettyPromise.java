package cykuo.hello;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author chengyuankuo
 * @date 2022/05/22
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoop loop = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(loop);

        new Thread(() -> {
            log.debug("start thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            promise.setSuccess(10);
        }).start();

        log.debug("wait res");

        Integer res = promise.get();
        log.debug("res={}",res);
    }
}
