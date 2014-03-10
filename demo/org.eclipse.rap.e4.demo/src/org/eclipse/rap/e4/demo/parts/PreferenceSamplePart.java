package org.eclipse.rap.e4.demo.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.rap.e4.preferences.EPreference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class PreferenceSamplePart {
	
	private Label value;
	
	private Color currentColor;

	@Optional
	@Inject
	@Preference
	EPreference prefs;
	
	@Inject
	public PreferenceSamplePart(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Label l = new Label(parent, SWT.NONE);
		l.setText("Current Value:");
		
		value = new Label(parent, SWT.NONE);
		value.setText("-");
		
		l = new Label(parent, SWT.NONE);
		l.setText("Color");
		
		final Text t = new Text(parent, SWT.BORDER);
		t.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				prefs.setString("colorPref", t.getText());
			}
		});
		t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	@Inject
	public void updatePreference(@Optional @Preference("colorPref") String v) {
		if( v == null ) {
			return;
		}
		value.setText(v);
		Color c = new Color(value.getDisplay(), toRGB(v));
		value.getParent().setBackground(c);
		
		if( currentColor != null ) {
			currentColor.dispose();	
		}
		currentColor = c;
		
		value.getParent().layout(true);
	}
	
	private RGB toRGB(String hexcolor) {
		String r;
		String g;
		String b;
		if( hexcolor.length() == 3 ) {
			r = hexcolor.substring(0, 1)+hexcolor.substring(0, 1);
			g = hexcolor.substring(1, 2)+hexcolor.substring(1, 2);
			b = hexcolor.substring(2, 3)+hexcolor.substring(2, 3);
		} else {
			r = hexcolor.substring(0, 2);
			g = hexcolor.substring(2, 4);
			b = hexcolor.substring(4, 6);
		}
		return new RGB(Integer.parseInt(r, 16),Integer.parseInt(g, 16),Integer.parseInt(b, 16));
	}
}
