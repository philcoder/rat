var onlineDevices = null;

$(document).ready(function(){
	loadDataOnTable()
	
	//auto refresh every 10s
//    setInterval(() => {
//    	loadDataOnTable();
//	}, 10000);
});

function loadDataOnTable(){
	console.log("load table")
	$.ajax({
        async: true,
        url: '/manager/web/api/show/online/machines',
        method: 'GET',
        success: function(serverData){
//        	console.log(serverData);
        	
        	if(onlineDevices != null){
        		onlineDevices.destroy()
        	}
        	onlineDevices = $("#online_devices_table").DataTable({
                "searching": false,
                "ordering": true,
                "lengthChange": true,
                "iDisplayLength": 5,
                "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, "All"]],
                data: serverData,
                columns: [
                	{
                		data: null, 
                		"orderable": false,
                		className: 'text-center',
                        searchable: false,
                        orderable: false,
                        render: function (data, type, full, meta) {
                            return '<input type="checkbox" id="check_' + data.id + '" class="check" name="check" value="' + data.id + '">';
                        },
                        width: "10px"
                	},
                	{data: "hostname", "orderable": true},
                    {data: "ip", "orderable": true},
                    {data: "port", "orderable": true},
                    {data: "windowsVersion", "orderable": true},
                    {data: "antivirus", "orderable": true},
                    {data: "firewall", "orderable": true}
                ],
                order: [[1, 'asc']]
            });
        	
        	injectReloadButtonOnDatatables();
        }
    })
}

function injectReloadButtonOnDatatables(){
	$("#online_devices_table_wrapper div.row div.col-sm-12:eq(1)").addClass("dataTables_filter")
	$("#online_devices_table_wrapper div.row div.col-sm-12:eq(1)").html('<button type="button" class="btn btn-primary" id="btn_reload_table" name="reloadTable" onclick="loadDataOnTable()">Reload</button>');
}

function executeCommandsFromMachines(){
	var selectMachineIds = []
	$("input:checked").each(function() {
		selectMachineIds.push($(this).val())
	});
	
	if(selectMachineIds.length > 0){
		$('#message_error').html("")
		
		cmdInput = $("#cmd_input").val()
		if(cmdInput === ""){
			$('#cmd_input_error').html("This field cannot be empty")
            $('#cmd_input').addClass("is-invalid")
		}else{
			$('#cmd_input').removeClass("is-invalid")
			
			sentCommands(selectMachineIds, cmdInput)
		}
	}else{
		$('#message_error').html("You must select at least one host")
	}
}


function sentCommands(selectMachineIds, cmdInput){
    var data = {
        machine_id_list : selectMachineIds,
        cmd_input : cmdInput
    }

    $.ajax({
        async: true,
        url: '/manager/web/api/execute/online/machines/commands',
        method: 'POST',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        data: JSON.stringify(data),
        success: function(serverData){
        	$("#cmd_output").empty() //cleanup
        	for (i = 0; i < serverData.length; i++) {
        		if('message' in serverData[i] === true){
        			populateOutputFailed(serverData[i])
        		}else{
        			populateOutputSuccess(serverData[i])
        		}
        	}
        },
        error: function(serverData, status){
        	console.log(serverData)
        }
    })
}

function populateOutputSuccess(host){
	$("#cmd_output").append("--------------------------------------------------------------------------------------------------------\n")
	$("#cmd_output").append("HOST: [" + host.netInfo.hostname + "] " + host.netInfo.ip + ":" + host.netInfo.port+"\n")
	$("#cmd_output").append("--------------------------------------------------------------------------------------------------------\n")
	$("#cmd_output").append("CMD: "+host.logs[0].commands+"\n")
	$("#cmd_output").append("****************************************************************************************************\n")
	for (i = 0; i < host.logs[0].outputs.length; i++) {
		$("#cmd_output").append(host.logs[0].outputs[i]+"\n")
	}
}

function populateOutputFailed(host){
	$("#cmd_output").append("--------------------------------------------------------------------------------------------------------\n")
	$("#cmd_output").append("HOST: [" + host.netInfo.hostname + "] " + host.netInfo.ip + ":" + host.netInfo.port+"\n")
	$("#cmd_output").append("--------------------------------------------------------------------------------------------------------\n")
	$("#cmd_output").append("CMD: "+host.logs[0].commands+"\n")
	$("#cmd_output").append("--------------------------------------------------------------------------------------------------------\n")
	$("#cmd_output").append("ERROR: "+host.message+"\n")
	$("#cmd_output").append("****************************************************************************************************\n")
}







