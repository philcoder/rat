package com.philipp.manager.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NetworkInfoDto {

	@NotEmpty
	@Size(max = 40, message = "Hostname must be a maximum of 40 characters")
	private String hostname;

	@Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", message = "IP must be 0.0.0.0 at 255.255.255.255 range")
	@Size(min = 7, max = 15)
	@NotEmpty
	private String ip;

	@Min(1)
	@Max(65535)
	private int port;

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

	@Override
	public String toString() {
		return "NetworkInfoDto [hostname=" + hostname + ", ip=" + ip + ", port=" + port + "]";
	}
}
