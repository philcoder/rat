var commandsTable = null;

$(document).ready(function(){
	loadDataOnTable()
});

function loadDataOnTable(){
	$.ajax({
        async: true,
        url: '/manager/web/api/show/online/machines',
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
                        	//<button type="button" class="btn btn-primary" id="btn_run" name="submitRunCmd" onclick="executeCommandsFromMachines()">Run</button>
                            return '<button type="button" id="btn_history_' + data.id + '" class="btn btn-info" onclick="loadingCommandsHistory(' + data.id + ')">show</button>';
                        },
                        width: "50px"
                	}
                ],
                order: [[1, 'asc']]
            });
        }
    })
}

function loadingCommandsHistory(id){
	console.log("machine id: "+id)
}