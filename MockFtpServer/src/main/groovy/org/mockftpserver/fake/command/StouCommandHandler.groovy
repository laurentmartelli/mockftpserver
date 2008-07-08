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
import org.mockftpserver.core.command.ReplyCodes
import org.mockftpserver.core.session.Session
import org.mockftpserver.fake.command.AbstractFakeCommandHandler

/**
 * CommandHandler for the STOU command. Handler logic:
 * <ol>
 *  <li>If the user has not logged in, then reply with 530 and terminate</li>
 *  <li>Send an initial reply of 150</li>
 *  <li>Create a new file within the current directory with a unique name</li>
 *  <li>If file write/store fails, then reply with 553 and terminate</li>
 *  <li>Send a final reply with 226, along with the new unique filename</li>
 * </ol>
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
class StouCommandHandler extends AbstractFakeCommandHandler {

    protected void handle(Command command, Session session) {
        verifyLoggedIn(session)
        this.replyCodeForFileSystemException = ReplyCodes.NEW_FILE_ERROR

        def baseName = command.getOptionalString(0) ?: 'Temp'
        def suffix = System.currentTimeMillis() as String
        def fullFilename = baseName + suffix
        def path = getRealPath(session, fullFilename)
        sendReply(session, ReplyCodes.TRANSFER_DATA_INITIAL_OK)

        session.openDataConnection();
        def contents = session.readData()
        session.closeDataConnection();

        def out = fileSystem.createOutputStream(path, false)
        out.withStream { it.write(contents) }
        sendReply(session, ReplyCodes.TRANSFER_DATA_FINAL_OK, 'stou', [fullFilename])
    }

}