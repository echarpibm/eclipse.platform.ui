/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.views.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.views.markers.MarkerItem;
import org.eclipse.ui.views.markers.internal.MarkerMessages;

class MarkerCategory extends MarkerSupportItem {

	boolean refreshing;

	int start;

	int end;

	MarkerEntry[] children;

	private String name;

	private int severity = -1;

	private Markers markers;

	/**
	 * Create a new instance of the receiver that has the markers between
	 * startIndex and endIndex showing.
	 *
	 * @param markers
	 * @param startIndex
	 * @param endIndex
	 *            the builder used to generate the children lazily.
	 */
	MarkerCategory(Markers markers, int startIndex,
			int endIndex, String categoryName) {
		this.markers = markers;
		start = startIndex;
		end = endIndex;
		refreshing=false;
		name = categoryName;
	}

	@Override
	MarkerSupportItem[] getChildren() {
		if (children == null) {
			MarkerItem[] allMarkers = markers.getMarkerEntryArray();
			int totalSize = getChildrenCount();
			children = new MarkerEntry[totalSize];
			System.arraycopy(allMarkers, start, children, 0, totalSize);
			for (MarkerEntry markerEntry : children) {
				markerEntry.setCategory(this);
			}
		}
		return children;
	}

	@Override
	int getChildrenCount() {
		return end - start + 1;
	}

	@Override
	String getDescription() {
		//see Bug 294959
		//if(refreshing){
		//	//see Bug 294959
		//	return NLS.bind(MarkerMessages.Category_building,
		//			new Object[] { getName() });
		//}
		int size = getChildrenCount();
		MarkerContentGenerator generator = markers.getBuilder().getGenerator();
		boolean limitsEnabled = generator.isMarkerLimitsEnabled();
		int limit = generator.getMarkerLimits();

		if (limitsEnabled && size > limit) {
			return NLS.bind(MarkerMessages.Category_Limit_Label, new Object[] {
					name,
					String.valueOf(limit),
					String.valueOf(getChildrenCount()) });
		}
		if (size == 1)
			return NLS.bind(MarkerMessages.Category_One_Item_Label,
					new Object[] { name });

		return NLS.bind(MarkerMessages.Category_Label, new Object[] { name,
				String.valueOf(size) });

	}

	/**
	 * Get the highest severity in the receiver.
	 *
	 * @return int
	 */
	int getHighestSeverity() {
		if (severity >= 0)
			return severity;
		severity = 0;// Reset to info
		for (MarkerSupportItem supportItem : getChildren()) {
			if (supportItem.isConcrete()) {
				int elementSeverity = supportItem.getAttributeValue(IMarker.SEVERITY, -1);
				if (elementSeverity > severity)
					severity = elementSeverity;
				if (severity == IMarker.SEVERITY_ERROR)// As bad as it gets
					return severity;
			}
		}
		return severity;
	}

	/**
	 * Return the name of the receiver.
	 *
	 * @return String
	 */
	String getName() {
		return name;
	}

	@Override
	MarkerSupportItem getParent() {
		return null;
	}

	@Override
	boolean isConcrete() {
		return false;
	}

	/**
	 * Clear the cached values for performance reasons.
	 */
	@Override
	void clearCache() {
		for (MarkerSupportItem supportItem : getChildren()) {
			supportItem.clearCache();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((markers == null) ? 0 : markers.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarkerCategory other = (MarkerCategory) obj;
		if (markers == null) {
			if (other.markers != null)
				return false;
		} else if (!markers.equals(other.markers))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarkerCategory [name="); //$NON-NLS-1$
		builder.append(name);
		builder.append(", severity="); //$NON-NLS-1$
		builder.append(severity);
		builder.append(", start="); //$NON-NLS-1$
		builder.append(start);
		builder.append(", end="); //$NON-NLS-1$
		builder.append(end);
		builder.append(']');
		return builder.toString();
	}

}
