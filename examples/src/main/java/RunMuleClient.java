/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) 2009 Osaka Gas Information System Research Institute Co., Ltd. 
 * All rights reserved.  http://www.ogis-ri.co.jp/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE file.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.transport.telnet.TelnetConnector;

public class RunMuleClient implements Runnable
{
	private static final Log logger = LogFactory.getLog(RunMuleClient.class);
	public static int MAX = 1;
	public static int NUM =1;
	private static final Queue<MuleMessage> responses = new ConcurrentLinkedQueue<MuleMessage> ();
	private static MuleClient client;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		if(args.length == 2)
		{
			MAX = Integer.parseInt(args[0]);
			NUM = Integer.parseInt(args[1]);
		}
		
		//String path = "./conf/mule-telnet-sample-client.xml";
		String path = "./mule-telnet-sample-client.xml";
		client = new MuleClient(path);
		client.getMuleContext().start();
		Thread [] list = new Thread[NUM];
		for(int i=0; i<NUM; i++)
		{
			list[i] = new Thread(new RunMuleClient());
		}
		for(Thread th : list)
		{
			th.start();
		}
		for(Thread th: list)
		{
			th.join();
		}
		client.getMuleContext().dispose();
		client.dispose();
		log();
		return ;
	}

	public void run()
	{
		logger.info("start RunMuleClient");
		
		try{
			Map<String, Object> properties = new HashMap<String, Object>();
			//properties.put(MuleProperties.MULE_EVENT_TIMEOUT_PROPERTY, "30000");
			
			for(int i=0; i<MAX; i++)
			{
				responses.add( client.send("vm://test", "sudo -S echo hello world!", properties));
			}
			
		}catch(MuleException e){
			new RuntimeException(e);
		}
	
	}
	

	private static void log()
	{
		try
		{
			for(MuleMessage response : responses){
				logger.info("response : " + response.getPayloadAsString());
				//System.out.println("response : " + response.getPayloadAsString());
				logger.info("message properties : " + response.getProperty(TelnetConnector.EXIT_STATUS));
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
