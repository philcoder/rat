package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class InputDto {

	private List<Integer> machineIdsList;

	@NotEmpty
	@Size(max = 256, message = "Command must be a maximum of 256 characters")
	private String commands;

	public InputDto() {
		machineIdsList = new ArrayList<>();
	}

	@JsonGetter("machine_id_list")
	public List<Integer> getMachineIdsList() {
		return machineIdsList;
	}

	@JsonSetter("machine_id_list")
	public void setMachineIdsList(List<Integer> machineIdsList) {
		this.machineIdsList = machineIdsList;
	}

	@JsonGetter("cmd_input")
	public String getCommands() {
		return commands;
	}

	@JsonSetter("cmd_input")
	public void setCommands(String commands) {
		this.commands = commands;
	}

	@Override
	public String toString() {
		return "InputDto [machineIdsList=" + machineIdsList + ", commands=" + commands + "]";
	}

}
