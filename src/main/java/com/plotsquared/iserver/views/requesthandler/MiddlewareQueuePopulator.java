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
package com.plotsquared.iserver.views.requesthandler;

import java.util.ArrayList;
import java.util.Collection;

public final class MiddlewareQueuePopulator
{

    private final Collection<Class<? extends Middleware>> middlewares = new ArrayList<>();

    public void add(final Class<? extends Middleware> middleware)
    {
        if ( Middleware.class == middleware )
        {
            return;
        }
        try
        {
            middleware.getConstructor();
        } catch ( final Exception e )
        {
            e.printStackTrace();
            return;
        }
        this.middlewares.add( middleware );
    }

    public MiddlewareQueue generateQueue()
    {
        final MiddlewareQueue queue = new MiddlewareQueue();
        middlewares.forEach( clazz ->
        {
            try
            {
                queue.add( clazz.newInstance() );
            } catch ( final Exception e )
            {
                e.printStackTrace();
            }
        } );
        return queue;
    }

}
