package com.predic8.plugin.membrane.contentproviders;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.predic8.membrane.core.RuleManager;

public class RulesViewContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
		RuleManager ruleManager = (RuleManager)inputElement;
		
		return ruleManager.getRules().toArray();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}