package com.philipp.manager.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MachineDto extends HostInfoDto {

	@NotEmpty
	@Size(max = 40, message = "Windows version must be a maximum of 40 characters")
	private String windowsVersion;

	@NotEmpty
	@Size(max = 32, message = ".NET version must be a maximum of 20 characters")
	private String dotNetVersion;

	@NotEmpty
	@Size(max = 32, message = "Disk available must be a maximum of 32 characters")
	private String diskAvailable;

	@NotEmpty
	@Size(max = 32, message = "Disk total must be a maximum of 32 characters")
	private String diskTotal;

	private boolean antivirus;// true = installed

	private boolean firewall;// true = actived

	public String getWindowsVersion() {
		return windowsVersion;
	}

	public void setWindowsVersion(String windowsVersion) {
		this.windowsVersion = windowsVersion;
	}

	public String getDotNetVersion() {
		return dotNetVersion;
	}

	public void setDotNetVersion(String dotNetVersion) {
		this.dotNetVersion = dotNetVersion;
	}

	public String getDiskAvailable() {
		return diskAvailable;
	}

	public void setDiskAvailable(String diskAvailable) {
		this.diskAvailable = diskAvailable;
	}

	public String getDiskTotal() {
		return diskTotal;
	}

	public void setDiskTotal(String diskTotal) {
		this.diskTotal = diskTotal;
	}

	public boolean isAntivirus() {
		return antivirus;
	}

	public void setAntivirus(boolean antivirus) {
		this.antivirus = antivirus;
	}

	public boolean isFirewall() {
		return firewall;
	}

	public void setFirewall(boolean firewall) {
		this.firewall = firewall;
	}

	@Override
	public String toString() {
		return "MachineDto [windowsVersion=" + windowsVersion + ", dotNetVersion=" + dotNetVersion + ", diskAvailable="
				+ diskAvailable + ", diskTotal=" + diskTotal + ", antivirus=" + antivirus + ", firewall=" + firewall
				+ ", hostname=" + hostname + ", ip=" + ip + ", port=" + port + "]";
	}
}
