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

// for @see
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;

/**
 * This class defines a message authentication policy.
 *
 * <p>
 * A ClientAuthContext uses this class to communicate (at module initialization time) request and response message
 * protection policies to its ClientAuthModule objects. A ServerAuthContext uses this class to communicate request and
 * response message protection policies to its ServerAuthModule objects.
 * 
 * @see ClientAuthContext
 * @see ServerAuthContext
 * @see ClientAuthModule
 * @see ServerAuthModule
 */
public class MessagePolicy {

	private TargetPolicy[] targetPolicies;
	private boolean mandatory;

	/**
	 * Create a MessagePolicy instance with an array of target policies.
	 *
	 * @param targetPolicies an array of target policies.
	 *
	 * @param mandatory A boolean value indicating whether the MessagePolicy is mandatory or optional.
	 *
	 * @exception IllegalArgumentException if the specified targetPolicies is null.
	 */
	public MessagePolicy(TargetPolicy[] targetPolicies, boolean mandatory) {
		if (targetPolicies == null) {
			throw new IllegalArgumentException("invalid null targetPolicies");
		}
		this.targetPolicies = targetPolicies.clone();
		this.mandatory = mandatory;
	}

	/**
	 * Get the MessagePolicy modifier.
	 *
	 * @return A boolean indicating whether the MessagePolicy is optional(false) or required(true).
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * Get the target policies that comprise the authentication policy.
	 *
	 * <p>
	 * When this method returns an array of target policies, the order of elements in the array represents the order that
	 * the corresponding message transformations or validations described by the target policies are to be performed by the
	 * authentication module.
	 *
	 * @return An array of target authentication policies, where each element describes an authentication policy and the
	 * parts of the message to which the authentication policy applies. This method returns null to indicate that no
	 * security operations should be performed on the messages to which the policy applies. This method never returns a
	 * zero-length array.
	 * 
	 */
	public TargetPolicy[] getTargetPolicies() {
		return targetPolicies.clone();
	}

	/**
	 * This class defines the message protection policies for specific Targets.
	 *
	 * <p>
	 * This class is used to associate a message protection policy with targets within a message. Message targets are
	 * represented using an implementation of the <i>Target</i> interface matched to the message types in MessageInfo. The
	 * message protection policy is identified by an implementation of the <i>ProtectionPolicy</i> interface.
	 *
	 * @see ClientAuthModule
	 * @see ServerAuthModule
	 */
	public static class TargetPolicy {

		private Target[] targets;
		private ProtectionPolicy protectionPolicy;

		/**
		 * Create a TargetPolicy instance with an array of Targets and with a ProtectionPolicy.
		 *
		 * @param targets An array of Targets. This argument may be null.
		 *
		 * @param protectionPolicy The object that describes the message authentication policy that applies to the targets.
		 *
		 * @exception IllegalArgumentException if the specified targets or protectionPolicy is null.
		 */
		public TargetPolicy(Target[] targets, ProtectionPolicy protectionPolicy) {
			if (protectionPolicy == null) {
				throw new IllegalArgumentException("invalid null protectionPolicy");
			}
			if (targets == null || targets.length == 0) {
				this.targets = null;
			} else {
				this.targets = targets.clone();
			}
			this.protectionPolicy = protectionPolicy;
		}

		/**
		 * Get the array of layer-specific target descriptors that identify the one or more message parts to which the specified
		 * message protection policy applies.
		 *
		 * @return An array of <i>Target</i> that identify targets within a message. This method returns null when the specified
		 * policy applies to the whole message (excluding any metadata added to the message to satisfy the policy). This method
		 * never returns a zero-length array.
		 */
		public Target[] getTargets() {
			return targets;
		}

		/**
		 * Get the ProtectionPolicy that applies to the targets.
		 *
		 * @return A ProtectionPolicy object that identifies the message authentication requirements that apply to the targets.
		 */
		public ProtectionPolicy getProtectionPolicy() {
			return protectionPolicy;
		}
	}

	/**
	 * This interface is used to represent and perform message targeting. Targets are used by message authentication modules
	 * to operate on the corresponding content within messages.
	 *
	 * <p>
	 * The internal state of a Target indicates whether it applies to the request or response message of a MessageInfo and
	 * to which components it applies within the identified message.
	 */
	public interface Target {

		/**
		 * Get the Object identified by the Target from the MessageInfo.
		 *
		 * @param messageInfo The MessageInfo containing the request or response message from which the target is to be
		 * obtained.
		 *
		 * @return An Object representing the target, or null when the target could not be found in the MessageInfo.
		 */
		Object get(MessageInfo messageInfo);

		/**
		 * Remove the Object identified by the Target from the MessageInfo.
		 *
		 * @param messageInfo The MessageInfo containing the request or response message from which the target is to be removed.
		 */
		void remove(MessageInfo messageInfo);

		/**
		 * Put the Object into the MessageInfo at the location identified by the target.
		 *
		 * @param messageInfo The MessageInfo containing the request or response message into which the object is to be put.
		 * @param data The data to be put into the MessageInfo
		 */
		void put(MessageInfo messageInfo, Object data);
	}

	/**
	 * This interface is used to represent message authentication policy.
	 *
	 * <p>
	 * The internal state of a ProtectionPolicy object defines the message authentication requirements to be applied to the
	 * associated Target.
	 */
	public interface ProtectionPolicy {

		/**
		 * The identifier for a ProtectionPolicy that indicates that the sending entity is to be authenticated.
		 */
		String AUTHENTICATE_SENDER =

		        "#authenticateSender";

		/**
		 * The identifier for a ProtectionPolicy that indicates that the origin of data within the message is to be authenticated
		 * (that is, the message is to be protected such that its recipients can establish who defined the message content).
		 */
		String AUTHENTICATE_CONTENT =

		        "#authenticateContent";

		/**
		 * The identifier for a ProtectionPolicy that indicates that the message recipient is to be authenticated.
		 */
		String AUTHENTICATE_RECIPIENT =

		        "#authenticateRecipient";

		/**
		 * Get the ProtectionPolicy identifier. An identifier may represent a conceptual protection policy (as is the case with
		 * the static identifiers defined within this interface) or it may identify a procedural policy expression or plan that
		 * may be more difficult to categorize in terms of a conceptual identifier.
		 *
		 * @return A String containing a policy identifier. This interface defines some policy identifier constants.
		 * Configuration systems may define and employ other policy identifiers values.
		 */
		String getID();
	}

}
