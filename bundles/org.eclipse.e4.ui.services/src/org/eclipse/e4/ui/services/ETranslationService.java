/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.services;

public interface ETranslationService {
	public static final String LANGUAGE_TOPIC = "org/eclipse/e4/ui/workbench/language";
	
	public <M> M createInstance(Class<M> messages)
			throws InstantiationException, IllegalAccessException;

	public void setLocale(String locale);

	public String getLocale();

	public String translate(String providerId, String key);
}
