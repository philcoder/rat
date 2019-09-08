package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ExecuteCommandDto {

	@JsonInclude(Include.NON_NULL)
	private String commands;

	@JsonInclude(Include.NON_EMPTY)
	private List<String> outputs;

	public ExecuteCommandDto() {
		outputs = new ArrayList<>();
	}

	public String getCommands() {
		return commands;
	}

	public void setCommands(String commands) {
		this.commands = commands;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}
}
