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


public class TelnetConnectorFactoryTestCase extends FunctionalTestCase
{

    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

    public void testCreateFromFactory() throws Exception
    {
        OutboundEndpoint endpoint = muleContext.getRegistry()
                .lookupEndpointFactory().getOutboundEndpoint(getEndpointURI());
        assertNotNull(endpoint);
        assertNotNull(endpoint.getConnector());
        assertTrue(endpoint.getConnector() instanceof TelnetConnector);
        assertEquals(getEndpointURI(), endpoint.getEndpointURI().toString());
    }

    public String getEndpointURI() 
    {
        // TODO return a valid endpoint URI string for your transport
        // i.e. tcp://localhost:1234
        //throw new UnsupportedOperationException("getEndpointURI");
    	return "telnet://telnet.out";
    }
    
	@Override
	protected String getConfigResources()
	{
		// TODO Auto-generated method stub
		return "src/test/resources/telnet-namespace-config.xml";
	}
}
