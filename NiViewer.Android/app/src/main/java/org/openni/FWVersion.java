package org.openni;

public class FWVersion {
	public FWVersion()
	{
		this.setM_nhwType(0);
		this.setM_nMajor(0);
		this.setM_nMinor(0);
		this.setM_nmaintenance(0);
		this.setM_nbuild(0) ;
		this.setM_nyear((short)0);
		this.setM_nmonth((short) 0);
		this.setM_nday((short)0);
		this.setM_nhour((short)0);
		this.setM_nmin((short)0);
		this.setM_nsec((short)0);
	}
	public FWVersion(int hwtype,int major,int minor,int maintenance,int build, short year,short month,short day,short hour,short min,short sec)
	{
		this.setM_nhwType(hwtype);
		this.setM_nMajor(major);
		this.setM_nMinor(minor);
		this.setM_nmaintenance(maintenance);
		this.setM_nbuild(build) ;
		this.setM_nyear(year);
		this.setM_nmonth(month);
		this.setM_nday(day);
		this.setM_nhour(hour);
		this.setM_nmin(min);
		this.setM_nsec(sec);
	}
	public int getM_nhwType() {
		return m_nhwType;
	}
	public void setM_nhwType(int m_nhwType) {
		this.m_nhwType = m_nhwType;
	}
	
	public int getM_nMajor() {
		return m_nMajor;
	}
	public void setM_nMajor(int m_nMajor) {
		this.m_nMajor = m_nMajor;
	}

	public int getM_nMinor() {
		return m_nMinor;
	}
	public void setM_nMinor(int m_nMinor) {
		this.m_nMinor = m_nMinor;
	}

	public int getM_nmaintenance() {
		return m_nmaintenance;
	}
	public void setM_nmaintenance(int m_nmaintenance) {
		this.m_nmaintenance = m_nmaintenance;
	}

	public int getM_nbuild() {
		return m_nbuild;
	}
	public void setM_nbuild(int m_nbuild) {
		this.m_nbuild = m_nbuild;
	}

	public int getM_nyear() {
		return m_nyear;
	}
	public void setM_nyear(short m_nyear) {
		this.m_nyear = m_nyear;
	}

	public short getM_nmonth() {
		return m_nmonth;
	}
	public void setM_nmonth(short m_nmonth) {
		this.m_nmonth = m_nmonth;
	}

	public int getM_nday() {
		return m_nday;
	}
	public void setM_nday(short m_nday) {
		this.m_nday = m_nday;
	}

	public int getM_nhour() {
		return m_nhour;
	}
	public void setM_nhour(short m_nhour) {
		this.m_nhour = m_nhour;
	}

	public int getM_nmin() {
		return m_nmin;
	}
	public void setM_nmin(short m_nmin) {
		this.m_nmin = m_nmin;
	}

	public int getM_nsec() {
		return m_nsec;
	}
	public void setM_nsec(short m_nsec) {
		this.m_nsec = m_nsec;
	}

	private int m_nhwType;

	private int m_nMajor;

	private int m_nMinor;

	private int m_nmaintenance;

	private int m_nbuild;

	private short m_nyear;

	private short m_nmonth;

	private short m_nday;

	private short m_nhour;

	private short m_nmin;

	private short m_nsec;

	@SuppressWarnings("unused")
	private short[] reserved = new short[34];
}
