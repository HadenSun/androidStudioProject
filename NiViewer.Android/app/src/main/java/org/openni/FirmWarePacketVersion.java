package org.openni;
public class FirmWarePacketVersion {
	FirmWarePacketVersion(String filename,int hwtype,int major, int minor, int maintenance, int build)
	{
		this.filename = filename;
		this.hwtype = hwtype;
		this.major = major;
		this.minor = minor;
		this.maintenance = maintenance;
		this.build = build;
	}
	public String filename;
	public int hwtype;
	public int major;
	public int minor;
	public int maintenance;
	public int build;
}
