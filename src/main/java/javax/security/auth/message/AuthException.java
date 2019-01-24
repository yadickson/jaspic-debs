/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package javax.security.auth.message;

/**
 * A generic authentication exception.
 *
 */
public class AuthException extends javax.security.auth.login.LoginException {

	private static final long serialVersionUID = -1156951780670243758L;

	/**
	 * Constructs an AuthException with no detail message. A detail message is a String that describes this particular
	 * exception.
	 */
	public AuthException() {
		super();
	}

	/**
	 * Constructs an AuthException with the specified detail message. A detail message is a String that describes this
	 * particular exception.
	 *
	 * @param msg The detail message.
	 */
	public AuthException(String msg) {
		super(msg);
	}
}
