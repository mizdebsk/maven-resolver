package org.eclipse.aether.internal.impl;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple digester for strings.
 */
class SimpleDigest
{

    private MessageDigest digest;

    private long hash;

    public SimpleDigest()
    {
        try
        {
            digest = MessageDigest.getInstance( "SHA-1" );
        }
        catch ( NoSuchAlgorithmException e )
        {
            try
            {
                digest = MessageDigest.getInstance( "MD5" );
            }
            catch ( NoSuchAlgorithmException ne )
            {
                digest = null;
                hash = 13;
            }
        }
    }

    public void update( String data )
    {
        if ( data == null || data.length() <= 0 )
        {
            return;
        }
        if ( digest != null )
        {
            try
            {
                digest.update( data.getBytes( "UTF-8" ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                throw new AssertionError( e );
            }
        }
        else
        {
            hash = hash * 31 + data.hashCode();
        }
    }

    public String digest()
    {
        if ( digest != null )
        {
            StringBuilder buffer = new StringBuilder( 64 );

            byte[] bytes = digest.digest();
            for ( byte aByte : bytes )
            {
                int b = aByte & 0xFF;

                if ( b < 0x10 )
                {
                    buffer.append( '0' );
                }

                buffer.append( Integer.toHexString( b ) );
            }

            return buffer.toString();
        }
        else
        {
            return Long.toHexString( hash );
        }
    }

}
