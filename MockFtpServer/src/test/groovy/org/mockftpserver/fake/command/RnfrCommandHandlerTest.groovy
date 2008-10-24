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
package org.mockftpserver.fake.command

import org.mockftpserver.core.command.Command
import org.mockftpserver.core.command.CommandHandler
import org.mockftpserver.core.command.CommandNames
import org.mockftpserver.core.command.ReplyCodes
import org.mockftpserver.core.session.SessionKeys

/**
 * Tests for RnfrCommandHandler
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
class RnfrCommandHandlerTest extends AbstractFakeCommandHandlerTest {

    def FILE = "/file.txt"

    void testHandleCommand() {
        createFile(FILE)
        commandHandler.handleCommand(createCommand([FILE]), session)
        assertSessionReply(ReplyCodes.RNFR_OK, 'rnfr')
        assert session.getAttribute(SessionKeys.RENAME_FROM) == FILE
    }

    void testHandleCommand_PathIsRelative() {
        createFile(FILE)
        session.setAttribute(SessionKeys.CURRENT_DIRECTORY, "/")
        commandHandler.handleCommand(createCommand(["file.txt"]), session)
        assertSessionReply(ReplyCodes.RNFR_OK, 'rnfr')
        assert session.getAttribute(SessionKeys.RENAME_FROM) == FILE
    }

    void testHandleCommand_PathDoesNotExistInFileSystem() {
        commandHandler.handleCommand(createCommand([FILE]), session)
        assertSessionReply(ReplyCodes.READ_FILE_ERROR, ['filesystem.doesNotExist', FILE])
        assert session.getAttribute(SessionKeys.RENAME_FROM) == null
    }

    void testHandleCommand_PathSpecifiesADirectory() {
        createDirectory(FILE)
        commandHandler.handleCommand(createCommand([FILE]), session)
        assertSessionReply(ReplyCodes.READ_FILE_ERROR, ['filesystem.isNotAFile', FILE])
        assert session.getAttribute(SessionKeys.RENAME_FROM) == null
    }

    void testHandleCommand_MissingPathParameter() {
        testHandleCommand_MissingRequiredParameter([])
    }

    //-------------------------------------------------------------------------
    // Helper Methods
    //-------------------------------------------------------------------------

    CommandHandler createCommandHandler() {
        new RnfrCommandHandler()
    }

    Command createValidCommand() {
        return new Command(CommandNames.RNFR, [FILE])
    }

}