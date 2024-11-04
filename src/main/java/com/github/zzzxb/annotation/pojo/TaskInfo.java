package com.github.zzzxb.annotation.pojo;

/**
 * @author zzzxb
 * 2024/11/4
 */
public class TaskInfo {
    public Runnable runnable;
    public int incr;

    public TaskInfo(Runnable runnable, int incr) {
        this.runnable = runnable;
        this.incr = incr;
    }
}
