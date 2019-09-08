package com.philipp.manager.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSetter;

public class InputDto {

	private List<Integer> machineIdsList;

	@NotEmpty
	@Size(max = 256, message = "Command must be a maximum of 256 characters")
	private String command;

	public InputDto() {
		machineIdsList = new ArrayList<>();
	}

	public List<Integer> getMachineIdsList() {
		return machineIdsList;
	}

	@JsonSetter("machine_id_list")
	public void setMachineIdsList(List<Integer> machineIdsList) {
		this.machineIdsList = machineIdsList;
	}

	public String getCommand() {
		return command;
	}

	@JsonSetter("cmd_input")
	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String toString() {
		return "InputDto [machineIdsList=" + machineIdsList + ", command=" + command + "]";
	}

}
