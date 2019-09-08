var logCommandsTable = null;

$(document).ready(function(){
	loadDataOnTable()
});

function loadDataOnTable(){
	$.ajax({
        async: true,
        url: '/manager/web/api/history/show/all/machines',
        method: 'GET',
        success: function(serverData){        	
        	devicesTable = $("#devices_table").DataTable({
                "searching": false,
                "ordering": true,
                "lengthChange": true,
                "iDisplayLength": 5,
                "aLengthMenu": [[5, 15, 30, 50, -1], [5, 15, 30, 50, "All"]],
                data: serverData,
                columns: [
                	{data: "status", "orderable": true, className: 'text-center'},
                	{data: "hostname", "orderable": true, className: 'text-center'},
                    {data: "ip", "orderable": true, className: 'text-center'},
                    {data: "port", "orderable": true, className: 'text-center'},
                    {data: "windowsVersion", "orderable": true, className: 'text-center'},
                    {data: "dotNetVersion", "orderable": true, className: 'text-center'},
                    {data: "antivirus", "orderable": true, className: 'text-center'},
                    {data: "firewall", "orderable": true, className: 'text-center'},
                    {data: "diskAvailable", "orderable": true, className: 'text-center'},
                    {data: "diskTotal", "orderable": true, className: 'text-center'},
                    {
                		data: null, 
                		"orderable": false,
                		className: 'text-center',
                        searchable: false,
                        orderable: false,
                        render: function (data, type, full, meta) {
                            return '<button type="button" id="btn_history_' + data.id + '" class="btn btn-info" onclick="loadLogHistory(' + data.id + ')">show</button>';
                        },
                        width: "50px"
                	}
                ],
                order: [[1, 'asc']]
            });
        }
    })
}

function loadLogHistory(id){
	$.ajax({
        async: true,
        url: '/manager/web/api/history/show/machine/logs/'+id,
        method: 'GET',
        success: function(serverData){
        	hiddenOutputTextArea()
        	
        	if(logCommandsTable != null){
        		logCommandsTable.destroy()
        	}
        	
        	logCommandsTable = $("#log_commands_table").DataTable({
                "searching": false,
                "ordering": true,
                "lengthChange": true,
                "iDisplayLength": 5,
                "aLengthMenu": [[5, 15, 30, 50, -1], [5, 15, 30, 50, "All"]],
                data: serverData.logs,
                columns: [
                	{data: "commands", "orderable": true, width: "800px"},
                	{data: "dateTime", "orderable": true, className: 'text-center', width: "150px"},
                    {
                		data: null, 
                		"orderable": false,
                		className: 'text-center',
                        searchable: false,
                        orderable: false,
                        render: function (data, type, full, meta) {
                            return '<button type="button" id="btn_output_' + data.id + '" class="btn btn-info" onclick="loadLogOutput(' + data.id + ')">show</button>';
                        },
                        width: "50px"
                	}
                ],
                order: [[1, 'desc']]
            });
        	
        	$("#machine_hostname").text(serverData.netInfo.hostname);
            $("#machine_ip").text(serverData.netInfo.ip);
            $("#machine_port").text(serverData.netInfo.port);
            
        	$("#show_logs_content").show();
        }
    })
}

function loadLogOutput(id){
	$.ajax({
        async: true,
        url: '/manager/web/api/history/show/machine/log/'+id+'/output',
        method: 'GET',
        success: function(serverData){       
        	$("#cmd").text(serverData.commands);
        	$("#cmd_output").empty();
        	
        	if(serverData.outputs.length > 0){
        		for (i = 0; i < serverData.outputs.length; i++) {
        			console.log(serverData.outputs[i])
        			$("#cmd_output").append(htmlEncode(serverData.outputs[i])+"\n")
        		}
        	}else{
        		$("#cmd_output").append('<EMPTY OUTPUT>')
        	}
        	
            $("#show_output_cmd_content").show();
        }
    })
}

function hiddenOutputTextArea(){
	$("#show_output_cmd_content").hide();
	$("#cmd_output").empty();
}

function htmlEncode(string){
	var el = document.createElement("div");
	el.innerText = string;
	return el.innerHTML;
}