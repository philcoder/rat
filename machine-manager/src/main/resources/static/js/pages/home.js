var onlineDevices = null;

$(document).ready(function(){
	loadDataOnTable()
	
	//auto refresh every 10s
    setInterval(() => {
    	loadDataOnTable();
	}, 10000);
});

function loadDataOnTable(){
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
        }
    })
}


function executeCommandsFromMachines(){
	var selectIds = []
	$("input:checked").each(function() {
		selectIds.push($(this).val())
	});
	
	if(selectIds.length > 0){
		alert("IDS: "+selectIds)
	}else{
		alert('empty cb selection')
	}
}


//function searchMovie(){
//    var data = {
//        movie_name: $('#movie_search').val(),
//    }
//
//    $.ajax({
//        async: true,
//        url: '/webui/searchMovie',
//        method: 'POST',
//        contentType: "application/json; charset=utf-8",
//        dataType: 'json',
//        data: JSON.stringify(data),
//        success: function(serverData){
//            if(serverData.status === "ok"){
//                populateMovieContent(serverData)
//            }else{
//                $('#movie_search_error').html("Movie didn't found in database")
//                $('#movie_search').addClass("is-invalid")
//                $("#movie_content").hide();
//            }
//        }
//    })
//}
//
//function randomMovie(){
//    $.ajax({
//        async: true,
//        url: '/webui/randomMovie',
//        method: 'GET',
//        success: function(serverData){
//            if(serverData.status === "ok"){
//                populateMovieContent(serverData)
//            }
//        }
//    })
//}
//
//function populateMovieContent(serverData){
//    $('#movie_search').val("")
//    $('#movie_search').removeClass("is-invalid")
//
//    $("#movie_title").text(serverData.movie.title);
//    $("#movie_release").text(serverData.movie.release_date);
//    $("#movie_genres").text(serverData.movie.genres);
//    $('#movie_id').attr('value',serverData.movie.id);
//    $("#movie_content").show();
//}
//
//function disableButtons(){
//    $("#btn_recommendation").attr("disabled", true);
//    $("#btn_recommendation").html(
//        `<span class="spinner-border spinner-border-sm"></span>   Recommendation Movie`
//    );
//    $("#btn_search_movie").attr("disabled", true);
//    $("#btn_random_movie").attr("disabled", true);
//}
//
//function enableButtons(){
//    $("#btn_recommendation").attr("disabled", false);
//    $("#btn_recommendation").empty();
//    $("#btn_search_movie").attr("disabled", false);
//    $("#btn_random_movie").attr("disabled", false);
//}
//
//function recommendationProcess(){
//    $("#movie_top5_content").hide();
//    disableButtons()
//
//
//    var data = {
//        movie_id: $('#movie_id').val(),
//        rating: $('#movie_rating').val(),
//    }
//
//    $.ajax({
//        async: true,
//        url: '/webui/recommendation',
//        method: 'POST',
//        contentType: "application/json; charset=utf-8",
//        dataType: 'json',
//        data: JSON.stringify(data),
//        success: function(serverData){
//            if(serverData.status === "ok"){
//                var watchCount = 1
//                var watchInterval = setInterval(function(){
//                    $.ajax({
//                        async: true,
//                        url: '/webui/watchRecommendation',
//                        method: 'POST',
//                        contentType: "application/json; charset=utf-8",
//                        dataType: 'json',
//                        data: JSON.stringify({rating_id : serverData.rating_id}),
//                        success: function(watchServerData){
//                            if(watchServerData.status === "ok"){
//                                stopInterval()
//                                populateMovieTop5Content(watchServerData)
//
//                            }else{
//                                if(watchCount == 5){
//                                    stopInterval()
//                                }
//                            }
//                            watchCount += 1
//                        }
//                    })
//                }, 3000);
//
//                var stopInterval = function () {
//                    clearInterval(watchInterval)
//                    enableButtons()
//                };
//            }else{
//
//            }
//        }
//    })
//}
//
//function populateMovieTop5Content(serverData){
//    $("#movie_top5").empty();
//    $("#btn_recommendation").html(
//        `Recommendation Movie`
//    );
//    serverData.movies.forEach(function(elem){
//        text = []
//        text.push("<li>")
//        text.push("<b>"+elem.title + "</b>; "+elem.genres)
//        text.push("</li>")
//        $("#movie_top5").append(text.join(""));
//    })
//    $("#movie_top5_content").show();
//}