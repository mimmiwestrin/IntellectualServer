/**
 * IntellectualServer is a web server, written entirely in the Java language.
 * Copyright (C) 2015 IntellectualSites
 *
 * This program is free software; you can redistribute it andor modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
import com.plotsquared.iserver.files.FileSystem;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FileSystemTest
{

    @Test(expected = FileSystem.IllegalPathException.class)
    public void testIllegalPath()
    {
        final FileSystem system = new FileSystem( new File( "./" ) );
        system.getPath( ".." );
    }

    @Test
    public void testListFiles()
    {
        final FileSystem system = new FileSystem( new File( "./" ) );
        Assert.assertTrue( system.getPath( "example" ).getSubPaths().length > 0 );
    }

}