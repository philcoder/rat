package com.philipp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "machine", indexes = { @Index(columnList = "name, ip, port", name = "machine_find_idx") })
public class Machine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotEmpty
	@Size(max = 32, message = "Hostname must be a maximum of 32 characters")
	@Column(nullable = false, length = 32)
	private String name;

	@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
	@Size(min = 7, max = 15)
	@NotEmpty
	@Column(nullable = false)
	private String ip;

	@Min(1)
	@Max(65535)
	@Column(nullable = false)
	private int port;

	@NotEmpty
	@Size(max = 20, message = "Windows version must be a maximum of 20 characters")
	@Column(nullable = false, length = 20)
	private String windowsVersion;

	@NotEmpty
	@Size(max = 20, message = ".NET version must be a maximum of 20 characters")
	@Column(nullable = false, length = 20)
	private String dotNetVersion;

	@NotEmpty
	@Size(max = 32, message = "Disk available must be a maximum of 32 characters")
	@Column(nullable = false, length = 32)
	private String diskAvailable;

	@NotEmpty
	@Size(max = 32, message = "Disk total must be a maximum of 32 characters")
	@Column(nullable = false, length = 32)
	private String diskTotal;

	@Column(nullable = false)
	private boolean antivirus;// true = installed

	@Column(nullable = false)
	private boolean firewall;// true = actived

	@JsonIgnore
	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime lastSeen;

	public Machine() {
		this.lastSeen = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public LocalDateTime getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}

	@Override
	public String toString() {
		return "Machine [id=" + id + ", name=" + name + ", ip=" + ip + ", windowsVersion=" + windowsVersion
				+ ", dotNetVersion=" + dotNetVersion + ", diskAvailable=" + diskAvailable + ", diskTotal=" + diskTotal
				+ ", antivirus=" + antivirus + ", firewall=" + firewall + ", lastSeen=" + lastSeen + "]";
	}

}
