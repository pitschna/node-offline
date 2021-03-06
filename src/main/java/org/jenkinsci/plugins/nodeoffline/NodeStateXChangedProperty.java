/*
 * The MIT License
 *
 * Copyright (c) 2012 Red Hat, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.nodeoffline;

import hudson.Extension;
import hudson.model.Node;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Configure list of email addresses as a property of a Node to be used for
 * notification purposes.
 */
public class NodeStateXChangedProperty extends NodeProperty<Node> {

	private final String emailRecipients;

	@DataBoundConstructor
	public NodeStateXChangedProperty(final String emailRecipients) {
		this.emailRecipients = emailRecipients;
	}

	public String getEmailRecipients() {
		return emailRecipients;
	}

	@Override
	public NodePropertyDescriptor getDescriptor() {
		return new DescriptorImpl();
	}

	@Extension
	public static class DescriptorImpl extends NodePropertyDescriptor {

		public static final String EMAIL_RECIPIENTS = "emailRecipients";

		@Override
		public boolean isApplicable(Class<? extends Node> nodeType) {
			return true;
		}

		@Override
		public NodeProperty<?> newInstance(final StaplerRequest req,
				final JSONObject formData) throws FormException {

			final String emailRecipients = formData.getString(EMAIL_RECIPIENTS);

			assert emailRecipients != null;

			if (emailRecipients.isEmpty())
				return null;

			return new NodeStateXChangedProperty(emailRecipients);
		}

		public FormValidation doCheckEmailRecipients(
				@QueryParameter String value) {
			return NodeStateChangedMailer.validateMailAddresses(value);
		}

		@Override
		public String getDisplayName() {
			return "Notify when node online status changes";
		}
	}

}
