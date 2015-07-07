/*******************************************************************************
 * Copyright (c) 2007, 2013 EclipseSource and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rap.e4.tooling;

import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;

public class HelloE4RAPWizard extends AbstractRAPWizard {

	private AbstractRAPTemplate template;

	public void init(IFieldData data) {
		super.init(data);
		setWindowTitle(Messages.helloRAPWizard_windowTitle);
	}

	public ITemplateSection[] createTemplateSections() {
		template = new HelloE4RAPTemplate();
		return new ITemplateSection[] { template };
	}

	@Override
	protected String getServletPath() {
		return template.getServletPath();
	}

	@Override
	protected String getPackageName() {
		return template.getPackageName();
	}

	@Override
	protected String getRequireBundles() {
		return asString(new BundleValueEntry(true, "org.eclipse.rap.rwt",
				"3.0.0", "4.0.0"), new BundleValueEntry(true,
				"org.eclipse.rap.e4", "0.9.0", null),
				BundleValueEntry.bundle("org.eclipse.e4.core.di"),
				BundleValueEntry.bundle("org.eclipse.e4.core.di.extensions"),
				BundleValueEntry.bundle("org.eclipse.rap.jface"),
				BundleValueEntry.bundle("org.eclipse.e4.core.services"),
				BundleValueEntry.bundle("org.eclipse.e4.ui.di"),
				BundleValueEntry.bundle("org.eclipse.equinox.common"));
	}

	public String getImportPackagesAsString() {
		return asString(
				BundleValueEntry.pack("org.osgi.framework"),
				BundleValueEntry.pack("javax.annotation", "1.2.0"),
				BundleValueEntry.pack("javax.inject", "1.0.0")
				);
	}

	@Override
	protected String getActivatorName() {
		return template.getActivatorName();
	}

	@Override
	protected boolean shouldModifyActivator() {
		return true;
	}

	@Override
	protected boolean shouldModifyBuildProperties() {
		return true;
	}

	private static String asString(BundleValueEntry... bundles) {
		StringBuilder b = new StringBuilder();
		for (BundleValueEntry r : bundles) {
			if (b.length() > 0) {
				b.append("," + ResourceModifier.NL + " ");
			}
			b.append(r);
		}
		return b.toString();
	}

	static class BundleValueEntry {
		private String name;
		private String version;
		private boolean bundle;

		public BundleValueEntry(boolean bundle, String name, String min,
				String max) {
			this.name = name;
			this.bundle = bundle;
			if (max == null) {
				version = min;
			} else if (max != null && min != null) {
				version = "[" + min + "," + max + ")";
			}
		}

		public static BundleValueEntry bundle(String name) {
			return new BundleValueEntry(true, name, null, null);
		}
		
		public static BundleValueEntry pack(String name) {
			return new BundleValueEntry(false, name, null, null);
		}
		
		public static BundleValueEntry pack(String name, String version) {
			return new BundleValueEntry(false, name, version, null);
		}

		@Override
		public String toString() {
			return name
					+ ((version != null) ? ";"
							+ (bundle ? "bundle-version" : "version") + "=\""
							+ version + "\"" : "");
		}
	}
}
