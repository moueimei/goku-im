package com.goku.im.relation.util;

/**
 * Created by moueimei on 15/12/3.
 */
public class RedisPageNumber
{
    private int pageNum = 0;
    private int pageSize = 10;
    private int start = 0;
    private int end = 0;

    public RedisPageNumber(int pageNum, int pageSize) throws Exception
    {
        if(pageNum <= 0 || pageSize <= 0)
            throw new Exception("invalid parameter.");

        this.pageNum = pageNum;
        this.pageSize = pageSize;
        init();
    }

    private void init()
    {
        start = (pageNum - 1) * pageSize;
        end = start + pageSize - 1;
    }

    public int getStart()
    {
        return this.start;
    }

    public int getEnd()
    {
        return this.end;
    }
}
