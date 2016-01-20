package com.goku.im.net.web.server.conf;

/**
 * @author moueimei
 */
public class ServerConfig {
    private int backlog = 102400;
    private int soTimeout = 30000;
    private int connTimeout = 10000;
    private boolean reuseAddr = true;
    private boolean keepAlive = true;
    private long readIdleTime = 0L;
    private long writeIdleTime = 0L;
    private long allIdleTime = 0L;
    private int maxPostSize = 20 * 1024 * 1024;

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public boolean isReuseAddr() {
        return reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr) {
        this.reuseAddr = reuseAddr;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public long getReadIdleTime() {
        return readIdleTime;
    }

    public void setReadIdleTime(long readIdleTime) {
        this.readIdleTime = readIdleTime;
    }

    public long getWriteIdleTime() {
        return writeIdleTime;
    }

    public void setWriteIdleTime(long writeIdleTime) {
        this.writeIdleTime = writeIdleTime;
    }

    public long getAllIdleTime() {
        return allIdleTime;
    }

    public void setAllIdleTime(long allIdleTime) {
        this.allIdleTime = allIdleTime;
    }

    public int getMaxPostSize() {
        return maxPostSize;
    }

    public void setMaxPostSize(int maxPostSize) {
        this.maxPostSize = maxPostSize;
    }
}