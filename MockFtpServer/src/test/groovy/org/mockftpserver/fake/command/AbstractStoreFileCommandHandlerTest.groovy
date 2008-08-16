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
import org.mockftpserver.fake.filesystem.FileSystemException

/**
 * Abstract superclass for tests of Fake CommandHandlers that store a file (STOR, STOU, APPE)
 *
 * @version $Revision: 80 $ - $Date: 2008-07-07 22:15:50 -0400 (Mon, 07 Jul 2008) $
 *
 * @author Chris Mair
 */
abstract class AbstractStoreFileCommandHandlerTest extends AbstractLoginRequiredCommandHandlerTest {

    def DIR = "/"
    def FILENAME = "file.txt"
    def FILE = p(DIR, FILENAME)
    def CONTENTS = "abc"

    void testHandleCommand_CreateOutputStreamThrowsException() {
        def newMethod = {String path, boolean append ->
            println "Calling createOutputStream() - throwing exception"
            throw new FileSystemException("bad")
        }
        overrideMethod(fileSystem, "createOutputStream", newMethod)

        handleCommand([FILE])
        assertSessionReplies([ReplyCodes.TRANSFER_DATA_INITIAL_OK, ReplyCodes.NEW_FILE_ERROR])
    }

    //-------------------------------------------------------------------------
    // Abstract Method Declarations
    //-------------------------------------------------------------------------

    /**
     * Verify the created output file and return its full path
     * @return the full path to the created output file; the path may be absolute or relative
     */
    protected abstract String verifyOutputFile()

    ;

    //-------------------------------------------------------------------------
    // Helper Methods
    //-------------------------------------------------------------------------

    CommandHandler createCommandHandler() {
        new AppeCommandHandler()
    }

    Command createValidCommand() {
        return new Command(CommandNames.APPE, [FILE])
    }

    void setUp() {
        super.setUp()
        assert fileSystem.createDirectory(DIR)
    }

    protected void testHandleCommand(List parameters, String messageKey, String contents) {
        session.dataToRead = CONTENTS.bytes
        handleCommand(parameters)
        assertSessionReply(0, ReplyCodes.TRANSFER_DATA_INITIAL_OK)
        assertSessionReply(1, ReplyCodes.TRANSFER_DATA_FINAL_OK, messageKey)

        def outputFile = verifyOutputFile()

        def actualContents = fileSystem.createInputStream(outputFile).text
        assert actualContents == contents
    }

}