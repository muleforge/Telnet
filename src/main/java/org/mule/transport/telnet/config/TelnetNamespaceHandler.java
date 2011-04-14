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

package org.mule.transport.telnet.config;

import org.mule.api.endpoint.EndpointURI;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.endpoint.AbstractEndpoint;
import org.mule.endpoint.AbstractEndpointBuilder;
import org.mule.endpoint.AbstractEndpointURIBuilder;
import org.mule.endpoint.MuleEndpointURI;
import org.mule.endpoint.URIBuilder;
import org.mule.transport.telnet.TelnetConnector;

/**
 * Registers a Bean Definition Parser for handling <code><telnet:connector></code> elements
 * and supporting endpoint elements.
 */
public class TelnetNamespaceHandler extends AbstractMuleNamespaceHandler
{
	
	public static final String TELNET_ATTRIBUTE = "telnet";
	
	public static final String[] ADDRESS_ATTRIBUTES = { TELNET_ATTRIBUTE };
	public static final String MAPPINGS = "SEND=" + TelnetConnector.TELNET_OUT;
	
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String LOGIN_ID = "loginId";
	public static final String PASSWORD = "password";
	public static final String RESOPNSE_TIMEOUT = "responseTimeout"; //alias soTimeout
	public static final String DEFAULT_TIMEOUT = "defaultTimeout";

	public static final String SUDO_PASSWORD = "sudoPassword";
	public static final String USE_SUDO = "useSudo";
	public static final String WAIT_TIME= "waitTime";
	public static final String ENCODING = "encoding";
	public static final String SUDO_STDIO_OPTION = "sudoStdioOption";
	
	public static final String[] ENDPOINT_ATTRIBUTES = {
		SUDO_PASSWORD, USE_SUDO, RESOPNSE_TIMEOUT, WAIT_TIME, ENCODING
		};
	
	public static final String[] ALL_ATTRIBUTES = {
		HOST, PORT, LOGIN_ID, PASSWORD,
		RESOPNSE_TIMEOUT, DEFAULT_TIMEOUT,
		SUDO_PASSWORD, USE_SUDO, SUDO_STDIO_OPTION
		};
	
    public void init()
    {
        /* This creates handlers for 'endpoint', 'outbound-endpoint' and 'inbound-endpoint' elements.
           The defaults are sufficient unless you have endpoint styles different from the Mule standard ones
           The URIBuilder as constants for common required attributes, but you can also pass in a user-defined String[].
         */
        registerStandardTransportEndpoints(TelnetConnector.TELNET, ADDRESS_ATTRIBUTES)
        	.addMapping(TELNET_ATTRIBUTE, MAPPINGS).addAlias(TELNET_ATTRIBUTE, URIBuilder.PATH)
        	.addAlias(USE_SUDO, USE_SUDO)
        	.addAlias(SUDO_STDIO_OPTION, SUDO_STDIO_OPTION)
        	.addAlias(SUDO_PASSWORD, SUDO_PASSWORD)
        	.addAlias(WAIT_TIME, WAIT_TIME)
        	.addAlias(ENCODING, ENCODING)
        	.addAlias(RESOPNSE_TIMEOUT, AbstractEndpointBuilder.PROPERTY_RESPONSE_TIMEOUT);
        /* This will create the handler for your custom 'connector' element.  You will need to add handlers for any other
           xml elements you define.  For more information see:
           http://www.mulesource.org/display/MULE2USER/Creating+a+Custom+XML+Namespace
        */
        registerConnectorDefinitionParser(TelnetConnector.class);
    }
}
