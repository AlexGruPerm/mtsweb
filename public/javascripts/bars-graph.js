function ajax_get(url, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            console.dir('responseText:' + xmlhttp.responseText);
            try {
                var data = JSON.parse(xmlhttp.responseText);
            } catch(err) {
                console.dir(err.message + " in " + xmlhttp.responseText);
                return;
            }
            callback(data);
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}


document.addEventListener("DOMContentLoaded", function(){
var list = document.getElementsByClassName('patt-search-row-class');
for (var i = 0; i < list.length; i++) {
     list[i].addEventListener("click",function(){
     funcOnClick(event);
     }
    );
  }
});


function funcOnClick(event) {
      console.dir("Selected line TR id="+event.currentTarget.id);
       var partsOfStr = event.currentTarget.id.split('-');
       var tickerId  = partsOfStr[3];
       var widthSec  = partsOfStr[4];
       var bPattCnt  = partsOfStr[5];
       var bMaxTsEnd = partsOfStr[6];
      console.dir("tickerId="+tickerId);
      console.dir("widthSec="+widthSec);
      console.dir("bPattCnt="+bPattCnt);
      console.dir("bMaxTsEnd="+bMaxTsEnd);
      console.dir("------------------------");
      //---------------------------------------------------------------
         ajax_get('/getbars/'+tickerId+'/'+widthSec+'/'+bPattCnt+'/'+bMaxTsEnd, function(data) {
         console.dir("JSON data.rows="+ data["data"].length);
         document.getElementById("div-test").innerHTML = data;
         for (var i=0; i < data["data"].length; i++) {
           console.dir("i=["+i+"]  ts_end="+data["data"][i][0]+"  DateTime="+ timeConverter(data["data"][i][0]) +" c="+data["data"][i][1]);
         }
         paintJQBarsGraph(data,widthSec);
        });
      //---------------------------------------------------------------
}


function timeConverter(UNIX_timestamp){
  var a = new Date(UNIX_timestamp);
  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  var year = a.getFullYear();
  var month = months[a.getMonth()];
  var date = a.getDate();
  var hour = a.getHours();
  var min = a.getMinutes();
  var sec = a.getSeconds();
  var time = date + '.' + (a.getMonth()+1) + '.' + year + ' ' + hour + ':' + min + ':' + sec ;
  return time;
}


function paintJQBarsGraph(series,widthSec){
      console.dir("function paintJQBarsGraph");

		var options = {
		    grid: {
        				hoverable: true,
        				clickable: true
        			},
			lines: {
				show: true
			},
			points: {
				show: true
			},
			xaxis: {
				//tickDecimals: 0,
				//tickSize:     1000
				  minTickSize: [1, "minute"],
                  //min: (new Date("2018-08-13Z14:57:00")).getTime(),
                  //max: (new Date("2018-08-13Z15:05:00")).getTime(),
				  mode: "time",
                  timeformat: "%H:%M:%S"//"%d.%m.%Y %H:%M:%S"
			},
			yaxes:  { position: "right" }
		};

        //timeformats https://stackoverflow.com/questions/2507235/jquery-flot-xaxis-time

		var data = [];
		$.plot("#placeholder", data, options);
		// Fetch one series, adding to what we already have
		var alreadyFetched = {};

		// Push the new data onto our existing data array
		console.dir("series.label="+series.label)
		if (!alreadyFetched[series.label]) {
			alreadyFetched[series.label] = true;
			data.push(series);
		}

		$.plot("#placeholder", data, options);

}

	$(function() {

$("<div id='tooltip'></div>").css({
			position: "absolute",
			display: "none",
			border: "1px solid #fdd",
			padding: "2px",
			"background-color": "#fee",
			opacity: 0.80
		}).appendTo("body");

function time(s) {
    return new Date(s * 1e3).toISOString().slice(-13, -5);
}

$("#placeholder").bind("plothover", function (event, pos, item) {
				var str = "(" + pos.x.toFixed(2) + ", " + pos.y.toFixed(2) + ")";
				$("#hoverdata").text(str);

				if (item) {
					var x = item.datapoint[0];
						y = item.datapoint[1];

					$("#tooltip").html(timeConverter(x) + "   -   " + y)
						.css({top: item.pageY+5, left: item.pageX+5})
						.fadeIn(200);
				} else {
					$("#tooltip").hide();
				}

		});

});