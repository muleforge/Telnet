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

import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.telnet.config.TelnetNamespaceHandler;

/**
 * TODO
 */
public class TelnetNamespaceHandlerTestCase extends FunctionalTestCase
{
    protected String getConfigResources()
    {
        //TODO You'll need to edit this file to configure the properties specific to your transport
        return "telnet-namespace-config.xml";
    }

    public void testTelnetConfig() throws Exception
    {
        TelnetConnector c = (TelnetConnector) muleContext.getRegistry().lookupConnector("telnetConnector");
        assertNotNull(c);
        assertTrue(c.isConnected());
        assertTrue(c.isStarted());
        
        assertEquals(23, c.getPort());
        
        assertEquals(10000, c.getDefaultTimeout());
        
        assertEquals("user", c.getLoginId());
        assertEquals(10000, c.getDefaultTimeout());
        
        
        OutboundEndpoint endpoint = (OutboundEndpoint) muleContext.getRegistry().lookupObject("telnet-out");
        
        assertEquals(Boolean.TRUE.toString(), endpoint.getProperty(TelnetNamespaceHandler.USE_SUDO));
        
        //valid to replace from 'telnet="SEND"' to 'address="telnet://telnet.out"'
        assertEquals("telnet.out", endpoint.getEndpointURI().getAddress());
        
        assertEquals("sudopwd", endpoint.getProperty("sudoPassword"));
        assertEquals("euc-jp", endpoint.getEncoding());
        assertEquals("0", endpoint.getProperty("waitTime"));
        assertEquals(15000, endpoint.getResponseTimeout());
        assertEquals(true, endpoint.isSynchronous());

    }
    
}
