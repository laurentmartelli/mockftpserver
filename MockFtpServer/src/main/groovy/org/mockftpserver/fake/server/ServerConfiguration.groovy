/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mockftpserver.fake.server

import org.mockftpserver.fake.filesystem.FileSystem
import org.mockftpserver.fake.user.UserAccount

/**
 * Interface for objects that provide access to server-specific information.
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
interface ServerConfiguration {

    /**
     * @return the {@link FileSystem}    for this server
     */
    FileSystem getFileSystem()

    /**
     * @return the {@link UserAccount}    configured for this server for the specified user name
     */
    UserAccount getUserAccount(String username)

    /**
     * @return the {@link ResourceBundle}    used by this server for reply messages
     */
    ResourceBundle getReplyTextBundle()

    /**
     * @return the System Name for this server (used by the SYST command)
     */
    String getSystemName()


    /**
     * Return the help text for a command or the default help text if no command name is specified
     * @param name - the command name; may be empty or null to indicate  a request for the default help text
     * @return the help text for the named command or the default help text if no name is supplied
     */
    String getHelpText(String name)

}