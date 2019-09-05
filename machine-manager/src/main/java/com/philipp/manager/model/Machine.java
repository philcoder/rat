package com.philipp.manager.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "machine", indexes = { @Index(columnList = "hostname, ip, port", name = "machine_find_idx") })
public class Machine {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false, length = 40)
	private String hostname;

	@Column(nullable = false, length = 15)
	private String ip;

	@Column(nullable = false)
	private int port;

	@Column(nullable = false, length = 40)
	private String windowsVersion;

	@Column(nullable = false, length = 32)
	private String dotNetVersion;

	@Column(nullable = false, length = 32)
	private String diskAvailable;

	@Column(nullable = false, length = 32)
	private String diskTotal;

	@Column(nullable = false)
	private boolean antivirus;// true = installed

	@Column(nullable = false)
	private boolean firewall;// true = actived

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

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
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
		return "Machine [id=" + id + ", hostname=" + hostname + ", ip=" + ip + ", windowsVersion=" + windowsVersion
				+ ", dotNetVersion=" + dotNetVersion + ", diskAvailable=" + diskAvailable + ", diskTotal=" + diskTotal
				+ ", antivirus=" + antivirus + ", firewall=" + firewall + ", lastSeen=" + lastSeen + "]";
	}

}
