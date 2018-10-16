package org.openni;

import java.util.NoSuchElementException;
public enum UpgradeStatus {
	AXON_LINK_SENDFILE_STATUS_STOP(0),
	AXON_LINK_SENDFILE_STATUS_READY(1),
	AXON_LINK_SENDFILE_STATUS_RECVING(2),
	AXON_LINK_SENDFILE_STATUS_WRITING(3),
	AXON_LINK_SENDFILE_STATUS_SUCCESS(4),
	AXON_LINK_SENDFILE_STATUS_FAILED(5);
	private final int mValue;
	private UpgradeStatus(int value) {
		this.mValue = value;
	}
	public int toNative() {
		return this.mValue;
	}
	public static UpgradeStatus fromNative(int value) {
		for (UpgradeStatus type : UpgradeStatus.values()) {
			if (type.mValue == value) return type;
		}

		throw new NoSuchElementException(String.format("Unknown upgrade status: %d", value));
	}

}
