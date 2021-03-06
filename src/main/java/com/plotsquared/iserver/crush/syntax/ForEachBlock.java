/**
 * IntellectualServer is a web server, written entirely in the Java language.
 * Copyright (C) 2015 IntellectualSites
 * <p>
 * This program is free software; you can redistribute it andor modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//     IntellectualServer is a web server, written entirely in the Java language.                            /
//     Copyright (C) 2015 IntellectualSites                                                                  /
//                                                                                                           /
//     This program is free software; you can redistribute it and/or modify                                  /
//     it under the terms of the GNU General Public License as published by                                  /
//     the Free Software Foundation; either version 2 of the License, or                                     /
//     (at your option) any later version.                                                                   /
//                                                                                                           /
//     This program is distributed in the hope that it will be useful,                                       /
//     but WITHOUT ANY WARRANTY; without even the implied warranty of                                        /
//     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                         /
//     GNU General Public License for more details.                                                          /
//                                                                                                           /
//     You should have received a copy of the GNU General Public License along                               /
//     with this program; if not, write to the Free Software Foundation, Inc.,                               /
//     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.                                           /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.plotsquared.iserver.crush.syntax;

import com.plotsquared.iserver.object.Request;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class ForEachBlock extends Syntax
{

    public ForEachBlock()
    {
        super( Pattern.compile( "\\{#foreach ([A-Za-z0-9]*).([A-Za-z0-9]*) -> ([A-Za-z0-9]*)\\}([A-Za-z0-9<>\"'-_\\/\\\\ }{}\\n\\s]*)\\{\\/foreach\\}" ) );
    }

    public String process(String content, Matcher matcher, Request r, Map<String, ProviderFactory> factories)
    {
        while ( matcher.find() )
        {
            String provider = matcher.group( 1 );
            String variable = matcher.group( 2 );
            String variableName = matcher.group( 3 );
            String forContent = matcher.group( 4 );

            try
            {
                if ( factories.containsKey( provider.toLowerCase() ) )
                {
                    VariableProvider p = factories.get( provider.toLowerCase() ).get( r );
                    if ( p != null )
                    {
                        if ( !variable.equalsIgnoreCase( "ALL" ) && !p.contains( variable ) )
                        {
                            content = content.replace( matcher.group(), "" );
                        } else
                        {
                            Object o = variable.equalsIgnoreCase( "ALL" ) ? p.getAll().values() : p.get( variable );

                            StringBuilder totalContent = new StringBuilder();
                            if ( o instanceof Object[] )
                            {
                                for ( Object oo : (Object[]) o )
                                {
                                    if ( oo == null )
                                    {
                                        continue;
                                    }
                                    totalContent.append( forContent.replace( "{{" + variableName + "}}", oo.toString() ) );
                                }
                            } else if ( o instanceof Collection )
                            {
                                for ( Object oo : (Collection) o )
                                {
                                    if ( oo == null )
                                    {
                                        continue;
                                    }
                                    totalContent.append( forContent.replace( "{{" + variableName + "}}", oo.toString() ) );
                                }
                            }
                            content = content.replace( matcher.group(), totalContent.toString() );
                        }
                    } else
                    {
                        content = content.replace( matcher.group(), "" );
                    }
                } else
                {
                    content = content.replace( matcher.group(), "" );
                }
            } catch ( final Exception e )
            {
                com.plotsquared.iserver.core.ServerImplementation.getImplementation().log( "Failed to finish the foor loop (" + provider + "." + variable + " -> " + variableName + ") -> " + e.getMessage() );
            }
        }
        return content;
    }
}
