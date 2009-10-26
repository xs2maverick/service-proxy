/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane.components;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.predic8.membrane.core.http.Message;
import com.predic8.plugin.membrane.components.messagefolder.MessageTabManager;
import com.predic8.plugin.membrane.viewers.ExchangeViewer;

public abstract class BaseComp extends Composite {

	protected Message msg;

	protected ExchangeViewer exchangeViewer;

	protected MessageTabManager tabManager;

	public BaseComp(Composite parent, int style, ExchangeViewer exchangeViewer) {
		super(parent, style);
		this.exchangeViewer = exchangeViewer;
		GridLayout grid = new GridLayout();
		grid.verticalSpacing = 1;
		grid.marginWidth = 0;
		grid.marginHeight = 0;
		grid.horizontalSpacing = 1;
		grid.numColumns = 2;
		setLayout(grid);

		tabManager = new MessageTabManager(this);
	}

	public void continuePressed() {
		if (msg == null)
			return;

		if (isBodyModified()) {
			tabManager.setBodyModified(false);
			copyBodyFromGUIToModel();
		}
		msg.release();
	}

	public ExchangeViewer getExchangeViewer() {
		return exchangeViewer;
	}

	public Message getMsg() {
		return msg;
	}

	public boolean isContinueEnabled() {
		if (msg == null || msg.hasMsgReleased()) {
			return false;
		}

		msg.getBodyAsStream();
		return true;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
		tabManager.doUpdate(msg);
	}

	public void setMessageEditable(boolean bool) {
		tabManager.setMessageEditable(bool);
	}

	public String getBodyText() {
		return tabManager.getBodyText();
	}

	public void copyBodyFromGUIToModel() {
		tabManager.copyBodyFromGUIToModel();
	}

	public abstract void setFormatEnabled(boolean status);

	public abstract void setSaveEnabled(boolean status);
	
	public void handleError(String errorMessage) {
		msg.setErrorMessage(errorMessage);
		tabManager.doUpdate(msg);
	}

	public abstract String getTabCompositeName();

	public boolean isBodyModified() {
		return tabManager.isBodyModified();
	}

	public void beautifyBody() {
		tabManager.beautify(msg);
	}

	public void doUpdate() {
		tabManager.doUpdate(msg);
	}

}