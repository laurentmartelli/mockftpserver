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
package org.mockftpserver.fake.filesystem

import org.mockftpserver.core.MockFtpServerException

/**
 * Represents an error that occurs while performing a FileSystem operation.
 *
 * @version $Revision$ - $Date$
 *
 * @author Chris Mair
 */
public class FileSystemException extends MockFtpServerException {

    /** The path involved in the file system operation that caused the exception  */
    String path

    /** The message key for the exception message  */
    String messageKey

    /**
     * Construct a new instance for the specified path and message key
     * @param path - the path involved in the file system operation that caused the exception
     * @param messageKey - the exception message key
     */
    FileSystemException(String path, String messageKey) {
        super(path)
        this.path = path
        this.messageKey = messageKey
    }

    /**
     * @param path - the path involved in the file system operation that caused the exception
     * @param message - the exception message
     * @param cause - the exception cause, wrapped by this exception
     */
    FileSystemException(String path, String messageKey, Throwable cause) {
        super(path)
        this.path = path
        this.messageKey = messageKey
    }

}