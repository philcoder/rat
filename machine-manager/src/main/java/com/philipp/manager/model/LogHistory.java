package com.philipp.manager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "log_history")
public class LogHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false, length = 256)
	private String command;

	@ElementCollection
	@CollectionTable(name = "log_history_output", joinColumns = @JoinColumn(name = "log_history_id"))
	@Column(name = "output", length = 128)
	private List<String> outputs;

	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime dateTime;

	public LogHistory() {
		this.outputs = new ArrayList<>();
		this.dateTime = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "LogHistory [id=" + id + ", command=" + command + ", outputs=" + outputs + ", dateTime=" + dateTime
				+ "]";
	}
}
