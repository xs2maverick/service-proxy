package com.predic8.plugin.membrane.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Message;
import com.predic8.plugin.membrane.MembraneUIPlugin;
import com.predic8.plugin.membrane.contentproviders.MessageViewContentProvider;
import com.predic8.plugin.membrane.resources.ImageKeys;
import com.predic8.plugin.membrane.viewcomponents.BaseComp;
import com.predic8.plugin.membrane.viewcomponents.IBaseCompositeHost;

public abstract class AbstractMessageView extends ViewPart implements IBaseCompositeHost {

	protected Exchange exchange;

	private String latestSavePath;

	protected Composite partComposite;

	protected ToolBar toolBar;

	protected ToolItem itemContinue, itemFormat, itemSave;

	protected BaseComp baseComp;

	protected MessageViewContentProvider contentProvider;

	@Override
	public void createPartControl(Composite parent) {

		GridLayout responseLayout = new GridLayout();
		partComposite = new Composite(parent, SWT.NONE);
		partComposite.setLayout(responseLayout);

		toolBar = new ToolBar(partComposite, SWT.NONE);

		itemContinue = new ToolItem(toolBar, SWT.PUSH);
		itemContinue.setText("Continue");
		ImageDescriptor descriptorContinueResponse = MembraneUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_FLAG_GREEN);

		Image iconContinueResponse = descriptorContinueResponse.createImage();
		itemContinue.setImage(iconContinueResponse);
		itemContinue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				baseComp.continuePressed();
			}
		});
		itemContinue.setEnabled(false);

		itemFormat = new ToolItem(toolBar, SWT.PUSH);
		itemFormat.setText("Format");
		ImageDescriptor descriptor2 = MembraneUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_FORMAT);

		Image icon2 = descriptor2.createImage();
		itemFormat.setImage(icon2);
		itemFormat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				baseComp.beautifyBody();
			}
		});
		itemFormat.setEnabled(false);

		itemSave = new ToolItem(toolBar, SWT.PUSH);
		itemSave.setText("Save");
		ImageDescriptor descriptorSaveResponse = MembraneUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_SAVE_MESSAGE);

		Image iconSaveResponse = descriptorSaveResponse.createImage();
		itemSave.setImage(iconSaveResponse);
		itemSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					saveMessage(baseComp.getMsg());
				} catch (Exception e1) {
					MessageDialog.openError(partComposite.getShell(), "Save Error", e1.getMessage());
				}
			}
		});
		itemSave.setEnabled(false);

	}

	protected void saveMessage(Message message) throws Exception {
		String selected = getFileName(message);
		if (selected == null || selected.equals(""))
			return;

		OutputStream os = null;
		try {
			File file = new File(selected);
			if (!file.exists()) {
				file.createNewFile();
			}

			latestSavePath = file.getParent();

			os = new FileOutputStream(file);
			if (message.isBodyEmpty()) {
				PrintWriter printer = new PrintWriter(os);
				printer.write(message.getHeader().toString());
				printer.flush();
			} else {
				os.write(message.getBody().getContent());
				os.flush();
			}
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}

	}

	private String getFileName(Message message) {
		FileDialog fd = new FileDialog(partComposite.getShell(), SWT.SAVE);
		fd.setText("Save Message");
		if (latestSavePath != null && !latestSavePath.equals("")) {
			fd.setFilterPath(latestSavePath);
		} else {
			fd.setFilterPath("C:/");
		}

		fd.setFilterExtensions(new String[] { "*." + getExtension(message) });
		return fd.open();
	}

	private String getExtension(Message message) {
		if (message.getHeader().getContentType() == null)
			return "txt";

		if (message.isCSS()) {
			return "css";
		} else if (message.isHTML()) {
			return "html";
		} else if (message.isJavaScript()) {
			return "js";
		} else if (message.isXML()) {
			return "xml";
		} else if (message.isImage()) {
			String contentType = message.getHeader().getContentType();
			if (contentType.contains("jpeg")) {
				return "jpg";
			} else if (contentType.contains("gif")) {
				return "gif";
			} else if (contentType.contains("png")) {
				return "png";
			} else if (contentType.contains("bmp")) {
				return "bmp";
			} else {
				return "bmp";
			}
		}
		return "txt";
	}

	public void setInput(Exchange newExchange) {
		if (contentProvider == null)
			return;

		contentProvider.inputChanged(exchange, newExchange);
		this.exchange = newExchange;
		baseComp.setMsg(contentProvider.getMessage(exchange));
	}

	public Exchange getExchange() {
		return exchange;
	}

	@Override
	public void setFocus() {

	}

	public void setMessage(Message message) {
		baseComp.setMsg(message);
	}

	public abstract void updateUIStatus(boolean canShowBody);

}