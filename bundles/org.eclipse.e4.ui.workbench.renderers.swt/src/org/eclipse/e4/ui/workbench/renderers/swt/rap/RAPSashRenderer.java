/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.workbench.renderers.swt.rap;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.osgi.service.event.EventHandler;

/**
 *
 */
public class RAPSashRenderer extends SWTPartRenderer {
	private Set<MElementContainer<MUIElement>> contentProcessing = new HashSet<MElementContainer<MUIElement>>();
	private boolean layoutUpdateInProgress = false;

	@Inject
	private IEventBroker broker;
	
	private EventHandler orientationHandler;
	private EventHandler containerDataHandler;


	private Listener resizeListener = new Listener() {

		public void handleEvent(Event event) {
			if (contentProcessing.isEmpty()) {
				return;
			}
			try {
				layoutUpdateInProgress = true;
				MUIElement e = (MUIElement) event.widget.getData(OWNING_ME);
				if (e instanceof MPartSashContainerElement
						&& ((EObject) e).eContainer() instanceof MPartSashContainer
						&& ((MPartSashContainer) ((EObject) e).eContainer())
								.getRenderer() == RAPSashRenderer.this) {
					updateWeight((MPartSashContainerElement) e);
				}
			} finally {
				layoutUpdateInProgress = false;
			}
		}
	};
	
	private void updateWeight(MPartSashContainerElement e) {
		SashForm s = (SashForm) e.getParent().getWidget();
		Widget child = (Widget) e.getWidget();

		int[] weights = s.getWeights();
		Control[] children = s.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] == child) {
				e.setContainerData(Integer.toString(weights[i]));
			}
		}
	}

	@PostConstruct
	void postConstruct() {
		orientationHandler = new EventHandler() {
			
			public void handleEvent(org.osgi.service.event.Event event) {
				MUIElement element = (MUIElement) event
						.getProperty(UIEvents.EventTags.ELEMENT);
				if (element.getRenderer() == RAPSashRenderer.this) {
					MPartSashContainer c = (MPartSashContainer) element;
					((SashForm) element.getWidget()).setOrientation(c
							.isHorizontal() ? SWT.HORIZONTAL : SWT.VERTICAL);
				}
			}
		};
		containerDataHandler = new EventHandler() {
			
			public void handleEvent(org.osgi.service.event.Event arg0) {
				if (!layoutUpdateInProgress) {
					// TODO Update the weights
				}
			}
		};
		
		broker.subscribe(UIEvents.GenericTile.TOPIC_HORIZONTAL, orientationHandler);
		broker.subscribe(UIEvents.UIElement.TOPIC_CONTAINERDATA,
				containerDataHandler);
	}

	@PreDestroy
	void preDestroy() {

	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		MPartSashContainer sashElement = (MPartSashContainer) element;
		SashForm sashForm = new SashForm((Composite) parent,
				sashElement.isHorizontal() ? SWT.HORIZONTAL : SWT.VERTICAL);
		return sashForm;
	}

	@Override
	public void processContents(MElementContainer<MUIElement> container) {
		try {
			contentProcessing.add(container);
			super.processContents(container);
		} finally {
			contentProcessing.remove(container);
		}
		
		int vIdx = 0;
		int[] weights = new int[container.getChildren().size()];
		for (MUIElement c : container.getChildren()) {
			if (c.isToBeRendered()) {
				weights[vIdx++] = getWeight(c);
			}
		}
		if (weights.length != vIdx) {
			int[] newWeights = new int[vIdx];
			System.arraycopy(weights, 0, newWeights, 0, vIdx);
			weights = newWeights;
		}
		((SashForm) container.getWidget()).setWeights(weights);
	}

	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement,
			MUIElement element) {
		super.childRendered(parentElement, element);
		Widget widget = (Widget) element.getWidget();
		widget.addListener(SWT.Resize, resizeListener);

		if (!contentProcessing.contains(parentElement)) {
			SashForm w = (SashForm) parentElement.getWidget();
			int[] weights = w.getWeights();
			int idx = calcVisibleIndex(element);
			weights[idx] = getWeight(element);
			w.setWeights(weights);
		}
	}
	
	@Override
	public void hideChild(MElementContainer<MUIElement> parentElement,
			MUIElement child) {
		super.hideChild(parentElement, child);
		// TODO Anything to be done??
	}

	private static int getWeight(MUIElement element) {
		String info = element.getContainerData();
		if (info == null || info.length() == 0) {
			element.setContainerData(Integer.toString(10000));
			info = element.getContainerData();
		}

		try {
			int value = Integer.parseInt(info);
			return value;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
