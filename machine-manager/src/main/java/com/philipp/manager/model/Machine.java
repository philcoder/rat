package com.philipp.manager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

	@Column(nullable = false)
	private boolean online;

	@Column(nullable = false)
	private boolean antivirus;// true = installed

	@Column(nullable = false)
	private boolean firewall;// true = actived

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "machine_id")
	private List<Drive> drives;

	@OneToMany
	@JoinColumn(name = "machine_id")
	private List<LogHistory> logHistories;

	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime lastSeen;

	public Machine() {
		this.online = true;
		this.lastSeen = LocalDateTime.now();
		this.drives = new ArrayList<>(3);
		this.logHistories = new ArrayList<>();
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

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
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

	public List<Drive> getDrives() {
		return drives;
	}

	public void setDrives(List<Drive> drives) {
		this.drives = drives;
	}

	public List<LogHistory> getLogHistories() {
		return logHistories;
	}

	public void setLogHistories(List<LogHistory> logHistories) {
		this.logHistories = logHistories;
	}

	public LocalDateTime getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}

	@Override
	public String toString() {
		return "Machine [id=" + id + ", hostname=" + hostname + ", ip=" + ip + ", port=" + port + ", windowsVersion="
				+ windowsVersion + ", dotNetVersion=" + dotNetVersion + ", online=" + online + ", antivirus="
				+ antivirus + ", firewall=" + firewall + ", drives=" + drives + ", logHistories=" + logHistories
				+ ", lastSeen=" + lastSeen + "]";
	}
}
