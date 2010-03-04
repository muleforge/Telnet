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

import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;

/**
 * <code>TelnetConnector</code> TODO document
 */
public class TelnetConnector extends AbstractConnector
{

	/* This constant defines the main transport protocol identifier */
    public static final String TELNET = "telnet";
    public static final String TELNET_PREFIX = "telnet.";
    public static final String TELNET_OUT = TELNET_PREFIX + "out";
	public static final String EXIT_STATUS = TELNET_OUT + "exit_status";
	
	public int defaultTimeout = 10000;
    
	private String host;
	private int port;
	private String loginId;
	private String password;
	
	private String exitStatusCommand = "echo $?";
	
    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

    /* IMPLEMENTATION NOTE: All configuaration for the transport should be set
       on the Connector object, this is the object that gets configured in
       MuleXml */
	
    public String getExitStatusCommand()
	{
		return exitStatusCommand;
	}

	public void setExitStatusCommand(String exitStatusCommand)
	{
		this.exitStatusCommand = exitStatusCommand;
	}

	public int getDefaultTimeout()
	{
		return defaultTimeout;
	}

	public void setDefaultTimeout(int defaultTimeout)
	{
		this.defaultTimeout = defaultTimeout;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getLoginId()
	{
		return loginId;
	}

	public void setLoginId(String id)
	{
		this.loginId = id;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void doInitialise() throws InitialisationException
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: Is called once all bean properties have been
           set on the connector and can be used to validate and initialise the
           connectors state. */
    }

    public void doConnect() throws Exception
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: Makes a connection to the underlying
           resource. When connections are managed at the receiver/dispatcher
           level, this method may do nothing */
    }

    public void doDisconnect() throws Exception
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: Disconnects any connections made in the
           connect method If the connect method did not do anything then this
           method shouldn't do anything either. */
    }

    public void doStart() throws MuleException
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: If there is a single server instance or
           connection associated with the connector i.e.  AxisServer or a Jms
           Connection or Jdbc Connection, this method should put the resource
           in a started state here. */
    }

    public void doStop() throws MuleException
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: Should put any associated resources into a
           stopped state. Mule will automatically call the stop() method. */
    }

    public void doDispose()
    {
        // Optional; does not need to be implemented. Delete if not required

        /* IMPLEMENTATION NOTE: Should clean up any open resources associated
           with the connector. */
    }

    public String getProtocol()
    {
        return TELNET;
    }
}
