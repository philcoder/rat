package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

public class OutputDto {
	private String command;

	private List<String> outputs;

	public OutputDto() {
		outputs = new ArrayList<>();
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
}
