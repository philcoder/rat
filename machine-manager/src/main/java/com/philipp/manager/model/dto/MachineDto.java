package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class MachineDto {

	private int id;

	@Valid
	@JsonUnwrapped
	private NetworkInfoDto networkInfoDto = new NetworkInfoDto();

	@NotEmpty
	@Size(max = 40, message = "Windows version must be a maximum of 40 characters")
	private String windowsVersion;

	@NotEmpty
	@Size(max = 32, message = ".NET version must be a maximum of 32 characters")
	private String dotNetVersion;

	@Valid
	private List<DriveDto> drives = new ArrayList<>(3);

	private boolean antivirus;// true = installed

	private boolean firewall;// true = actived

	private String status;

	private String diskAvailable;

	private String diskTotal;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public List<DriveDto> getDrives() {
		return drives;
	}

	public void setDrives(List<DriveDto> drives) {
		this.drives = drives;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public NetworkInfoDto getNetworkInfoDto() {
		return networkInfoDto;
	}

	public void setNetworkInfoDto(NetworkInfoDto networkInfoDto) {
		this.networkInfoDto = networkInfoDto;
	}
}
