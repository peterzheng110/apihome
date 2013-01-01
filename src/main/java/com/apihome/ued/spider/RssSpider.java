package com.apihome.ued.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apihome.ued.task.RssTask;
import com.apihome.ued.task.impl.RssTaskImpl;

/**
 * Rss 解析
 * @author root
 *
 */
@Component("rssSpider")
public class RssSpider 
{
	protected static Log logger = LogFactory.getLog(RssSpider.class);
	
    private static final int corePoolSize = 2;
    private static final int maxPoolSize = 15;
    private static final int keepAliveTime = 10;
    private static final int workQueue = 20;
    
    /** 线程池 */
    private static ThreadPoolExecutor spiderThreadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                    keepAliveTime, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(workQueue),
                    new ThreadPoolExecutor.CallerRunsPolicy());
    

    
    @Autowired
    private RssTaskImpl rssTaskImpl;
    
    /**
     * 定时开启rss文章抓取的任务
     */
    public void rssTask()
    {
    	List<String> urls = new ArrayList<String>();
    	
    	for (int i = 0; i < urls.size(); i++) 
    	{
    	    newSpiderTaskThread(rssTaskImpl, urls.get(i));
		}
    }

    /**
     * 
     * @param task
     * @param url
     */
    public void newSpiderTaskThread(final RssTask task,final String rss)
    {
    	spiderThreadPool.execute(new Runnable()
        {
            public void run()
            {
            	task.parseRss(rss);
            }
        });
    }

}