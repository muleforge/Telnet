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

package org.mule.transport.telnet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.retry.RetryContext;
import org.mule.api.transport.PropertyScope;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.transport.NullPayload;
import org.mule.transport.telnet.config.TelnetNamespaceHandler;
import org.mule.util.StringUtils;

/**
 * <code>TelnetMessageDispatcher</code> TODO document
 */
public class TelnetMessageDispatcher extends AbstractMessageDispatcher
{
	private boolean useSudo = false;
	private String sudoPassword = null;
	private int waitTime;
//	private TelnetClientWrapper client = null;
	private final boolean sudoStdioOption;

	/* For general guidelines on writing transports see
	   http://mule.mulesource.org/display/MULE/Writing+Transports */

	public TelnetMessageDispatcher(OutboundEndpoint endpoint)
	{
		super(endpoint);

		String strUseSudo = (String) endpoint.getProperty(TelnetNamespaceHandler.USE_SUDO);
		setUseSudo((Boolean) Boolean.parseBoolean(strUseSudo));
		if(isUseSudo()){
			setSudoPassword((String) endpoint.getProperty(TelnetNamespaceHandler.SUDO_PASSWORD));
		}
		setWaitTime(Integer.parseInt((String) endpoint.getProperty(TelnetNamespaceHandler.WAIT_TIME)));
        
		String strSudoStdioOption = (String) endpoint.getProperty(TelnetNamespaceHandler.SUDO_STDIO_OPTION);
        sudoStdioOption = Boolean.parseBoolean(strSudoStdioOption);
        
		//responseTimeout = endpoint.getResponseTimeout();
		
		/* IMPLEMENTATION NOTE: If you need a reference to the specific
		   connector for this dispatcher use:

		   TelnetConnector cnn = (TelnetConnector)endpoint.getConnector(); */
	}

	public void doConnect() throws Exception
	{

	}

	public void doDisconnect() throws Exception
	{

	}

	public void doDispatch(MuleEvent event) throws Exception
	{

		// TODO Write the client code here to dispatch the event over this
		// transport
		logger.info("do dispatch");
		doSend(event);
		//throw new UnsupportedOperationException("doDispatch");
	}

	public MuleMessage doSend(MuleEvent event) throws Exception
	{
		// TODO Write the client code here to send the event over this
		// transport (or to dispatch the event to a store or repository)

		// TODO Once the event has been sent, return the result (if any)
		// wrapped in a MuleMessage object

		String host = getConnector().getHost();
		int port = getConnector().getPort();
		logger.debug("connect : "+  host +":" + port);
		TelnetClientWrapper client = new TelnetClientWrapper(host, port, this, getWaitTime());
		
		String result = "";
		

		String userId = getConnector().getLoginId();
		String password = getConnector().getPassword();
		int exitStatus = -10;

		//TODO refactoring
		MuleMessage message = buildMuleMessage(event.getMessageAsString(), exitStatus, event.getMessage());

		//it is supposed to enable local echo in this proccess.
		try{
			client.login(userId, password);

			if(isUseSudo())
			{
				String command = event.getMessageAsString();
				Pattern pattern = Pattern.compile("^\\s*sudo");
				Matcher matcher = pattern.matcher(command);
				if(!matcher.find())
				{
					if(isSudoStdioOption())
					{
						command = "sudo -S " + command;
					}else
					{
						command = "sudo " + command;
					}
					message = event.getMessage();
					message.setPayload(command);
				}
				logger.debug("execute command : " + command);
				result = client.sudoExecCommand(message, getSudoPassword());
				
			}
			else
			{
				logger.debug("execute command : " + event.getMessageAsString());
				result = client.execCommand(event.getMessage());
			}
			exitStatus= client.getExitStatus();
		}
		catch(IOException e)
		{
			
			handleException(e);
		}
		finally
		{
			client.logout();
		}

		if (result != null)
		{
			logger.debug("command result : " + result);
		}
		else
		{
			logger.debug("command result is null");
		}

		logger.info("exit status : " + exitStatus);
		message = buildMuleMessage(result, exitStatus, event.getMessage());
		if (logger.isDebugEnabled())
		{
			logger.debug("build message : " + message);
		}

		return message;
	}

	public void doDispose()
	{
		// Optional; does not need to be implemented. Delete if not required

		/* IMPLEMENTATION NOTE: Is called when the Dispatcher is being
		   disposed and should clean up any open resources. */
	}

	/**
	 * 
	 * @param payload
	 * @param exitStatus
	 * @param origilanMessage
	 * @return MuleMessage. This payload is a String that last new-line character is removed. 
	 */
	private MuleMessage buildMuleMessage(String payload, int exitStatus,
			MuleMessage origilanMessage)
	{

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(TelnetConnector.EXIT_STATUS, exitStatus);
		
		MuleMessage message = new DefaultMuleMessage(payload, origilanMessage);
		message.addProperties(params, PropertyScope.OUTBOUND);
		
		if (StringUtils.isEmpty(payload))
		{
			message.setPayload(NullPayload.getInstance());
		}
		else
		{
			payload = StringUtils.chomp(payload);
			message.setPayload(payload);
		}
		
		//TODO setting exception payload if exitStatus != 0.
		/*
		if(exitStatus != 0) 
		{
			logger.debug("#buildMuleMessage() : set exception payload");
			DefaultExceptionPayload ep = new DefaultExceptionPayload(
					new DispatchException(origilanMessage, getEndpoint(), 
							new Exception("failed to execute command. exit status : " + exitStatus)));
			message.setExceptionPayload(ep);
		}
		*/

		return message;
	}


	@Override
	public TelnetConnector getConnector()
	{
		return (TelnetConnector) super.getConnector();
	}
	
	private boolean isUseSudo()
	{
		return useSudo;
	}
	
	public String getSudoPassword()
	{
		return sudoPassword;
	}

	public int getWaitTime()
	{
		return waitTime;
	}

	public void setWaitTime(int waitTime)
	{
		this.waitTime = waitTime;
	}

	public void setSudoPassword(String sudoPassword)
	{
		this.sudoPassword = sudoPassword;
	}

	public void setUseSudo(boolean useSudo)
	{
		this.useSudo =useSudo;
	}
	
	public boolean isSudoStdioOption() {
		return sudoStdioOption;
	}

	
    @Override
    public RetryContext validateConnection(RetryContext retryContext)
    {

        try
        {
    		String host = getConnector().getHost();
    		int port = getConnector().getPort();
    		TelnetClient client = new TelnetClient();
    		client.connect(host, port);
        	boolean b = client.sendAYT(2000);
        	
        	if(b)
        	{
        		retryContext.setOk();
        	} else {
        		retryContext.setFailed(new IOException("sendAYT is false."));
        	}
        }
        catch(Exception e)
        {
        	retryContext.setFailed(e);
        }
        return retryContext;
    }
}
