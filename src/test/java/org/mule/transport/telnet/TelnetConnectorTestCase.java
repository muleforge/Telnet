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

import org.mule.api.transport.Connector;
import org.mule.transport.AbstractConnectorTestCase;

public class TelnetConnectorTestCase extends AbstractConnectorTestCase
{

    @Override
	public void testConnectorListenerSupport() throws Exception
	{
		// not supported
	}
    
    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

    public Connector createConnector() throws Exception
    {
        /* IMPLEMENTATION NOTE: Create and initialise an instance of your
           connector here. Do not actually call the connect method. */

        TelnetConnector c = new TelnetConnector();
        c.setName("telnetConnector");
        // TODO Set any additional properties on the connector here
        
    	String host = "localhost";
    	int port = 23;

    	String loginId = "user";
    	String password = "password";

    	int defaultTimeout = 20000;
        
    	c.setHost(host);
    	c.setPort(port);
    	c.setLoginId(loginId);
    	c.setPassword(password);
    	c.setDefaultTimeout(defaultTimeout);
    	
        return c;
    }
    
    

    public String getTestEndpointURI()
    {
        // TODO Return a valid endpoint for you transport here
        //throw new UnsupportedOperationException("getTestEndpointURI");
    	return "telnet://telnet.out";
    }

    public Object getValidMessage() throws Exception
    {
        // TODO Return an valid message for your transport
        //throw new UnsupportedOperationException("getValidMessage");
    	return "root\n";
    }


    public void testProperties() throws Exception
    {
        // TODO test setting and retrieving any custom properties on the
        // Connector as necessary
    	return;
    }

    
}
